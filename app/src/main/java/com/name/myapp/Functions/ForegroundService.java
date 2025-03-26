package com.eroinnovations.qelmedistore;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
public class ForegroundService extends Service {
    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_MESSAGE = "extra_message";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String title = "Foreground Service";
        String message = "Running...";
        if (intent != null) {
            if (intent.hasExtra(EXTRA_TITLE)) {
                title = intent.getStringExtra(EXTRA_TITLE);
            }
            if (intent.hasExtra(EXTRA_MESSAGE)) {
                message = intent.getStringExtra(EXTRA_MESSAGE);
            }
        }
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.app_icon)
            .setContentIntent(getPendingIntent())
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(false) 
            .build();
        startForeground(1, notification);
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void createNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(
            CHANNEL_ID,
            "Foreground Service Channel",
            NotificationManager.IMPORTANCE_LOW 
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(serviceChannel);
        }
    }
    private PendingIntent getPendingIntent() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        return PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }
}