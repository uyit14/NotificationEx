package com.example.uy.notificationex;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class MainActivity extends AppCompatActivity {

    Button btnNotify;
    private final String CHANNEL_ID = "notify_001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnNotify = findViewById(R.id.btnNotify);
        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNotification();
            }
        });
    }


    public void createNotification(){
        //
        Intent intent = new Intent(this, ResultActivity.class);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Title")
                .setContentText("data notification")
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("data notification"))
                .setLights(0xff00ff00, 300, 100)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        //
        builder.setSmallIcon(getNotificationIcon(builder));
        builder.setLargeIcon(getLargeNotificationIcon(builder));
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Mobijuce";
            String description = "Mobijuce";
            int importance = NotificationManager.IMPORTANCE_HIGH; //Important for heads-up notification
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setShowBadge(false);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        if (notificationManager != null) {
            Random random = new Random();
            int ranNumber = random.nextInt(50) + 1;
            notificationManager.notify(String.valueOf(ranNumber).hashCode(), builder.build());
        }

    }

    private Bitmap getLargeNotificationIcon(NotificationCompat.Builder notificationBuilder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = 0x066EA9;
            notificationBuilder.setColor(color);
            return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        }
        return null;
    }

    public static int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = 0x008000;
            notificationBuilder.setColor(color);
            //TODO will create icon notification later
            return R.mipmap.ic_launcher;
        } else {
            return R.mipmap.ic_launcher;
        }
    }
}
