package com.example.webviewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {
    private WebView appWV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        appWV = findViewById(R.id.appWV);
        appWV.setWebViewClient(new MyWebViewClient());
        appWV.getSettings().setJavaScriptEnabled(true);
        getIntentAndShowWebView();
    }

    private void getIntentAndShowWebView() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        appWV.loadUrl(url);
    }
}