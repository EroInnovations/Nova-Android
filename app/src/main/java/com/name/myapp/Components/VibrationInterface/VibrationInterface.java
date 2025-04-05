package com.advance_native;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class VibrationInterface {
    private Context mContext;
    private Vibrator vibrator;

    public VibrationInterface(Context context) {
        mContext = context;
        vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @SuppressLint("MissingPermission")
    @JavascriptInterface
    public void vibrate(long milliseconds) {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            showToast("Vibration is not supported on this device.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}