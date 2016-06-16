package com.github.advanced_android.databindingsample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.github.advanced_android.databindingsample.databinding.ActivityMainBinding;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bindingオブジェクトを取得する
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // BindingオブジェクトにUserをセットする
        binding.setUser(new User("taro", 25));

        String date = (String) DateFormat.format("yyyy/MM/dd kk:mm:ss", Calendar.getInstance());
        binding.textTime.setText(date);//Viewにidが指定してあると、BindingオブジェクトからViewへの参照が取得できる
    }
}
