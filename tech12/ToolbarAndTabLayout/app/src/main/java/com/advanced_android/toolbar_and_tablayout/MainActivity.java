package com.advanced_android.toolbar_and_tablayout;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.tabs)
    TabLayout mTabLayout;

    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // ToolbarをActionBarとして利用
        setSupportActionBar(mToolbar);

        // TabLayoutとViewPagerを連携
        final MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // ページがスクロールされたとき
            }

            @Override
            public void onPageSelected(int position) {
                // ページが選択されたとき
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //　ページのスクロール状態
            }
        });

        mTabLayout.setupWithViewPager(mViewPager); // TabLayoutとViewPagerを紐付ける

        //以下の設定でもTabLayoutとViewPagerを紐付けることができます
        //mTabLayout.setTabsFromPagerAdapter(adapter);
        //mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        //mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        // タブレイアウトにタブを設定(ViewPagerを利用しない場合)
        // mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_label1));
        // mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_label2));
        // mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_label3));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //　ActionBarのメニューアイテムを読み込みます
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // ActionBarのメニューアイテムが選択されたら呼ばれます
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(MainActivity.this, "item [" + item + "] selected", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }
}
