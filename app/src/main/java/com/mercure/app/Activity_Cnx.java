package com.mercure.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class Activity_Cnx extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cnx);

        CircularProgressButton btn = (CircularProgressButton) findViewById(R.id.btn_id);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn.startAnimation();
            }
        });


    }
}