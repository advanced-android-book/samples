package com.advanced_android.wordoftoday1;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static com.advanced_android.wordoftoday1.WordsOfTodayContract.*;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "WordsOfToday";
    private static final String[] PROJECTIONS = new String[] {
            WordsOfTodayColumns._ID,
            WordsOfTodayColumns.NAME,
            WordsOfTodayColumns.WORDS,
            WordsOfTodayColumns.DATE,
    };
    private ContentResolver mContentResolver;
    private ContentObserver mObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContentResolver = getContentResolver();

        dump();
        mObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                Log.d(TAG, "Provider Changed");
                dump();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        // 第2引数がfalseの場合は、第1引数のURIとマッチした場合のみContentObserver.onChange()をコール。
        // trueの場合は、URIと部分マッチ。
        //mContentResolver.registerContentObserver(WordsOfTodayProvider.CONTENT_URI, false, mObserver);
        mContentResolver.registerContentObserver(CONTENT_URI, true, mObserver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        mContentResolver.unregisterContentObserver(mObserver);
    }

    private void dump() {
        Cursor c = mContentResolver.query(CONTENT_URI, PROJECTIONS, null, null, null);
        if (c.moveToFirst()) {
            int wordsCol = c.getColumnIndexOrThrow(WordsOfTodayColumns.WORDS);
            do {
                Log.d(TAG, "words=" + c.getString(wordsCol));
            } while(c.moveToNext());
        }
    }
}
