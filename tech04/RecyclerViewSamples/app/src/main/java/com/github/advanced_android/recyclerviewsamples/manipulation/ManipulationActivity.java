package com.github.advanced_android.recyclerviewsamples.manipulation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.github.advanced_android.recyclerviewsamples.DummyDataGenerator;
import com.github.advanced_android.recyclerviewsamples.R;

public class ManipulationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ManipulationSimpleStringAdapter simpleStringAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manipulation_recycler_view);
        setupRecyclerView();
        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleStringAdapter.addAtPosition(3, "新しいアイテム");
            }
        });
        findViewById(R.id.remove_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleStringAdapter.removeAtPosition(3);
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.simple_recycler_view);

        // RecyclerView自体の大きさが変わらないことが分かっている時、
        // このオプションを付けておくと、パフォーマンスが改善されます
        recyclerView.setHasFixedSize(true);

        // Adapterを設定します
        simpleStringAdapter = new ManipulationSimpleStringAdapter(DummyDataGenerator.generateStringListData());
        recyclerView.setAdapter(simpleStringAdapter);

        // ItemTouchHelperクラスを実装する
        // これにより、ドラッグアンドドロップやスワイプで削除などが行えるようになる。
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // ドラッグアンドドロップ時
                simpleStringAdapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // アイテムスワイプ時
                simpleStringAdapter.removeAtPosition(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, ManipulationActivity.class);
    }

}
