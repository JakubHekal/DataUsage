package com.jakubhekal.datausage.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.jakubhekal.datausage.R;
import com.jakubhekal.datausage.Utils;
import com.jakubhekal.datausage.managers.PreferenceManager;

public class SplashActivity extends AppCompatActivity {

    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Utils.getCurrentTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferenceManager = new PreferenceManager(this);
        preferenceManager.setLaunched(true);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            Bundle bundle = ActivityOptions.makeCustomAnimation(SplashActivity.this, android.R.anim.fade_in,android.R.anim.fade_out).toBundle();
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent,bundle);
            finish();

        },750);
    }
}