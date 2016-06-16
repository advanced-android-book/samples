package com.advanced_android.compositecustomviewsample;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyCustomView extends LinearLayout {
    private ImageView mStar1;
    private ImageView mStar2;
    private ImageView mStar3;
    private int mSelected = 0;

    public MyCustomView(Context context) {
        super(context);
        initializeViews(context, null);
    }


    public MyCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public MyCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyCustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context, attrs);
    }

    /**
     * レイアウトを初期化する
     *
     * @param context
     */
    private void initializeViews(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.three_stars_indicator, this);
        if (attrs != null) {
            //attrs.xmlに定義したスタイルを取得
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyCustomView);
            mSelected = a.getInteger(0, 0);
            a.recycle(); // 利用が終わったら、recycle()を呼ぶ
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mStar1 = (ImageView) findViewById(R.id.star1);
        mStar2 = (ImageView) findViewById(R.id.star2);
        mStar3 = (ImageView) findViewById(R.id.star3);
        // 初回だけはxmlからの指定を反映させるため第2引数のforceをtrueとする
        setSelected(mSelected, true);
    }

    /**
     * 指定された番号で選択する
     *
     * @param select 指定する番号 (0が一番左)
     */
    public void setSelected(int select) {
        setSelected(select, false);
    }

    private void setSelected(int select, boolean force) {
        if (force || mSelected != select) {
            if (2 > mSelected && mSelected < 0) {
                return;
            }
            mSelected = select;
            if (mSelected == 0) {
                mStar1.setImageResource(R.drawable.star);
                mStar2.setImageResource(R.drawable.star_empty);
                mStar3.setImageResource(R.drawable.star_empty);
            } else if (mSelected == 1) {
                mStar1.setImageResource(R.drawable.star_empty);
                mStar2.setImageResource(R.drawable.star);
                mStar3.setImageResource(R.drawable.star_empty);
            } else if (mSelected == 2) {
                mStar1.setImageResource(R.drawable.star_empty);
                mStar2.setImageResource(R.drawable.star_empty);
                mStar3.setImageResource(R.drawable.star);
            }
        }
    }

    public int getSelected() {
        return mSelected;
    }
}
