package com.eroinnovations.qelmedistore;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
public class VibrationInterface {
    private Context context;
    private Vibrator vibrator;
    public VibrationInterface(Context context) {
        this.context = context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
    @SuppressLint("MissingPermission")
    @JavascriptInterface
    public void vibrate(long duration) {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            showToast("Vibration is not supported on this device.");
        }
    }
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}