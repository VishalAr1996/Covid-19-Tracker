package com.example.covid_19tracker.ChatSupportActivities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.covid_19tracker.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import static com.example.covid_19tracker.MyApplication.CHANNEL_ID;
import static com.example.covid_19tracker.MyApplication.CHANNEL_ID2;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String title, body ,click_action;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        title = remoteMessage.getNotification().getTitle();
        body = remoteMessage.getNotification().getBody();
        //click_action=remoteMessage.getNotification().getClickAction();




        if(remoteMessage.getData().get("type").equalsIgnoreCase("msg")){
            Intent intent = new Intent(this, ChattingActivity.class);
            intent.putExtra("otherUserKey", remoteMessage.getData().get("userID"));
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_icon)
                    .setAutoCancel(true)
                    .setPriority(NotificationManager.IMPORTANCE_MAX)
                    .build();

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(123, notification);
        }
        if(remoteMessage.getData().get("type").equalsIgnoreCase("request")){

            Intent requestIntent=new Intent(this,ViewUserDetailsActivity.class);
            requestIntent.putExtra("userKey",remoteMessage.getData().get("requestUserId"));
            PendingIntent requestPendingIntent=PendingIntent.getActivity(this, 102, requestIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification requestNotification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentIntent(requestPendingIntent)
                    .setSmallIcon(R.drawable.ic_icon)
                    .setAutoCancel(true)
                    .setPriority(NotificationManager.IMPORTANCE_MAX)
                    .build();

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(124, requestNotification);
        }




    }
}

