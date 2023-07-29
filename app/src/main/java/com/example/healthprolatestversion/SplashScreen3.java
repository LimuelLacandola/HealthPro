package com.example.healthprolatestversion;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthprolatestversion.Dashboard;
import com.example.healthprolatestversion.R;
import com.example.healthprolatestversion.SplashScreen2;

public class SplashScreen3 extends AppCompatActivity {

    private Button backButton;
    private ImageView nextButton;

    TextView splashthreeone, splashthreetwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen3);

        splashthreeone = findViewById(R.id.splashthreeone);
        splashthreetwo = findViewById(R.id.splashthreetwo);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        nextButton = findViewById(R.id.nextbtn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreen3.this, Dashboard.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        splashthreeone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opensplashthreeone();
            }
        });

        splashthreetwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opensplashthreetwo();
            }
        });
    }

    public void opensplashthreeone() {
        Intent splashthreeone = new Intent(SplashScreen3.this, SplashScreen.class);
        startActivity(splashthreeone);
        finish();
    }
    public void opensplashthreetwo(){
        Intent splashthreetwo = new Intent(SplashScreen3.this, SplashScreen2.class);
        startActivity(splashthreetwo);
        finish();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SplashScreen3.this, SplashScreen2.class);
        startActivity(intent);
        finish();
    }
}


