package com.example.uees2.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class WifiActivity extends AppCompatActivity {

    Button button;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        webView = findViewById(R.id.wifiViewWeb);
        button = findViewById(R.id.buttonInicio);

       //setContentView(webView);
        webView.setWebViewClient(new WebViewClient());
       webView.loadUrl("http://10.0.1.1");

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
    }
}
