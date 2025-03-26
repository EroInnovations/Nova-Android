package com.eroinnovations.qelmedistore;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
public class NetworkManager {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Context context;
    public NetworkManager(Context context) {
        this.context = context;
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
        }
        return false;
    }
    public boolean isConnectedToWiFi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }
    public boolean isConnectedToMobileData() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }
    public int getWiFiSignalStrength() {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5);  
        }
        return -1;  
    }
    public String getMobileNetworkType() {
        if (checkAndRequestPermission()) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                int networkType = telephonyManager.getNetworkType();
                return getNetworkTypeString(networkType);
            }
            return "NO_TELEPHONY_SERVICE";
        }
        return "PERMISSION_DENIED";
    }
    public String getNetworkType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (networkCapabilities != null) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return "WIFI";
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return "MOBILE";
                }
            }
        }
        return "NONE";
    }
    private boolean checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("Permission granted for reading phone state.");
            } else {
                showToast("Permission denied for reading phone state.");
            }
        }
    }
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    private String getNetworkTypeString(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "HSPA+";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "1xRTT";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "eHRPD";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "EVDO_0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "EVDO_A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "EVDO_B";
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_GSM:
                return "GSM";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "iDEN";
            case TelephonyManager.NETWORK_TYPE_IWLAN:
                return "IWLAN";
            case TelephonyManager.NETWORK_TYPE_NR:
                return "NR";  // 5G
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                return "TD-SCDMA";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "UNKNOWN";
            default:
                return "UNKNOWN";
        }
    }
}