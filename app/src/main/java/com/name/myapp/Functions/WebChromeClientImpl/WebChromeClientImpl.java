package com.advance_native;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.net.Uri;
import android.app.Activity;


public class WebChromeClientImpl extends WebChromeClient {

    private ValueCallback<Uri[]> mFilePathCallback;

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        if (mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
        }
        mFilePathCallback = filePathCallback;

        Intent intent = fileChooserParams.createIntent();
        try {
            ((Activity) webView.getContext()).startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException e) {
            mFilePathCallback = null;
            return false;
        }
        return true;
    }
}
