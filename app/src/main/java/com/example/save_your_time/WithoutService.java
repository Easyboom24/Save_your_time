package com.example.save_your_time;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.concurrent.TimeUnit;

public class WithoutService extends Service {
    public WithoutService() {
    }
    long interval;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(){
        Intent intent1 = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent1,0);

        Notification notification = new NotificationCompat.Builder(this,"ChannelId1")
                .setContentTitle("Режим \"Без телефона\"")
                .setContentText("Вы не должны видеть это уведомление!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();
        //com.sololearn
        startForeground(1,notification);
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        interval = 15;
        startService(new Intent(this, MyService.class));
        WaitIntervalTask waitIntervalTask = new WaitIntervalTask();
        waitIntervalTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        Log.e("Func", "Destroy");
        stopService(new Intent(this, MyService.class));
        Intent intent1 = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent1,0);

        Notification notification = new NotificationCompat.Builder(this ,"ChannelId1")
                .setContentTitle("Режим \"Без телефона\"")
                .setContentText("Вы не должны видеть это уведомление!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();
        //com.sololearn
        synchronized(notification){
            // notify() is being called here when the thread and
            // synchronized block does not own the lock on the object.
            Log.e("Notifycation", "Should be");
            notification.notify();
        }
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class WaitIntervalTask extends AsyncTask <Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                TimeUnit.SECONDS.sleep(10);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("Interval", "Ended");
            stopSelf();
        }
    }
}