package com.github.advanced_android.recyclerviewsamples.divider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.github.advanced_android.recyclerviewsamples.DummyDataGenerator;
import com.github.advanced_android.recyclerviewsamples.R;
import com.github.advanced_android.recyclerviewsamples.simple.SimpleStringAdapter;

public class DividerRecyclerViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SimpleStringAdapter simpleStringAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recycler_view);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.simple_recycler_view);

        // RecyclerView自体の大きさが変わらないことが分かっている時、
        // このオプションを付けておくと、パフォーマンスが改善されます
        recyclerView.setHasFixedSize(true);

        // Adapterを設定しま
        // 最初のリスト表示で作ったものを利用
        simpleStringAdapter = new SimpleStringAdapter(DummyDataGenerator.generateStringListData());
        recyclerView.setAdapter(simpleStringAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(this));
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, DividerRecyclerViewActivity.class);
    }

}
