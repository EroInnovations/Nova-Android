package com.eroinnovations.qelmedistore;
import android.app.Activity;
import android.webkit.WebView;
public class BackPressHandler {
    private WebView webView;
    private Activity activity;
    public BackPressHandler(Activity activity, WebView webView) {
        this.activity = activity;
        this.webView = webView;
    }
    public void handleBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            activity.finish();
        }
    }
}