package com.example.cikcanmiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splash_kahve extends AppCompatActivity {
    Handler hndlr = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_kahve);
        hndlr.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splash_kahve.this,MainPage.class);
                startActivity(i);
                finish();
            }
        },2000);
    }
}