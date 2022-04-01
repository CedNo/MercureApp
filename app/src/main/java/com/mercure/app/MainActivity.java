package com.mercure.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mercure.app.ui.remote.JoystickView;

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

public class MainActivity extends AppCompatActivity implements JoystickView.JoystickListener {

    public static ConstraintLayout frameConnecting;
    public static ConstraintLayout frameConnectionFailed;
    public static ConstraintLayout frameObstacle;
    ImageView btHomeRefreshConnection;
    private ActivityMainBinding binding;
    Context context;

    MqttAndroidClient client;
    String clientId;

    public static String address;
    public static Boolean isConnected;
    public static String mouvement;

    Vibrator v;
    long[] pattern = {400, 400, 400, 400, 400};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        getSupportActionBar().hide();
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

        frameConnecting = findViewById(R.id.frameConnecting);
        frameConnectionFailed = findViewById(R.id.frameConnectionFailed);
        frameObstacle = findViewById(R.id.frameObstacle);
        btHomeRefreshConnection = findViewById(R.id.btHomeRefreshConnection);
        btHomeRefreshConnection.setOnClickListener(this::refreshConnection);
        isConnected = false;
        mouvement = "stop";

        frameObstacle.setVisibility(View.GONE);


        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "tcp://172.16.207.54:1883";
        address = sharedPref.getString("adresseMQTT", defaultValue);

        clientId = MqttClient.generateClientId();
        connect();
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

                switch (topic) {
                    case "accel": {
                        String[] m = new String(message.getPayload()).split("@", 4);
                        double d0 = Double.parseDouble(m[0]);
                        int i = (int) Math.abs(d0);
                        Log.d("[VITESSE]", Math.abs(i) + "");
                        String txt0 = i + " km/h";
                        ((CustomGauge) findViewById(R.id.displaySpeed)).setValue(i);
                        ((TextView) findViewById(R.id.tvSpeed)).setText(txt0);

                        double d1 = Double.parseDouble(m[1]);
                        int y = (int) (d1* 90);
                        Log.d("[ANGLE Y]", Math.abs(y) + "");
                        String txt1 = Math.abs(y) + "°";
                        ((CustomGauge) findViewById(R.id.displayAngleFace)).setValue(y);
                        ((TextView) findViewById(R.id.tvAngleFace)).setText(txt1);

                        double d2 = Double.parseDouble(m[2]);
                        int x = (int) (d2 * 90);
                        Log.d("[ANGLE X]", Math.abs(x) + "");
                        String txt2 = Math.abs(x) + "°";
                        ((CustomGauge) findViewById(R.id.displayAngleLateral)).setValue(x);
                        ((TextView) findViewById(R.id.tvAngleLateral)).setText(txt2);
                        ((ImageView) findViewById(R.id.imgJeepStats)).setRotation(x);
                        break;
                    }
                    case "sonar":{
                        String distance = new String(message.getPayload());
                        double dist = Double.parseDouble(distance);
                        Log.d("test", distance);

                        if (dist < 15 && dist != 0)
                        {
                            v.vibrate(pattern, -1);
                            frameObstacle.setVisibility(View.VISIBLE);

                        }
                        else frameObstacle.setVisibility(View.GONE);
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
        } catch ( MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishing(String topic, String message)
    {
        if(isConnected) {
            try
            {
                Log.d("[MESSAGE]", topic);
                client.publish(topic, message.getBytes(),0,false);
            } catch ( MqttException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void setSubscription(){
        try{
            client.subscribe("accel",0);
            client.subscribe("video",0);
            client.subscribe("sonar",0);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    public void connect(){
        Log.d("[CONNECTING]", "CALLING MainActivity.connect()...");
        frameConnecting.setVisibility(View.VISIBLE);
        frameConnectionFailed.setVisibility(View.GONE);
        Log.d("[CONNECTING]", address);
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
                    setSubscription();
                    setClientCallbacks();

                    isConnected = true;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("[FAILURE]", "CONNECTION FAILED");
                    Toast.makeText(MainActivity.this, "CONNECTION ECHOUER", Toast.LENGTH_LONG).show();
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


    public void refreshConnection(View view) {
        connect();
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id)
    {
        Log.d("Joystick", " X percent : " + xPercent + " Y percent : " + yPercent);

        if(xPercent == 0.0 && yPercent == 0.0)
        {
            publishing("move", "stop");
            mouvement = "stop";
        }
        // Avancer
        else if((xPercent >= -0.3 && xPercent <= 0.3) && yPercent < -0 && mouvement != "av")
        {
            publishing("move", "avancer");
            mouvement = "av";
        }
        else if((xPercent >= -0.9 && xPercent <= -0.3) && yPercent < 0 && mouvement != "avg" )
        {
            publishing("move", "avGauche");
            mouvement = "avg";
        }
        else if((xPercent >= 0.3 && xPercent <=0.9) && yPercent < 0 && mouvement != "avd" )
        {
            publishing("move", "avDroit");
            mouvement = "avd";
        }
        // Reculer
        else if((xPercent >= -0.3 && xPercent <= 0.3) && yPercent > 0 && mouvement != "ar" )
        {
            publishing("move", "reculer");
            mouvement = "ar";
        }
        else if((xPercent >= -0.9 && xPercent <= -0.3) && yPercent > 0 && mouvement != "arg" )
        {
            publishing("move", "arGauche");
            mouvement = "arg";
        }
        else if((xPercent >= 0.3 && xPercent <=0.9) && yPercent > 0 && mouvement != "ard")
        {
            publishing("move", "arDroit");
            mouvement = "ard";
        }
    }
}
