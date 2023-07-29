package com.example.healthprolatestversion;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

public class Article extends AppCompatActivity {
    private WebView webView;

    ImageView feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ImageView articleback = findViewById(R.id.articleback);
//        ImageView articlenext = findViewById(R.id.articlenext);

        webView = (WebView) findViewById(R.id.article);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.mayoclinichealthsystem.org/hometown-health/speaking-of-health/7-tips-to-live-a-happier-life");

        feedback = findViewById(R.id.feedback);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        articleback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Article.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

//        articlenext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Article.this, Article2.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedback = new Intent(Article.this, Heartrate.class);
                startActivity(feedback);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Article.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
    }
