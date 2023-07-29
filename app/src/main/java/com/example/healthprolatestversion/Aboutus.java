package com.example.healthprolatestversion;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

public class Aboutus extends AppCompatActivity {

    ImageView flaticon, firebase, lottie, material;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        ImageView heartback = findViewById(R.id.aboutback);
        ImageView flaticon = findViewById(R.id.flaticon);
        ImageView firebase = findViewById(R.id.firebase);
        ImageView lottie = findViewById(R.id.lottie);
        ImageView material = findViewById(R.id.material);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        heartback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Aboutus.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        flaticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.flaticon.com/");
                Intent flaticon = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(flaticon);
            }
        });

        firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://firebase.google.com/");
                Intent firebase = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(firebase);
            }
        });

        lottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://lottiefiles.com/");
                Intent lottie = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(lottie);
            }
        });

        material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://developer.android.com/design/ui/mobile/guides/components/material-overview");
                Intent material = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(material);
            }
        });


    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Aboutus.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

}