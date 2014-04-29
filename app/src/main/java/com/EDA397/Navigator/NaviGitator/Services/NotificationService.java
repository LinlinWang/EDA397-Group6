package com.EDA397.Navigator.NaviGitator.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.EDA397.Navigator.NaviGitator.R;

/**
 * Created by QuattroX on 2014-04-29.
 */
public class NotificationService extends Service{

    private Thread thread;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this,"Service created", Toast.LENGTH_LONG).show();
        thread = new Thread(){
            @Override
            public void run() {
                NotificationManager NotifyManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                Notification notification = builder
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("This is the title")
                        .setContentText("This is the text")
                        .setAutoCancel(true)
                        .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, new Intent(), 0))
                        .build();
                NotifyManager.notify(1111, notification);
            }
        };

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Service destroyed", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Service started", Toast.LENGTH_LONG).show();
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }
}
