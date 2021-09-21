package com.example.covid_19tracker;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyApplication extends Application {
public static final String CHANNEL_ID="Message.Channel";
public static final String CHANNEL_ID2="Request.Channel";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,"Message Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Covid Tracker Channel");
            NotificationChannel notificationChannel2=new NotificationChannel(CHANNEL_ID,"Request Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel2.setDescription("Covid Tracker Channel");
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.createNotificationChannel(notificationChannel2);
        }
    }
}
