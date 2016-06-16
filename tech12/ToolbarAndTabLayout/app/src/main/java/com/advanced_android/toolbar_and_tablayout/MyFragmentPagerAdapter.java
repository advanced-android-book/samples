package com.advanced_android.toolbar_and_tablayout;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{

    private Context mContext;
    private static final int MAX_ITEM_COUNT = 3;

    public MyFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return MyFragmentFactory.getInstance().create(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String tabPrefix = mContext.getString(R.string.tab_name_prefix);
        return tabPrefix + (position + 1);
    }

    @Override
    public int getCount() {
        return MAX_ITEM_COUNT;
    }
}
