package com.advanced_android.networkcheckfragmentsample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * ネットワークの接続チェックを行うBroadcastReceiver
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MyReceiver", "onReceive");
        Intent i = new Intent(NetworkCheckFragment.ACTION_CHECK_INTERNET);
        i.putExtra(NetworkCheckFragment.KEY_CHECK_INTERNET,
                NetworkCheckFragment.isInternetConnected(context));
        // 接続変更を通知
        LocalBroadcastManager.getInstance(context).sendBroadcast(i);
    }
}
