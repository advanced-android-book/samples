package com.advanced_android.leakcanarydemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LeakActivity extends AppCompatActivity {

    static View.OnClickListener sListener = new View.OnClickListener() {

        private Button prevClickedView;

        @Override
        public void onClick(View v) {
            String text = "ボタンが押されました。前回は(" + (prevClickedView == null ? "なし" : prevClickedView.getText()) + ")を押しました";
            Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
            prevClickedView = (Button)v;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak);

        findViewById(R.id.button1).setOnClickListener(sListener);
        findViewById(R.id.button2).setOnClickListener(sListener);
    }

}
