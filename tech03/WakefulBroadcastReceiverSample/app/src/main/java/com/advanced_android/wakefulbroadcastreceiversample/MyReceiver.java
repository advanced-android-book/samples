package com.advanced_android.wakefulbroadcastreceiversample;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class MyReceiver extends WakefulBroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MyReceiver", "onReceive");
        Intent serviceIntent = new Intent(context, MyIntentService.class);
        startWakefulService(context, serviceIntent);
    }
}
