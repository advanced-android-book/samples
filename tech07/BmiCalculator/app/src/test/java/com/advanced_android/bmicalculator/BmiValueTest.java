package com.advanced_android.bmicalculator;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by shoma2da on 2015/12/23.
 */
public class BmiValueTest {

    @Test
    public void 生成時に渡したFloat値が小数点第２位までで切り上げて返却される() {
        BmiValue bmiValue = new BmiValue(20.00511f);
        Assert.assertEquals(20.01f, bmiValue.toFloat());
    }

    @Test
    public void 生成時に渡したFloat値が小数点第２位までで切り捨てて返却される() {
        BmiValue bmiValue = new BmiValue(20.00499f);
        Assert.assertEquals(20.00f, bmiValue.toFloat());
    }

    @Test
    public void やせ型判定される上限値() {
        BmiValue bmiValue = new BmiValue(18.499f);
        Assert.assertEquals(BmiValue.THIN, bmiValue.getMessage());
    }

    @Test
    public void 標準判定される下限値() {
        BmiValue bmiValue = new BmiValue(18.500f);
        Assert.assertEquals(BmiValue.NORMAL, bmiValue.getMessage());
    }

    @Test
    public void 標準判定される上限値() {
        BmiValue bmiValue = new BmiValue(24.999f);
        Assert.assertEquals(BmiValue.NORMAL, bmiValue.getMessage());
    }

    @Test
    public void 軽度肥満判定される下限値() {
        BmiValue bmiValue = new BmiValue(25.000f);
        Assert.assertEquals(BmiValue.OBESITY, bmiValue.getMessage());
    }

    @Test
    public void 軽度肥満判定される上限値() {
        BmiValue bmiValue = new BmiValue(29.999f);
        Assert.assertEquals(BmiValue.OBESITY, bmiValue.getMessage());
    }

    @Test
    public void 重度肥満判定される上限値() {
        BmiValue bmiValue = new BmiValue(30.000f);
        Assert.assertEquals(BmiValue.VERY_OBESITY, bmiValue.getMessage());
    }
}
