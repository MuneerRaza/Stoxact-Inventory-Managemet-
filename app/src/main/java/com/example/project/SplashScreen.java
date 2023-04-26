package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences onBoarding;
    int SPLASH_TIME = 1650;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(() -> {
            onBoarding = getSharedPreferences("onBoarding",MODE_PRIVATE);
            boolean flag = onBoarding.getBoolean("new_user",true);
            if(flag) {
                SharedPreferences.Editor editor = onBoarding.edit();
                editor.putBoolean("new_user",false);
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), OnBoarding.class);
                startActivity(intent);
                finish();

            }
            else{
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME);
    }





}

