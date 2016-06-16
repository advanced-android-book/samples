package com.github.advanced_android.recyclerviewsamples.manipulation;

import com.github.advanced_android.recyclerviewsamples.cardview.CardViewAdapter;

import java.util.List;

/*
    CardViewAdapterとやることがほぼ変わらないので、継承して必要な部分だけ追加する
*/
public class ManipulationSimpleStringAdapter extends CardViewAdapter {
    public ManipulationSimpleStringAdapter(List<String> myDataset) {
        super(myDataset);
    }

    // データを挿入する
    public void addAtPosition(int position, String text) {
        if (position > dataset.size()) {
            // 現在存在するアイテムの個数より多い位置を指定しているので、最後の位置に追加
            position = dataset.size();
        }
        // データを追加する
        dataset.add(position, text);
        // 挿入したことをAdapterに教える
        notifyItemInserted(position);
    }

    // データを削除する
    public void removeAtPosition(int position) {
        if (position < dataset.size()) {
            // データを削除する
            dataset.remove(position);
            // 削除したことをAdapterに教える
            notifyItemRemoved(position);
        }
    }


    // データを移動する
    public void move(int fromPosition, int toPosition) {
        final String text = dataset.get(fromPosition);
        dataset.remove(fromPosition);
        dataset.add(toPosition, text);
        notifyItemMoved(fromPosition, toPosition);
    }
}
