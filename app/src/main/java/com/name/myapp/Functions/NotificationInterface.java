package com.eroinnovations.qelmedistore;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
public class NotificationInterface {
    private Context context;
    private NotificationManagerCompat notificationManager;
    public NotificationInterface(Context context) {
        this.context = context;
        this.notificationManager = NotificationManagerCompat.from(context);
        String channelId = "my_channel";
        CharSequence channelName = "My Channel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        notificationManager.createNotificationChannel(channel);
    }
    public void showNotification(String title, String message) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, flags);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "my_channel")
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);
        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, builder.build());
    }
}