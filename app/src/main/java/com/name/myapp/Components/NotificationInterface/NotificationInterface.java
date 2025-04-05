package com.advance_native;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationInterface {
    private Context mContext;
    private NotificationManagerCompat notificationManager;

    public NotificationInterface(Context context) {
        mContext = context;
        notificationManager = NotificationManagerCompat.from(mContext);

        // Create a notification channel if API level is 26 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "my_channel";
            CharSequence channelName = "My Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showNotification(String title, String message) {
        // Create a PendingIntent for when the notification is clicked
        Intent intent = new Intent(mContext, MainActivity.class); // Replace YourActivity with the activity you want to open
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "my_channel")
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Set the PendingIntent
                .setAutoCancel(true); // Close the notification when clicked

        // Show the notification
        int notificationId = (int) System.currentTimeMillis(); // Unique ID for each notification
        notificationManager.notify(notificationId, builder.build());
    }
}