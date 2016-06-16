package com.github.advanced_android.recyclerviewsamples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.advanced_android.recyclerviewsamples.cardview.CardViewRecyclerViewActivity;
import com.github.advanced_android.recyclerviewsamples.divider.DividerRecyclerViewActivity;
import com.github.advanced_android.recyclerviewsamples.grid.GridRecyclerViewActivity;
import com.github.advanced_android.recyclerviewsamples.manipulation.ManipulationActivity;
import com.github.advanced_android.recyclerviewsamples.simple.RecyclerViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
    }

    private void setupViews() {
        findViewById(R.id.simple_execute_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RecyclerViewActivity.createIntent(MainActivity.this);
                startActivity(intent);
            }
        });
        findViewById(R.id.divider_execute_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DividerRecyclerViewActivity.createIntent(MainActivity.this);
                startActivity(intent);
            }
        });
        findViewById(R.id.cardview_execute_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CardViewRecyclerViewActivity.createIntent(MainActivity.this);
                startActivity(intent);
            }
        });
        findViewById(R.id.grid_execute_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = GridRecyclerViewActivity.createIntent(MainActivity.this);
                startActivity(intent);
            }
        });
        findViewById(R.id.manipulation_execute_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ManipulationActivity.createIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }
}
