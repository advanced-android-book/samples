package com.github.advanced_android.recyclerviewsamples.divider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private final int dividerHeight;
    private Drawable divider;

    public DividerItemDecoration(Context context) {
        // デフォルトのListViewの区切り線のDrawableを取得 (区切り線をカスタマイズしたい場合はここでDrawableを取得してください)
        final TypedArray a = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
        divider = a.getDrawable(0);

        // 表示のたびに高さを取得しなくて済むようにここで取得しておく
        dividerHeight = divider.getIntrinsicHeight();
        a.recycle();
    }

//    Viewのアイテムより上に描画したい場合はこちらのメソッドを使う
//    @Override
//    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        super.onDrawOver(c, parent, state);
//    }


    // Viewのアイテムより下に描画したい場合はこちらのメソッドを使う
    // ここではRecyclerViewのアイテムごとに下に線を描画する
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        // 右左のpaddingから線のrightとleftを設定
        final int lineLeft = parent.getPaddingLeft();
        final int lineRight = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            // アニメーションなどの時にちゃんとずらすため
            int childTransitionY = Math.round(ViewCompat.getTranslationY(child));
            final int top = child.getBottom() + params.bottomMargin + childTransitionY;
            final int bottom = top + dividerHeight;

            // Viewの下に線を描画
            divider.setBounds(lineLeft, top, lineRight, bottom);
            divider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // Viewの下に線を入れるので、下にOffsetを入れる
        outRect.set(0, 0, 0, dividerHeight);
    }
}