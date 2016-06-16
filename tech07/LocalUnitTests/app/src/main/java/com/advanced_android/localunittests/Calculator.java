package com.advanced_android.localunittests;

/**
 * Created by shoma2da on 2015/12/21.
 */
public class Calculator {
    public int evaluate(String expression) {
        int sum = 0;
        for (String summand: expression.split("\\+"))
            sum += Integer.valueOf(summand);
        return sum;
    }
}

