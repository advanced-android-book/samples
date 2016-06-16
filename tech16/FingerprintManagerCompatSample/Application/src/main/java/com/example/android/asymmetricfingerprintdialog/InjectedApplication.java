package com.example.android.asymmetricfingerprintdialog;

import android.app.Application;
import android.util.Log;

import dagger.ObjectGraph;

/**
 * サンプルのアプリケーションクラスです。DIライブラリ「Dagger」の初期化処理をしていますが割愛します。
 */
public class InjectedApplication extends Application {

    private static final String TAG = InjectedApplication.class.getSimpleName();

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        initObjectGraph(new FingerprintModule(this));
    }

    /**
     * DIライブラリ「Dagger」の初期化処理です。
     *
     * @param module for Dagger
     */
    public void initObjectGraph(Object module) {
        mObjectGraph = module != null ? ObjectGraph.create(module) : null;
    }

    public void inject(Object object) {
        if (mObjectGraph == null) {
            // This usually happens during tests.
            Log.i(TAG, "Object graph is not initialized.");
            return;
        }
        mObjectGraph.inject(object);
    }

}
