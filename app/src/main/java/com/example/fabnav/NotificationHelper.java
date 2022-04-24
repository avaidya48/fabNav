package com.example.fabnav;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

public class NotificationHelper extends ContextWrapper {

    private static final String TAG = "NotificationHelper";

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }
    }

    private String CHANNEL_NAME = "High priority channel";
    private String CHANNEL_ID = "com.example.notifications" + CHANNEL_NAME;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setDescription("this is the description of the channel.");
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);
    }

    public void sendHighPriorityNotification(String title, String body, Class activityName, Boolean isDest) {

        Notification notification;
        if(isDest){
            Intent broadcastIntent = new Intent(this, NotificationBroadcastReceiver.class);
            broadcastIntent.putExtra("Test", "test");
            PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setStyle(new NotificationCompat.BigTextStyle().setSummaryText("summary").setBigContentTitle(title).bigText(body))
                    .setAutoCancel(true)
                    .addAction(R.mipmap.ic_launcher, "Cancel", actionIntent)
                    .build();
        }else{
            Intent intent = new Intent(this, activityName);
            intent.putExtra("parkingId","test");
            PendingIntent surveyIntent = PendingIntent.getActivity(this, 267, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setStyle(new NotificationCompat.BigTextStyle().setSummaryText("summary").setBigContentTitle(title).bigText(body))
                    .setAutoCancel(true)
                    .setContentIntent(surveyIntent)
                    .build();
        }


        //.setContentIntent(pendingIntent)

        NotificationManagerCompat.from(this).notify(new Random().nextInt(), notification);
        //return notification;

    }

}
