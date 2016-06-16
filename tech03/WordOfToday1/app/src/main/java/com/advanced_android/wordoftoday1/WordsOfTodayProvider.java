package com.advanced_android.wordoftoday1;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static com.advanced_android.wordoftoday1.WordsOfTodayContract.AUTHORITY;
import static com.advanced_android.wordoftoday1.WordsOfTodayContract.MIME_TYPE_DIR;
import static com.advanced_android.wordoftoday1.WordsOfTodayContract.MIME_TYPE_ITEM;
import static com.advanced_android.wordoftoday1.WordsOfTodayContract.TABLE_NAME;
import static com.advanced_android.wordoftoday1.WordsOfTodayContract.WordsOfTodayColumns;

/**
 * オンメモリで動作するContentProvider
 */
public class WordsOfTodayProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher;

    private static final SparseArray<WordsOfToday> sList = new SparseArray<>();
    private static final int ROW_DIR = 1;
    private static final int ROW_ITEM = 2;

    private static int sLastId = 0;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, ROW_DIR);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", ROW_ITEM);

        sList.append(sLastId, new WordsOfToday(sLastId, "Taiki", "今日はいい天気", 20151001)); sLastId++;
        sList.append(sLastId, new WordsOfToday(sLastId, "Osamu", "アプリの不具合修正", 20151001)); sLastId++;
        sList.append(sLastId, new WordsOfToday(sLastId, "Osamu", "今日もアプリの不具合修正", 20151002)); sLastId++;
        sList.append(sLastId, new WordsOfToday(sLastId, "Taiki", "ジムで頑張った", 20151002)); sLastId++;
        sList.append(sLastId, new WordsOfToday(sLastId, "Ken", "髪を短く切った", 20151002)); sLastId++;
        sList.append(sLastId, new WordsOfToday(sLastId, "Taiki", "今日のランチは美味しかった", 20151003)); sLastId++;
        sList.append(sLastId, new WordsOfToday(sLastId, "Taiki", "今朝は4時30分起き", 20151004));
    }


    public WordsOfTodayProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case ROW_ITEM:
                int id =  (int) ContentUris.parseId(uri);
                synchronized (sList) {
                    if (sList.indexOfKey(id) >= 0) {
                        sList.remove(id);
                        // 変更通知
                        getContext().getContentResolver().notifyChange(uri, null);
                        count = 1;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("引数のURIが間違っています");
        }
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ROW_DIR:
                return MIME_TYPE_DIR;
            case ROW_ITEM:
                return MIME_TYPE_ITEM;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri resultUri = null;
        switch (sUriMatcher.match(uri)) {
            case ROW_DIR:
                WordsOfToday obj = new WordsOfToday();
                if (values.containsKey(WordsOfTodayColumns.NAME)) {
                    obj.name = values.getAsString(WordsOfTodayColumns.NAME);
                }
                if (values.containsKey(WordsOfTodayColumns.WORDS)) {
                    obj.words = values.getAsString(WordsOfTodayColumns.WORDS);
                }
                if (values.containsKey(WordsOfTodayColumns.DATE)) {
                    obj.date = values.getAsInteger(WordsOfTodayColumns.DATE);
                }
                synchronized (sList) {
                    sLastId++;
                    obj._id = sLastId;
                    sList.append(sLastId, obj);
                    resultUri = ContentUris.withAppendedId(uri, sLastId);
                    Log.d("WordsOfToday", "WordsOfTodayProvider insert " + obj);
                    // 変更通知
                    getContext().getContentResolver().notifyChange(resultUri, null);
                }
                break;
            default:
                throw new IllegalArgumentException("引数のURIが間違っています");
        }
        return resultUri;
    }

    @Override
    public boolean onCreate() {
        Log.d("WordsOfToday", "WordsOfTodayProvider onCreate");
        // SQLiteデータベースを利用する場合は、SQLiteOpenHelperをここで作成。
        // 今回はオンメモリなので特に何もしない。
        // 問題なく処理が完了したということで、単にtrueを返す。
        return true;
    }

    /**
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d("WordsOfToday", "query uri=" + uri.toString());
        // _IDは常に返す
        HashSet<String> set = new HashSet<String>(Arrays.asList(projection));
        //set.add(WordsOfTodayColumns._ID);
        Log.d("WordsOfToday", "query projection=" + set.toString());
        String[] columns = set.toArray(new String[set.size()]);
        MatrixCursor cursor = new MatrixCursor(columns);
        switch (sUriMatcher.match(uri)) {
            case ROW_DIR:
                synchronized (sList) {
                    String name = null;
                    if (selection != null) {
                        // 実装簡略化のため、nameカラムのみ対応
                        // name=Taiki のような形
                        Log.d(MainActivity.TAG, "query where " + selection );
                        name = selection.split("=")[1];
                    }
                    int size = sList.size();
                    for (int i = 0; i < size; i++) {
                        WordsOfToday obj = sList.valueAt(i);
                        // where句が指定された場合はチェック
                        if (name == null || TextUtils.equals(obj.name, name)) {
                            Object[] row = getRow(obj._id, columns);
                            cursor.addRow(row);
                        }
                    }
                }
                return cursor;
            case ROW_ITEM:
                synchronized (sList) {
                    long id =  ContentUris.parseId(uri);
                    Object[] row = getRow(id, columns);
                    cursor.addRow(row);
                }
                break;
            default:
                ;
        }
        return cursor;
    }

    /**
     * projectionで指定されたカラム順にデータを配列に入れる
     * @param id TodayOfWordのID
     * @param columns 取得したカラム
     * @return
     */
    private Object[] getRow(long id, String[] columns) {
        ArrayList<Object> values = new ArrayList<Object>();
        WordsOfToday row = sList.get((int)id);
        for(String column : columns) {
            if (column.equals(WordsOfTodayColumns._ID)) {
                Log.d("WordsOfToday", "getRow _id=" + row._id);
                values.add(row._id);
                continue;
            }
            if(column.equals(WordsOfTodayColumns.NAME)) {
                Log.d("WordsOfToday", "getRow name=" + row.name);
                values.add(row.name);
                continue;
            }
            if(column.equals(WordsOfTodayColumns.WORDS)) {
                Log.d("WordsOfToday", "getRow words=" + row.words);
                values.add(row.words);
                continue;
            }
            if(column.equals(WordsOfTodayColumns.DATE)) {
                Log.d("WordsOfToday", "getRow date=" + row.date);
                values.add(row.date);
                continue;
            }
        }
        return values.toArray(new Object[values.size()]);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case ROW_ITEM:
                int id = (int) ContentUris.parseId(uri);
                synchronized (sList) {
                    WordsOfToday row = sList.get(id);
                    if (row != null) {
                        if (values.containsKey(WordsOfTodayColumns.NAME)) {
                            row.name = values.getAsString(WordsOfTodayColumns.NAME);
                        }
                        if (values.containsKey(WordsOfTodayColumns.WORDS)) {
                            row.words = values.getAsString(WordsOfTodayColumns.WORDS);
                        }
                        if (values.containsKey(WordsOfTodayColumns.NAME)) {
                            row.date = values.getAsInteger(WordsOfTodayColumns.DATE);
                        }
                        // 変更通知
                        getContext().getContentResolver().notifyChange(uri, null);
                        count = 1;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("引数のURIが間違っています");
        }
        return count;
    }
}
