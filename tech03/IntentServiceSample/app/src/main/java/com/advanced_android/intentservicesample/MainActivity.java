package com.advanced_android.intentservicesample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    private LocalBroadcastManager mLocalBroadcastManager;
    private IntentFilter mIntentFilter;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (FibService.ACTION_CALC_DONE.equals(action)) {
                int result = intent.getIntExtra(FibService.KEY_CALC_RESULT, -1);
                long msec = intent.getLongExtra(FibService.KEY_CALC_MILLISECONDS, -2);
                // 結果を表示
                mTextView.setText("fib(" + FibService.N  + ") = " + result + " (" + msec + ")ミリ秒" );
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        mIntentFilter = new IntentFilter(FibService.ACTION_CALC_DONE);
        Intent serviceIntent = new Intent(FibService.ACTION_CALC);
        serviceIntent.setClass(getApplicationContext(), FibService.class);
        startService(serviceIntent);

        mTextView = (TextView) findViewById(R.id.text);
        mTextView.setText("fib(" + FibService.N + ") 計算中...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Local BroadcastReceiverを受け取るようにを登録
        mLocalBroadcastManager.registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 登録していたLocal BroadcastReceiverを解除
        mLocalBroadcastManager.unregisterReceiver(mReceiver);
    }
}
