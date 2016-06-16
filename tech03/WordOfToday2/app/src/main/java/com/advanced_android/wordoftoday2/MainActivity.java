package com.advanced_android.wordoftoday2;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.stetho.Stetho;

import static android.provider.BaseColumns._ID;
import static com.advanced_android.wordoftoday2.WordsOfTodayContract.WordsOfTodayColumns.DATE;
import static com.advanced_android.wordoftoday2.WordsOfTodayContract.WordsOfTodayColumns.NAME;
import static com.advanced_android.wordoftoday2.WordsOfTodayContract.WordsOfTodayColumns.WORDS;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 1;

    private static final String[] PROJECTIONS = new String[] {
            _ID, NAME, WORDS, DATE,
    };
    private CursorAdapter mAdapter;
    private ContentObserver mObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Context context = getApplicationContext();
            Stetho.initialize(
                    Stetho.newInitializerBuilder(context)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                            .build());
        }
        setContentView(R.layout.activity_main);

        mAdapter = new CursorAdapter(getApplicationContext(), null, false) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                Log.d(TAG, "newView");
                return View.inflate(context, R.layout.list_item, null);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME));
                String words = cursor.getString(cursor.getColumnIndexOrThrow(WORDS));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE));
                TextView nameText = (TextView) view.findViewById(R.id.nameText);
                TextView wordsText = (TextView) view.findViewById(R.id.wordsText);
                TextView dateText = (TextView) view.findViewById(R.id.dateText);
                date = date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6);
                Log.d(TAG, "bindView name=" + name + ", dateText=" + date);
                nameText.setText(name);
                wordsText.setText(words);
                dateText.setText(date);
            }
        };
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");
        return new CursorLoader(
                MainActivity.this, WordsOfTodayContract.CONTENT_URI,
                PROJECTIONS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished");
        // 変更を監視するUriを設定する
        data.setNotificationUri(getContentResolver(), WordsOfTodayContract.CONTENT_URI);
        mAdapter.swapCursor(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
