package com.advance_native;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class WebAppInterface {
    private VibrationInterface vibrationInterface;
    private NotificationInterface notificationInterface;
    private NavigationBarHider navigationBarHider;
    private NavigationBarShower navigationBarShower;
    private ContactsInterface contactsInterface;

    public WebAppInterface(Context context) {
        vibrationInterface = new VibrationInterface(context);
        notificationInterface = new NotificationInterface(context);
        navigationBarHider = new NavigationBarHider((MainActivity) context); 
        navigationBarShower = new NavigationBarShower((MainActivity) context); 
        contactsInterface = new ContactsInterface(context);
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
    public void hideNavigationBar() {
        navigationBarHider.hideNavigationBar();
    }

    @JavascriptInterface
    public void showNavigationBar() {
        navigationBarShower.showNavigationBar();
    }

    @JavascriptInterface
    public String getContacts() {
        return contactsInterface.getContacts();
    }
}
