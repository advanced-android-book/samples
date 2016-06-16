package com.advanced_android.networkcheckfragmentsample;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class NetworkCheckFragment extends Fragment {

    public static final String TAG = NetworkCheckFragment.class.getSimpleName();
    public static final String ACTION_CHECK_INTERNET = "ACTION_CHECK_INTERNET";
    public static final String KEY_CHECK_INTERNET = "KEY_CHECK_INTERNET";

    private IntentFilter mIntentFilter;

    public NetworkCheckFragment() {
    }

    public static NetworkCheckFragment newInstance() {
        return new NetworkCheckFragment();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_CHECK_INTERNET.equals(action)) {
                // ネットワーク接続の変更に応じた共通処理を記述
                boolean isConnected = intent.getBooleanExtra(KEY_CHECK_INTERNET, false);
                if (isConnected) {
                    // インターネットの接続がある場合
                    Toast.makeText(context, "インターネット接続あり", Toast.LENGTH_SHORT).show();
                } else {
                    // インターネットの接続がない場合
                    Toast.makeText(context, "インターネット接続なし", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setRetainInstance(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (mIntentFilter == null) {
            mIntentFilter = new IntentFilter(ACTION_CHECK_INTERNET);
        }
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }

    public static boolean isInternetConnected(Context context) {
        return isWifiConnected(context) || isMobileConnected(context);
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (info != null && info.isConnected()) {
            return true;
        }

        return false;
    }

    public static boolean isMobileConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (info != null && info.isConnected()) {
            return true;
        }

        return false;
    }

}

