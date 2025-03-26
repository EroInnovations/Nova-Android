package com.eroinnovations.qelmedistore;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.app.Activity;
import android.view.WindowManager;


public class WebAppInterface {
    private VibrationInterface vibrationInterface;
    private NotificationInterface notificationInterface;
    private ContactsInterface contactsInterface;
    private EmailInterface emailInterface;
    private NetworkManager networkManager;
    private BatteryStatusInterface batteryStatusInterface;
    private FileManager fileManager;
    private ToastInterface toastInterface;
    private GeoLocationInterface geoLocationInterface;
    private Context context;
    private WebView webView;
    private Activity activity; 

    public WebAppInterface(Context context, WebView webView) {
        this.activity = (Activity) context;
        this.context = context;
        this.webView = webView;
        this.vibrationInterface = new VibrationInterface(context);
        this.notificationInterface = new NotificationInterface(context);
        this.contactsInterface = new ContactsInterface(context);
        this.emailInterface = new EmailInterface(context);
        this.networkManager = new NetworkManager(context);
        this.batteryStatusInterface = new BatteryStatusInterface(context);
        this.fileManager = new FileManager(context);
        this.toastInterface = new ToastInterface(context);
        this.geoLocationInterface = new GeoLocationInterface(context);
    }

    @JavascriptInterface
    public void vibrate(long milliseconds) {
        vibrationInterface.vibrate(milliseconds);
    }

    @JavascriptInterface
    public void showNotification(String title, String message) {
        notificationInterface.showNotification(title, message);
    }

    @JavascriptInterface
    public String getContacts() {
        return contactsInterface.getContacts();
    }

    @JavascriptInterface
    public void addContact(String name, String phoneNumber) {
        contactsInterface.addContact(name, phoneNumber);
    }

    @JavascriptInterface
    public void updateContact(String contactId, String newName, String newPhoneNumber) {
        contactsInterface.updateContact(contactId, newName, newPhoneNumber);
    }

    @JavascriptInterface
    public void deleteContact(String contactId) {
        contactsInterface.deleteContact(contactId);
    }

    @JavascriptInterface
    public void requestEmailPermission() {
        emailInterface.requestEmailPermission();
    }

    @JavascriptInterface
    public String getEmails() {
        return emailInterface.getEmails();
    }

    @JavascriptInterface
    public boolean isNetworkAvailable() {
        return networkManager.isNetworkAvailable();
    }

    @JavascriptInterface
    public boolean isConnectedToWiFi() {
        return networkManager.isConnectedToWiFi();
    }

    @JavascriptInterface
    public boolean isConnectedToMobileData() {
        return networkManager.isConnectedToMobileData();
    }

    @JavascriptInterface
    public int getBatteryLevel() {
        return batteryStatusInterface.getBatteryLevel();
    }

    @JavascriptInterface
    public String readFileAsBase64(String filePath) {
        return fileManager.readFileAsBase64(filePath);
    }

    @JavascriptInterface
    public String readFileAsUTF(String filePath) {
        return fileManager.readFileAsUTF(filePath);
    }

    @JavascriptInterface
    public boolean writeFile(String filePath, String data) {
        return fileManager.writeFile(filePath, data);
    }

    @JavascriptInterface
    public boolean createDirectory(String dirPath) {
        return fileManager.createDirectory(dirPath);
    }

    @JavascriptInterface
    public boolean deleteFile(String filePath) {
        return fileManager.deleteFile(filePath);
    }

    @JavascriptInterface
    public boolean deleteDirectory(String dirPath) {
        return fileManager.deleteDirectory(dirPath);
    }

    @JavascriptInterface
    public boolean moveFile(String sourceFilePath, String destDirPath) {
        return fileManager.moveFile(sourceFilePath, destDirPath);
    }

    @JavascriptInterface
    public void showToast(String message) {
        toastInterface.showToast(message);
    }

    @JavascriptInterface
    public void reloadPage() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void startForegroundService(String title, String message) {
        Intent intent = new Intent(context, ForegroundService.class);
        intent.putExtra(ForegroundService.EXTRA_TITLE, title);
        intent.putExtra(ForegroundService.EXTRA_MESSAGE, message);
        context.startForegroundService(intent);
    }

    @JavascriptInterface
    public void stopForegroundService() {
        Intent intent = new Intent(context, ForegroundService.class);
        context.stopService(intent);
    }

    @JavascriptInterface
    public void requestLocation() {
        geoLocationInterface.requestLocation();
    }

    @JavascriptInterface
    public double getLatitude() {
        return geoLocationInterface.getLatitude();
    }

    @JavascriptInterface
    public double getLongitude() {
        return geoLocationInterface.getLongitude();
    }

    @JavascriptInterface
    public void stopLocationUpdates() {
        geoLocationInterface.stopLocationUpdates();
    }
        @JavascriptInterface
    public void enableScreenCaptureProtection() {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    @JavascriptInterface
    public void disableScreenCaptureProtection() {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }
}