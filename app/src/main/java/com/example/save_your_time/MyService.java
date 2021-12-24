package com.example.save_your_time;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {

    BroadcastReceiver mReceiver;
    ExecutorService es;
    FailTimerTask failTimerTask;
    boolean fail = false;

    @Override
    public void onCreate() {
        super.onCreate();
        // REGISTER RECEIVER THAT HANDLES SCREEN ON AND SCREEN OFF LOGIC
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        es = Executors.newFixedThreadPool(1);

        Intent intent1 = new Intent(this,BlockSettingsActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent1,0);

        Notification notification = new NotificationCompat.Builder(this,"ChannelId1")
                .setContentTitle("Режим \"Без телефона\"")
                .setContentText("Вы обязались не использовать телефон и не должны видеть это уведомление!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();
        //com.sololearn
        startForeground(1,notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean screenOn = intent.getBooleanExtra("screen_state", false);
        if (!screenOn) {
            failTimerTask = new FailTimerTask();
            failTimerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            Log.e("SCREEN", "ON " + startId);
        } else {
            failTimerTask.cancel(false);
            Log.e("SCREEN", "OFF " + startId);
        }
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("SCREEN", "DESTROY");
        unregisterReceiver(mReceiver);
        stopService(new Intent(this, WithoutService.class));
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class FailTimerTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onCancelled(){
            Log.e("Fail", "Cancelled");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("Fail", "Started");
            try{
                TimeUnit.SECONDS.sleep(5);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("Fail", "Ended");
            fail = true;
            stopSelf();
        }
    }
}