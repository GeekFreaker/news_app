package com.example.news_app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.news_app.R;


public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent opener = new Intent(getApplicationContext(),MainActivity.class);
                opener.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(opener);
                finish();
            }
        },2500);
    }
}
