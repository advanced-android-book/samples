package com.advanced_android.wakefulbroadcastreceiversample;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 *
 */
public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Log.d("MyIntentService", "onHandleIntent START");

            // 30秒スリープ
            Thread.currentThread().sleep(30 * 1000);

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("test", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("KEY", "SAVED");
            editor.apply();
            Log.d("MyIntentService", "onHandleIntent SAVED");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Log.d("MyIntentService", "onHandleIntent Finally");
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }
}
