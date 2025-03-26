package com.eroinnovations.qelmedistore;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.app.Activity;
import android.app.AlertDialog;
import android.net.Uri;
import android.widget.EditText;
public class WebChromeClientImpl extends WebChromeClient {
    private ValueCallback<Uri[]> uploadMessage;
    public static final int FILECHOOSER_RESULTCODE = 100;
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        if (uploadMessage != null) {
            uploadMessage.onReceiveValue(null);
            uploadMessage = null;
        }
        uploadMessage = filePathCallback;

        Intent intent = fileChooserParams.createIntent();
        try {
            ((Activity) webView.getContext()).startActivityForResult(intent, FILECHOOSER_RESULTCODE);
        } catch (ActivityNotFoundException e) {
            uploadMessage = null;
            return false;
        }
        return true;
    }
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        String appName = view.getContext().getString(R.string.app_name);  
        new AlertDialog.Builder(view.getContext())
            .setTitle(appName)
            .setMessage(message)
            .setIcon(R.drawable.app_icon)
            .setPositiveButton(android.R.string.ok, (dialog, which) -> result.confirm())
            .setCancelable(false)
            .create()
            .show();
        return true;
    }
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        String appName = view.getContext().getString(R.string.app_name);  
        new AlertDialog.Builder(view.getContext())
            .setTitle(appName)
            .setMessage(message)
            .setIcon(R.drawable.app_icon)
            .setPositiveButton(android.R.string.ok, (dialog, which) -> result.confirm())
            .setNegativeButton(android.R.string.cancel, (dialog, which) -> result.cancel())
            .create()
            .show();
        return true;
    }
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
        String appName = view.getContext().getString(R.string.app_name);  
        final EditText input = new EditText(view.getContext());
        input.setText(defaultValue);
        new AlertDialog.Builder(view.getContext())
            .setTitle(appName)
            .setMessage(message)
            .setIcon(R.drawable.app_icon)
            .setView(input)
            .setPositiveButton(android.R.string.ok, (dialog, which) -> result.confirm(input.getText().toString()))
            .setNegativeButton(android.R.string.cancel, (dialog, which) -> result.cancel())
            .create()
            .show();
        return true;
    }
    public ValueCallback<Uri[]> getUploadMessage() {
        return uploadMessage;
    }
    public void setUploadMessage(ValueCallback<Uri[]> uploadMessage) {
        this.uploadMessage = uploadMessage;
    }
}