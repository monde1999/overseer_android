package com.adventurers.overseer.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.adventurers.overseer.MapsActivity;
import com.adventurers.overseer.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent i = new Intent(Splash.this, MapsActivity.class);
            Splash.this.startActivity(i);
            Splash.this.finish();
        }, 300);
    }
}