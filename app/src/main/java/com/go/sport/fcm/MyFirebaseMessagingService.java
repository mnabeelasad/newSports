package com.go.sport.fcm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.earnextramiles.app.models.eventbus.NotificationToken;
import com.go.sport.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private Handler handler;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
            notificationDialog(remoteMessage.getData().get("body"));
        }

//         Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message_Notification_Body:---> " + remoteMessage.getNotification().getBody());
            Log.e(TAG, "Message_Notification_Tag:---> " + remoteMessage.getData().get("screen"));
            notificationDialog(remoteMessage.getNotification().getBody());

            notificationDialog(remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("onNewToken", s);

        EventBus.getDefault().postSticky(new NotificationToken(s));

    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notificationDialog(String messageBody) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        String NOTIFICATION_CHANNEL_ID = "tutorialspoint_01";
        String NOTIFICATION_CHANNEL_ID = getString(R.string.default_notification_channel_id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription(messageBody);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("GoPlay")
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("GoPlay")
                .setContentText(messageBody)
                .setContentInfo(messageBody);

        Random random = new Random();
        int id = random.nextInt(1000);
        notificationManager.notify(id, notificationBuilder.build());
        //notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    Handler handler1 = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
}

