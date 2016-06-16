package com.advanced_android.wakefulbroadcastreceiversample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 前回の設定をクリア
        SharedPreferences sharedPreferences = getSharedPreferences("test", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        setContentView(R.layout.activity_main);
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(30 * 1000);
                    sendBroadcast(new Intent("com.advanced_android.wakefulbroadcastreceiversample.TEST_ACTION"));
                } catch (InterruptedException e) {
                }

            }
        })).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("test", Context.MODE_PRIVATE);
        String result = sharedPreferences.getString("KEY", null);
        TextView textView = (TextView) findViewById(R.id.text);
        if (result == null) {
            textView.setText("しばらく放置して、スリープ状態にしてください。(30秒未満でスリープするように設定しておいてください）");
        } else {
            textView.setText("MyIntentServiceで処理が完了");
        }
    }
}
