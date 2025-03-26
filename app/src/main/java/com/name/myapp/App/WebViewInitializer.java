package com.eroinnovations.qelmedistore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
public class WebViewInitializer {
    @SuppressLint("SetJavaScriptEnabled")
    public static void initializeWebView(Activity activity, WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); 
        webSettings.setDomStorageEnabled(true); 
        webSettings.setAllowFileAccess(true); 
        webSettings.setDatabaseEnabled(true);
        webSettings.setBuiltInZoomControls(false); 
        webSettings.setDisplayZoomControls(false); 
        webSettings.setMediaPlaybackRequiresUserGesture(false); 
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); 
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
    }
}