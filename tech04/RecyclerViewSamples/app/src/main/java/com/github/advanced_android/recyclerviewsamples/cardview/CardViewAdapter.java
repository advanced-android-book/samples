package com.github.advanced_android.recyclerviewsamples.cardview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.advanced_android.recyclerviewsamples.R;

import java.util.List;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
    protected List<String> dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.simple_text_view);
        }
    }

    // 今回はコンストラクタでデータを渡す
    public CardViewAdapter(List<String> myDataset) {
        dataset = myDataset;
    }

    // 新しいViewHolderを作成する(LayoutManagerから呼び出される)
    @Override
    public CardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // 新しくViewを作る
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_row_card_view, parent, false);
        // データと関連がないレイアウトの調整はここで行う(ここで作ったレイアウトを使い回すため)
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Viewの中のデータを変更する (LayoutManagerから呼び出される)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // セットするデータの取得
        String text = dataset.get(position);
        // ViewHolderのViewの中のデータを変更する
        holder.textView.setText(text);

    }

    // データの数を返す (LayoutManagerから呼び出される)
    @Override
    public int getItemCount() {
        return dataset.size();
    }

}