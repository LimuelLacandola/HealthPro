package com.example.healthprolatestversion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    ImageView nextButton;
    TextView skipsplash1, splashtwo, splashonethree;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        skipsplash1 = findViewById(R.id.skipsplash1);
        nextButton = findViewById(R.id.nextbtn);
        splashtwo = findViewById(R.id.splashtwo);
        splashonethree = findViewById(R.id.splashonethree);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreen.this, SplashScreen2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        skipsplash1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent skipsplash1 = new Intent(SplashScreen.this, Dashboard.class);
                startActivity(skipsplash1);
                finish();
            }
        });

        splashtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opensplashtwo();
            }
        });

        splashonethree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opensplashonethree();
            }
        });


    }

    public void opensplashtwo() {
        Intent splashtwo = new Intent(SplashScreen.this, SplashScreen2.class);
        startActivity(splashtwo);
        finish();
    }

    public void opensplashonethree() {
        Intent splashonethree = new Intent(SplashScreen.this, SplashScreen3.class);
        startActivity(splashonethree);
        finish();

    }
    @Override
    public void onBackPressed() {

    }
}
