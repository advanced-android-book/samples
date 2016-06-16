package com.advanced_android.coordinatorlayout02;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * このアプリは
 * AndroidStudioのテンプレート、ScrollingActivityをもとにしています。
 *
 */
public class ScrollingActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        ButterKnife.bind(this);

        // ActionBarを設定
        setSupportActionBar(mToolbar);
    }

    /**
     * ActionBarItemをセットアップ
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    /**
     * ActionBarItemがタップされたときにコールバックされる
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // タップされたアイテムのID別に制御
        if (id == R.id.action_settings) {
            // 設定がタップされたときの制御を書く
            Toast.makeText(this, R.string.actionbar_settings_message,Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
