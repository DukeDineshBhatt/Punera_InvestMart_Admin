package com.puneragroups.punerainvestmartadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
//Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        if (hasLoggedIn) {
            //Go directly to main activity.

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }
            }, SPLASH_TIME_OUT);
        } else {


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();

                }
            }, SPLASH_TIME_OUT);


        }

    }


}