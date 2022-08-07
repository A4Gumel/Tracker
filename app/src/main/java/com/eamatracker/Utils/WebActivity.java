package com.eamatracker.Utils;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.eamatracker.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.material.appbar.MaterialToolbar;

public class WebActivity extends AppCompatActivity {

    String url, title;
    ProgressBar webViewLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        if (getIntent() != null) {

            url = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("title");

        }

        MaterialToolbar webViewToolbar = findViewById(R.id.webViewToolbar);
        webViewLoading = findViewById(R.id.webSpinKit);
        Sprite doubleBounce = new DoubleBounce();
        webViewLoading.setIndeterminateDrawable(doubleBounce);

        webViewToolbar.setTitle(title);
        webViewToolbar.setNavigationOnClickListener(v -> supportFinishAfterTransition());

        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

               webViewLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }
        });

        myWebView.loadUrl(url);

    }

}