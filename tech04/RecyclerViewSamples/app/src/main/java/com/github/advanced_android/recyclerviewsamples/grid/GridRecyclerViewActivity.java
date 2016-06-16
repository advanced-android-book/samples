package com.github.advanced_android.recyclerviewsamples.grid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.advanced_android.recyclerviewsamples.DummyDataGenerator;
import com.github.advanced_android.recyclerviewsamples.R;

public class GridRecyclerViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

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
        // 最初のリスト表示で作ったものを利用
        final RichAdapter adapter = new RichAdapter(DummyDataGenerator.generateStringListData());
        recyclerView.setAdapter(adapter);

        // 列の数を3つに設定したGridLayoutManagerのインスタンスを作成、セット
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        // SpanSizeLookupで位置毎に専有する幅を決定する
        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position) == RichAdapter.HEADER_VIEW_TYPE) {
                    // ヘッダーは3列専有して表示する(表示されるのは1列)
                    return gridLayoutManager.getSpanCount();
                }
                // 他は1列使う(表示されるのは3列)
                return 1;
            }
        };
        gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, GridRecyclerViewActivity.class);
    }

}
