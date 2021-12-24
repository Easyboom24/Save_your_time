package com.example.save_your_time;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class MyService2 extends Service {

    @Override
    public void onCreate(){
        super.onCreate();

        Toast.makeText(this, "Служба создана",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        Intent intent1 = new Intent(this,BlockSettingsActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent1,0);

        Notification notification = new NotificationCompat.Builder(this,"ChannelId1")
                .setContentTitle("My App tutorial")
                .setContentText("Our application")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();
        //com.sololearn
        startForeground(1,notification);
        while(true) {
            ActivityManager am = (ActivityManager) this
                    .getSystemService(ACTIVITY_SERVICE);

// get the info from the currently running task
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().contains("sololearn")) {
                Intent intentMain = new Intent(this, BlockFullActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                this.startActivity(intentMain);
            }
            return START_STICKY;
        }

    }

    private void createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    "ChannelId1","Foreground notification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }
}