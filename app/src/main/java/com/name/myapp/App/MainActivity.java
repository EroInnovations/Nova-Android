package com.advance_native;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebView;

public class MainActivity extends Activity {

    private WebView webView;
    private ValueCallback<Uri[]> mFilePathCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        WebViewInitializer.initializeWebView(this, webView);
        webView.setWebViewClient(new WebViewClientImpl());
        webView.setWebChromeClient(new WebChromeClientImpl());
        webView.loadUrl("file:///android_asset/App/index.html");
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && mFilePathCallback != null) {
            if (resultCode == RESULT_OK && data != null) {
                Uri[] uris = new Uri[]{data.getData()};
                mFilePathCallback.onReceiveValue(uris);
            } else {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = null;
        }
    }
}