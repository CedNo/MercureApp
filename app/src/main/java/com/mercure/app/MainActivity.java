package com.mercure.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.mercure.app.databinding.ActivityMainBinding;
import com.mercure.app.ui.home.HomeFragment;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Arrays;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class MainActivity extends AppCompatActivity {

    public static ConstraintLayout frameConnecting;
    public static ConstraintLayout frameConnectionFailed;
    public static ConstraintLayout frameInfos;
    private ActivityMainBinding binding;
    Context context;

    MqttAndroidClient client;
    String clientId;

    public static String address;
    public static Boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_remote, R.id.navigation_history, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        isConnected = false;

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "tcp://172.16.207.54:1883";
        address = sharedPref.getString("adresseMQTT", defaultValue);

        clientId = MqttClient.generateClientId();

        Log.d("[CHECK STATUS]", isConnected.toString());

        if(!isConnected) {
            connect();
        }
    }

    public void setClientCallbacks() {
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                frameConnectionFailed.setVisibility(View.VISIBLE);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("[MESSAGE]", new String(message.getPayload()));

//              TODO FIX THE SETTING OF VALUES
                switch (topic) {
                    case "accel": {
                        String[] m = new String(message.getPayload()).split("@", 4);
                        double d0 = Double.parseDouble(m[0]);
                        int i = (int) Math.abs(d0);
                        Log.d("[VITESSE]", Math.abs(i) + "");
                        String txt0 = i + " m/s";
                        ((CustomGauge) findViewById(R.id.displaySpeed)).setValue(i);
                        ((TextView) findViewById(R.id.tvSpeed)).setText(txt0);

                        double d1 = Double.parseDouble(m[1]);
                        int y = (int) (d1* 90);
                        Log.d("[ANGLE Y]", Math.abs(y) + "");
                        String txt1 = Math.abs(y) + "°";
                        ((CustomGauge) findViewById(R.id.displayAngleFace)).setValue(y);
                        ((TextView) findViewById(R.id.tvAngleFace)).setText(txt1);
                        ((ImageView) findViewById(R.id.imgJeepStats)).setRotationX(-y);

                        double d2 = Double.parseDouble(m[2]);
                        int x = (int) (d2 * 90);
                        Log.d("[ANGLE X]", Math.abs(x) + "");
                        String txt2 = Math.abs(x) + "°";
                        ((CustomGauge) findViewById(R.id.displayAngleLateral)).setValue(x);
                        ((TextView) findViewById(R.id.tvAngleLateral)).setText(txt2);
                        ((ImageView) findViewById(R.id.imgJeepStats)).setRotation(x);
                        break;
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void publishing(){
        String topic = "event";
        String message = "the payload";
        try {
            client.publish(topic, message.getBytes(),0,false);
            Toast.makeText(this,"Published Message",Toast.LENGTH_SHORT).show();
        } catch ( MqttException e) {
            e.printStackTrace();
        }
    }

    private void setSubscription(){
        try{
            client.subscribe("accel",0);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    public void connect(){
        Log.d("[CONNECTING]", "CALLING MainActivity.connect()...");
        client = new MqttAndroidClient(context, address, clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("[SUCCES]", "CONNECTED");
                    Toast.makeText(MainActivity.this, "CONNECTER", Toast.LENGTH_LONG).show();
                    frameConnecting.setVisibility(View.GONE);
                    frameConnectionFailed.setVisibility(View.GONE);
                    frameInfos.setVisibility(View.VISIBLE);
                    setSubscription();
                    setClientCallbacks();

                    isConnected = true;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("[FAILURE]", "CONNECTION FAILED");
                    Toast.makeText(MainActivity.this, "CONNECTION ECHOUER", Toast.LENGTH_LONG).show();
                    frameInfos.setVisibility(View.GONE);
                    frameConnecting.setVisibility(View.GONE);
                    frameConnectionFailed.setVisibility(View.VISIBLE);

                    try {
                        IMqttToken token = client.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    isConnected = false;
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try {
            IMqttToken token = client.disconnect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this,"Disconnected!!",Toast.LENGTH_LONG).show();
                    isConnected = false;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this,"Could not disconnect!!",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void setAddress(String address) {
        MainActivity.address = address;
    }
}