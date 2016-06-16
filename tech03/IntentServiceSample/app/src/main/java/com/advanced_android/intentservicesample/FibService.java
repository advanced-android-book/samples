package com.advanced_android.intentservicesample;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;


/**
 *  フィボナッチ数列を計算するIntentService
 *
 *  onHandleIntent内はワーカースレッド(メインスレッドではない）で実行されます
 */
public class FibService extends IntentService {

    // サービスのアクション
    static final String ACTION_CALC = "ACTION_CALC";
    // ブロードキャストのアクション
    static final String ACTION_CALC_DONE = "ACTION_CALC_DONE";
    // ブロードキャストで計算結果を受け渡しするためのキー名
    static final String KEY_CALC_RESULT = "KEY_CALC_RESULT";
    // ブロードキャストで計算にかかった秒数を受け渡しするためのキー名
    static final String KEY_CALC_MILLISECONDS = "KEY_CALC_MILLISECONDS";

    // フィボナッチ数列の計算
    static final int N = 40;

    public FibService() {
        super("FibService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CALC.equals(action)) {
                long start = System.nanoTime();
                int result = fib(N); // フィボナッチ数列を計算
                long end = System.nanoTime();
                Intent resultIntent = new Intent(ACTION_CALC_DONE);
                // 結果をIntentに付与
                resultIntent.putExtra(KEY_CALC_RESULT, result);
                resultIntent.putExtra(KEY_CALC_MILLISECONDS, (end - start) / 1000 / 1000);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(resultIntent);
            }
        }
    }

    /** フィボナッチ数列の計算 */
    private static int fib(int n) {
        return n <= 1 ? n : fib(n - 1) + fib(n - 2);
    }
}
