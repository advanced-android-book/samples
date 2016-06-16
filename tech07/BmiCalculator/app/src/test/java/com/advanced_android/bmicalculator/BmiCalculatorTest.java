package com.advanced_android.bmicalculator;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by shoma2da on 2015/12/23.
 */
public class BmiCalculatorTest {

    @Spy
    private BmiCalculator calculator = new BmiCalculator();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = RuntimeException.class)
    public void 身長に負の値を渡すと例外が発生する() {
        calculator.calculate(-1f, 60.0f);
    }

    @Test(expected = RuntimeException.class)
    public void 体重に負の値を渡すと例外が発生する() {
        calculator.calculate(170.0f, -1f);
    }

    @Test
    public void シンプルな値を渡してBMIが計算される() {
        BmiValue result = calculator.calculate(1, 1);
        assertNotNull(result);
        verify(calculator, times(1)).createValueObj(1f);
    }

    @Test
    public void 実際の体重身長を渡してBMIが計算される() {
        BmiValue result = calculator.calculate(1.70f, 60f);
        assertNotNull(result);
        verify(calculator, times(1)).createValueObj(20.761246f);
    }

}
