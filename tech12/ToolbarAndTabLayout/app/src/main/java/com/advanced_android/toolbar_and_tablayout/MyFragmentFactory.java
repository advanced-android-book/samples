package com.advanced_android.toolbar_and_tablayout;

public class MyFragmentFactory {

    private static MyFragmentFactory sInstance = new MyFragmentFactory();

    private MyFragmentFactory(){}

    public static synchronized MyFragmentFactory getInstance() {
        if(sInstance == null) {
            sInstance = new MyFragmentFactory();
        }
        return sInstance;
    }

    public MyFragment create(int index) {
        return MyFragment.createInstance(index);
    }
}
