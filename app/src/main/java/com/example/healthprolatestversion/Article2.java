package com.example.healthprolatestversion;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class Article2 extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ImageView articleback = findViewById(R.id.articleback);

        webView = (WebView) findViewById(R.id.article);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.everydayhealth.com/fitness/all-articles/");



        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        articleback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Article2.this, Article.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Article2.this, Article.class);
        startActivity(intent);
        finish();
    }
    }
