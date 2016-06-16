package com.github.advanced_android.recyclerviewsamples.grid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.advanced_android.recyclerviewsamples.R;

import java.util.List;

public class RichAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_VIEW_TYPE = 0;
    public static final int HEADER_VIEW_TYPE = 1;
    private List<String> dataset;

    @Override
    public int getItemViewType(int position) {
        // ■から始まっていたらヘッダーとして判定
        if (dataset.get(position).startsWith("■")) {
            return HEADER_VIEW_TYPE;
        }
        return ITEM_VIEW_TYPE;
    }

    // 今回はコンストラクタでデータを渡す
    public RichAdapter(List<String> myDataset) {
        dataset = myDataset;
    }

    // アイテム用のViewHolder
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        public ItemViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.simple_text_view);
        }
    }

    // ヘッダー用のViewHolder
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final TextView titleTextView;
        public final TextView detailTextView;

        public HeaderViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.title_text_view);
            detailTextView = (TextView) v.findViewById(R.id.detail_text_view);
        }
    }

    // 新しいViewHolderを作成する(LayoutManagerから呼び出される)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View view;
        // 新しくViewを作る
        switch (viewType) {
            // コンテンツ用のViewHolderの作成
            case ITEM_VIEW_TYPE: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.simple_row, parent, false);
                return new ItemViewHolder(view);
            }
            // ヘッダー用のViewHolderの作成
            case HEADER_VIEW_TYPE: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.header_row, parent, false);
                return new HeaderViewHolder(view);
            }
            default:
                throw new RuntimeException("予測されないViewTypeです");
        }
    }

    // Viewの中のデータを変更する (LayoutManagerから呼び出される)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // セットするデータの取得
        String text = dataset.get(position);
        // ViewHolderのViewの中のデータを変更する
        switch (holder.getItemViewType()) {
            case ITEM_VIEW_TYPE: {
                // アイテム用にそのまま文字を設定
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.textView.setText(text);
                break;
            }
            case HEADER_VIEW_TYPE: {
                // ヘッダーならタイトル用の文字列を設定する
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                headerViewHolder.titleTextView.setText("タイトル:" + text);
                headerViewHolder.detailTextView.setText(text + "の一覧です");
                break;
            }
        }
    }

    // データの数を返す (LayoutManagerから呼び出される)
    @Override
    public int getItemCount() {
        return dataset.size();
    }
}