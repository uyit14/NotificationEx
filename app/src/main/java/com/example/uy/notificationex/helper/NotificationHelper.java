package com.example.uy.notificationex.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.uy.notificationex.R;

import java.util.Random;

import static com.example.uy.notificationex.MainActivity.getNotificationIcon;

public class NotificationHelper {
    private NotificationHelper() {

    }

    public static void createNotification(Context context, String title, String body, PendingIntent pendingIntent) {
        try {
            String channelId = "notify_001";
            Bitmap bitmap = null;

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setNumber(1)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                    .setLights(0xff00ff00, 300, 100)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent);

            builder.setSmallIcon(getNotificationIcon(builder));
            //set large notification
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int color = 0x066EA9;
                builder.setColor(color);
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round);
            }

            builder.setLargeIcon(bitmap);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                String name = "Mobijuce";
                String description = "Mobijuce";
                int importance = NotificationManager.IMPORTANCE_HIGH; //Important for heads-up notification
                NotificationChannel channel = new NotificationChannel(channelId, name, importance);
                channel.setDescription(description);
                channel.setShowBadge(true);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }


            if (notificationManager != null) {
                Random random = new Random();
                int ranNumber = random.nextInt(100) + 1;
                notificationManager.notify(String.valueOf(ranNumber).hashCode(), builder.build());
            }
        } catch (Exception e) {

        }
    }
}
