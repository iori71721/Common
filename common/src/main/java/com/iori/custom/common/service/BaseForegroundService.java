package com.iori.custom.common.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.UUID;

public abstract class BaseForegroundService extends Service {
    protected String channelID=BaseForegroundService.class.getName();
    protected int notificationID=1;
    protected String ID;
    protected Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        handler=new Handler();
        ID= UUID.randomUUID().toString();
        initNotificationInfos();
        keepAliveTrick();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected abstract void initNotificationInfos();

    private void keepAliveTrick(){
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(channelID,channelID,NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            Notification notification=new NotificationCompat.Builder(this,channelID)
                    .setOngoing(true)
                    .setContentTitle("")
                    .setContentText("").build();
            startForeground(notificationID,notification);
        }else{
            startForeground(notificationID,new Notification());
        }
    }
}
