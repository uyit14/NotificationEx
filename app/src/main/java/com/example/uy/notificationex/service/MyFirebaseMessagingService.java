package com.example.uy.notificationex.service;

public class MyFirebaseMessagingService {
    /*
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final String TAG = this.getClass().getSimpleName();
    private String title, body;
    private final String channelId = "notify_001";
    PendingIntent pendingIntent = null;
    Intent intent;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            pushNotification(remoteMessage);
        }

        if (remoteMessage.getNotification() != null) {
        }
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void pushNotification(RemoteMessage remoteMessage) {
        try {
            PushNotificationFromServer pushNotificationFromServer = null;
            Map<String, String> data = remoteMessage.getData();
            for (String name : data.keySet()) {

                String key = name.toString();
                String value = data.get(name).toString();

                JSONObject jsonObject = new JSONObject(value);
                Gson gson = new Gson();
                //get command
                pushNotificationFromServer = gson.fromJson(jsonObject.toString(), PushNotificationFromServer.class);
                title = pushNotificationFromServer.getNotification().getTitle();
                body = pushNotificationFromServer.getNotification().getBody();
                //send to stop
                EventBus.getDefault().postSticky(pushNotificationFromServer);
                EventBus.getDefault().postSticky(new UpdateMessageList(true));
            }

            if (pushNotificationFromServer.getData().getAction() != null) {
                switch (pushNotificationFromServer.getData().getAction()) {
                    case AppConstants.JUCEPAC_RELEASED: {
                        EventBus.getDefault().postSticky(new EventShowPopUpReturnSuccess(true));
                        break;
                    }
                    case AppConstants.CANCEL_RENTAL: {
                        if (pushNotificationFromServer.getData() != null) {
                            intent = new Intent(this, DetailRentalActivity.class);
                            intent.putExtra(AppConstants.RENTAL_ID, pushNotificationFromServer.getData().getRentId());
                            intent.setAction(Long.toString(System.currentTimeMillis()));
                            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        }
                        break;
                    }
                    case AppConstants.JUCEPAC_RETURNED: {
                        //send an event bus to show popup return success
                        EventBus.getDefault().postSticky(new EventShowPopUpReturnSuccess(true));
                        intent = new Intent(this, ReturnJucepacActivity.class);
                        intent.putExtra(AppConstants.RENTAL_ID, pushNotificationFromServer.getData().getRentId());
                        intent.setAction(Long.toString(System.currentTimeMillis()));
                        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        break;
                    }
                    case AppConstants.RENT_JUCEPAC_OVER_24h: {

                    }
                    case AppConstants.RENT_JUCEPAC_OVER_48h: {

                    }
                    case AppConstants.RENT_JUCEPAC_OVER_72h: {

                    }
                    case AppConstants.RENT_JUCEPACS_OVER_24h: {

                    }
                    case AppConstants.RENT_JUCEPACS_OVER_48h: {
                        intent = new Intent(this, RentalHistoryActivity.class);
                        intent.setAction(Long.toString(System.currentTimeMillis()));
                        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                        break;
                    }
                }
            } else {
                intent = new Intent(this, HomeActivity.class);
                intent.setAction(Long.toString(System.currentTimeMillis()));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            }

            NotificationCompat.Builder builder;
            builder = new NotificationCompat.Builder(this, channelId)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setNumber(BaseApplication.getInstance().sPrefManager.getBadgeNumber() + 1)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                    .setLights(0xff00ff00, 300, 100)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent);

            builder.setSmallIcon(getNotificationIcon(builder));
            builder.setLargeIcon(getLargeNotificationIcon(builder));
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String name = "Mobijuce";
                String description = "Mobijuce";
                int importance = NotificationManager.IMPORTANCE_HIGH; //Important for heads-up notification
                NotificationChannel channel = new NotificationChannel(channelId, name, importance);
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
        } catch (Exception e) {
            MobijuceHelper.logExceptionCrashlytics(e);
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
            return R.drawable.ic_notifications_black_24dp;
        } else {
            return R.mipmap.ic_launcher;
        }
    }

    private boolean isAtActivity(String activity) {
        boolean isAtActivity = false;
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        String current = taskInfo.get(0).topActivity.getShortClassName();
        boolean result = current.contains(activity);
        if (result) {
            isAtActivity = true;
        }
        return isAtActivity;
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
    */
}
