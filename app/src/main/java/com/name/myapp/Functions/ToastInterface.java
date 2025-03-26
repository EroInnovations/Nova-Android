package com.eroinnovations.qelmedistore;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
public class ToastInterface {
    private Context context;
    public ToastInterface(Context context) {
        this.context = context;
    }
    @JavascriptInterface
    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}