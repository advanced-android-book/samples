package com.advanced_android.bmicalculator;

import android.support.annotation.VisibleForTesting;

import java.io.Serializable;

/**
 * Created by shoma2da on 2015/12/23.
 */
public class BmiValue implements Serializable {

    private static final long serialVersionUID = -4325336659053219895L;

    @VisibleForTesting
    static final String THIN = "やせ";
    @VisibleForTesting
    static final String NORMAL = "標準";
    @VisibleForTesting
    static final String OBESITY = "肥満（１度）";
    @VisibleForTesting
    static final String VERY_OBESITY = "肥満（２度）";

    private final float mValue;

    public BmiValue(float value) {
        mValue = value;
    }

    /**
     * 小数点以下第２位までの浮動小数点値です。
     * @return
     */
    public float toFloat() {
        int rounded = Math.round(mValue * 100);
        return  rounded / 100f;
    }

    /**
     * BMIによる判定メッセージを返却します
     */
    public String getMessage() {
        if (mValue < 18.5f) {
            return THIN;
        } else if (18.5 <= mValue && mValue < 25) {
            return NORMAL;
        } else if (25 <= mValue && mValue < 30) {
            return OBESITY;
        } else {
            return VERY_OBESITY;
        }
    }

}
