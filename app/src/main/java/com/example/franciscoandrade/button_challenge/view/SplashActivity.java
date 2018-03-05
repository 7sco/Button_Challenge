package com.example.franciscoandrade.button_challenge.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.franciscoandrade.button_challenge.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Thread that works for two seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent view = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(view);
                finish();
            }
        }, 2000);


    }
}
