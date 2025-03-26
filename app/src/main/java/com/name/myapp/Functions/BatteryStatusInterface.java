package com.eroinnovations.qelmedistore;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.os.BatteryManager;
public class BatteryStatusInterface {
    private Context context;
    public BatteryStatusInterface(Context context) {
        this.context = context;
    }
    @JavascriptInterface
    public int getBatteryLevel() {
        int batteryLevel = -1;
        BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        return batteryLevel;
    }
}