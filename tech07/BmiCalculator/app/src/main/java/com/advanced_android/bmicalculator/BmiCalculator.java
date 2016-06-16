package com.advanced_android.bmicalculator;

import android.support.annotation.VisibleForTesting;

/**
 * Created by shoma2da on 2015/12/23.
 */
public class BmiCalculator {

    /**
     * BMI値を計算して返却します
     * @param heightInMeter BMIの計算に使用する身長の値です
     * @param weightInKg BMIの計算に使用する身長の値です
     * @return BMI値です。
     */
    public BmiValue calculate(float heightInMeter, float weightInKg) {
        if (heightInMeter < 0 || weightInKg < 0) {
            throw new RuntimeException("身長・体重には正の実数を指定してください");
        }
        float bmiValue = weightInKg / (heightInMeter * heightInMeter);
        return createValueObj(bmiValue);
    }

    @VisibleForTesting
    BmiValue createValueObj(float bmiValue) {
        return new BmiValue(bmiValue);
    }

}
