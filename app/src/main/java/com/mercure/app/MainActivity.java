package com.mercure.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class MainActivity extends AppCompatActivity {

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
        address = "tcp://192.168.0.27:1883";

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
                findViewById(R.id.frameConnectionFailed).setVisibility(View.VISIBLE);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                int m = Integer.parseInt(new String(message.getPayload()));
                Log.d("[MESSAGE]", m + "");

//              TODO FIX THE SETTING OF VALUES
                switch (topic) {
                    case "speed": {
                        String txt = m + " m/s";
                        ((CustomGauge) findViewById(R.id.displaySpeed)).setValue(m);
                        ((TextView) findViewById(R.id.tvSpeed)).setText(txt);
                        break;
                    }
                    case "angleY": {
                        String txt = m + "°";
                        ((CustomGauge) findViewById(R.id.displayAngleFace)).setValue(m);
                        ((TextView) findViewById(R.id.tvAngleFace)).setText(txt);
                        break;
                    }
                    case "angleX": {
                        String txt = m + "°";
                        ((CustomGauge) findViewById(R.id.displayAngleLateral)).setValue(m);
                        ((TextView) findViewById(R.id.tvAngleLateral)).setText(txt);
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

    public void publishing(String topic, String message)
    {
        try
        {
            client.publish(topic, message.getBytes(),0,false);
            Toast.makeText(this,"Published Message",Toast.LENGTH_SHORT).show();
        } catch ( MqttException e)
        {
            e.printStackTrace();
        }
    }

    private void setSubscription(){
        try{
            client.subscribe("speed",0);
            client.subscribe("angleY",0);
            client.subscribe("angleX",0);
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
                    findViewById(R.id.frameConnecting).setVisibility(View.GONE);
                    findViewById(R.id.frameConnectionFailed).setVisibility(View.GONE);
                    setSubscription();
                    setClientCallbacks();

                    isConnected = true;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("[FAILURE]", "CONNECTION FAILED");
                    Toast.makeText(MainActivity.this, "CONNECTION ECHOUER", Toast.LENGTH_LONG).show();
                    findViewById(R.id.frameConnecting).setVisibility(View.GONE);
                    findViewById(R.id.frameConnectionFailed).setVisibility(View.VISIBLE);

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