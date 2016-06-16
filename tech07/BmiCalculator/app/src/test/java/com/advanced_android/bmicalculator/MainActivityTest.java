package com.advanced_android.bmicalculator;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by shoma2da on 2015/12/24.
 */
public class MainActivityTest {

    @Test
    public void initViewsを呼ぶとボタンのListenerが設定される() {
        //モックオブジェクトの用意
        EditText weightText = mock(EditText.class);
        EditText heightText = mock(EditText.class);
        TextView resultText = mock(TextView.class);
        Button resultButton = mock(Button.class);
        View.OnClickListener buttonListener = mock(View.OnClickListener.class);

        //モックオブジェクトを返却するように設定
        MainActivity activity = spy(new MainActivity());
        when(activity.findViewById(R.id.text_weight)).thenReturn(weightText);
        when(activity.findViewById(R.id.text_height)).thenReturn(heightText);
        when(activity.findViewById(R.id.text_result)).thenReturn(resultText);
        when(activity.findViewById(R.id.button_calculate)).thenReturn(resultButton);
        when(activity.createButtonListener(weightText, heightText, resultText)).thenReturn(buttonListener);

        //テストと検証
        activity.initViews();
        verify(activity, times(1)).createButtonListener(weightText, heightText, resultText);
        verify(resultButton, times(1)).setOnClickListener(buttonListener);
    }

    @Test
    public void ボタンに設定するListenerで各種の処理を実行する() {
        //モックの用意
        TextView heightView = mock(TextView.class);
        TextView weightView = mock(TextView.class);
        TextView resultView = mock(TextView.class);
        BmiValue bmiValue = mock(BmiValue.class);

        MainActivity activity = spy(new MainActivity());
        doReturn(bmiValue).when(activity).calculateBmiValue(weightView, heightView);
        doNothing().when(activity).showCalcResult(resultView, bmiValue);
        doNothing().when(activity).startResultSaveService(bmiValue);
        doNothing().when(activity).prepareReceiveResultSaveServiceAction();

        View.OnClickListener buttonListener = activity.createButtonListener(weightView, heightView, resultView);
        buttonListener.onClick(mock(View.class));
        verify(activity, times(1)).calculateBmiValue(weightView, heightView);
        verify(activity, times(1)).showCalcResult(resultView, bmiValue);
        verify(activity, times(1)).startResultSaveService(bmiValue);
        verify(activity, times(1)).prepareReceiveResultSaveServiceAction();
    }

    @Test
    public void ボタンのリスナーで各種の処理が実行される() {
        //モックオブジェクトの用意
        EditText weightText = mock(EditText.class);
        EditText heightText = mock(EditText.class);
        TextView resultText = mock(TextView.class);
        BmiValue result = mock(BmiValue.class);

        //モックオブジェクトを返却するように設定
        MainActivity activity = spy(new MainActivity());
        doReturn(result).when(activity).calculateBmiValue(weightText, heightText);
        doNothing().when(activity).showCalcResult(resultText, result);
        doNothing().when(activity).startResultSaveService(result);
        doNothing().when(activity).prepareReceiveResultSaveServiceAction();

        //テストと検証
        View.OnClickListener buttonListener = activity.createButtonListener(weightText, heightText, resultText);
        buttonListener.onClick(mock(View.class));
        verify(activity, times(1)).calculateBmiValue(weightText, heightText);
        verify(activity, times(1)).showCalcResult(resultText, result);
        verify(activity, times(1)).startResultSaveService(result);
        verify(activity, times(1)).prepareReceiveResultSaveServiceAction();
    }

    @Test
    public void Receiverで正しく値が渡されればボタンに対する変更が行われる() {
        Button button = mock(Button.class);
        Intent intent = mock(Intent.class);
        when(intent.hasExtra(SaveBmiService.PARAM_RESULT)).thenReturn(true);
        when(intent.getBooleanExtra(SaveBmiService.PARAM_RESULT, false)).thenReturn(true);

        MainActivity.BmiSaveResultReceiver receiver = new MainActivity.BmiSaveResultReceiver(button);
        receiver.onReceive(mock(Context.class), intent);

        verify(button, times(1)).setText((CharSequence) any());
        verify(button, times(1)).setEnabled(true);
    }

}
