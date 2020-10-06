package com.medium.octaveapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class MyJobIntentService extends JobIntentService {
    //we cant keep normal services running in the background
    //better than intent service

    //on oreo onwards it uses job scheduler
    //it can get interrupted
    //but no persistence notification
    MediaPlayer player;
    private static final String TAG = "ExampleJobIntentService";
    static void enqueueWork(Context context, Intent work) { //starting the service
        enqueueWork(context, MyJobIntentService.class, 123, work);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        //first time service is started

        //wake lock is taken care of automatically
    }
    @Override
    //equivalent to on handle intent
    //sequential
    //auto background thread
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork");
        String input = intent.getStringExtra("inputExtra");
        for (int i = 0; i < 10; i++) {
            Log.d(TAG, input + " - " + i);
            if (isStopped()) return; //ran through onStopCurrentWork
            SystemClock.sleep(1000);
        }
    }
   /* private void playSong(String s){
        if (player == null) {
            player = MediaPlayer.create(this, uri);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }
        player.start();
    }*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
    @Override
    public boolean onStopCurrentWork() {
        Log.d(TAG, "onStopCurrentWork");
        //when the current job is stopped or interrupted
        return super.onStopCurrentWork();
        //return with true : start again with same intent : by default
    }
}
