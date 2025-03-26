package com.eroinnovations.qelmedistore;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
public class FileChooserHandler {
    private WebChromeClientImpl webChromeClientImpl;
    public FileChooserHandler(WebChromeClientImpl webChromeClientImpl) {
        this.webChromeClientImpl = webChromeClientImpl;
    }
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WebChromeClientImpl.FILECHOOSER_RESULTCODE) {
            ValueCallback<Uri[]> uploadMessage = webChromeClientImpl.getUploadMessage();
            if (uploadMessage != null) {
                Uri[] results = null;
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Uri dataUri = data.getData();
                    if (dataUri != null) {
                        results = new Uri[]{dataUri};
                    }
                }
                uploadMessage.onReceiveValue(results);
                webChromeClientImpl.setUploadMessage(null);
            }
        }
    }
}