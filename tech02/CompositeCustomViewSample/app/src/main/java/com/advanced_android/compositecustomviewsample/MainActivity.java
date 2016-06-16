package com.advanced_android.compositecustomviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MyCustomView indicator = (MyCustomView) findViewById(R.id.indicator);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected = indicator.getSelected();
                if (selected == 2) {
                    selected = 0;
                } else {
                    selected++;
                }
                Log.d("MainActivity", "selected=" + selected);
                indicator.setSelected(selected);
            }
        });
    }
}
