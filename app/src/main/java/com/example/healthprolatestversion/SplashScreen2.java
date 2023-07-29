package com.example.healthprolatestversion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen2 extends AppCompatActivity {

    ImageView nextButton2;
    TextView skipsplash2, splashthree, splashtwoone, splashtwothree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen2);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        skipsplash2 = findViewById(R.id.skipsplash2);
        splashtwoone = findViewById(R.id.splashtwoone);
        splashtwothree = findViewById(R.id.splashtwothree);
        nextButton2 = findViewById(R.id.nextbtn);
        nextButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreen2.this, SplashScreen3.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,   R.anim.slide_out_left);
            }
        });

        skipsplash2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent skipssplash2 = new Intent(SplashScreen2.this, Dashboard.class);
                startActivity(skipssplash2);
                finish();
            }
        });

        splashtwothree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opensplashtwothree();
            }
        });

        splashtwoone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opensplashtwoone();
            }
        });
    }

    public  void opensplashtwothree(){
        Intent splashthree = new Intent(SplashScreen2.this, SplashScreen3.class);
        startActivity(splashthree);
        finish();
    }

    public void opensplashtwoone(){
        Intent splashtwoone = new Intent(SplashScreen2.this, SplashScreen.class);
        startActivity(splashtwoone);
        finish();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SplashScreen2.this, SplashScreen.class);
        startActivity(intent);
        finish();
    }
}

