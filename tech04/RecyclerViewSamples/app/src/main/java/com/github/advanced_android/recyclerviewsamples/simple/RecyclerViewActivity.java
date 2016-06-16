package com.github.advanced_android.recyclerviewsamples.simple;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.github.advanced_android.recyclerviewsamples.DummyDataGenerator;
import com.github.advanced_android.recyclerviewsamples.R;

public class RecyclerViewActivity extends AppCompatActivity {

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

        // Adapterを設定します
        simpleStringAdapter = new SimpleStringAdapter(DummyDataGenerator.generateStringListData());
        simpleStringAdapter.setOnItemViewClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // itemがクリックされたら呼ばれる
                Toast.makeText(v.getContext(), "Position:" + recyclerView.getChildAdapterPosition(v) + "がクリックされました", Toast.LENGTH_LONG).show();
            }
        });

        recyclerView.setAdapter(simpleStringAdapter);
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, RecyclerViewActivity.class);
    }

}
