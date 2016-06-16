package com.advanced_android.bmicalculator;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.junit.Test;
import org.mockito.Matchers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by shoma2da on 2015/12/24.
 */
public class SaveBmiServiceTest {

    @Test
    public void staticなstartメソッドを呼ぶとstartServiceされる() {
        Context context = mock(Context.class);
        SaveBmiService.start(context, mock(BmiValue.class));
        verify(context, times(1)).startService((Intent) any());
    }

    @Test
    public void onHandleIntentにnullを渡したら何もしない() {
        SaveBmiService service = spy(new SaveBmiService());
        service.onHandleIntent(null);
        verify(service, never()).sendLocalBroadcast(anyBoolean());
        verify(service, never()).saveToRemoteServer((BmiValue) any());
    }

    @Test
    public void onHandleIntentにパラメータ無しのIntentを渡したら何もしない() {
        SaveBmiService service = spy(new SaveBmiService());
        service.onHandleIntent(mock(Intent.class));
        verify(service, never()).sendLocalBroadcast(anyBoolean());
        verify(service, never()).saveToRemoteServer((BmiValue)any());
    }

    @Test
    public void onHandleIntentにBmiValue型以外のデータが入ったIntentを渡したら何もしない() {
        Intent intent = mock(Intent.class);
        when(intent.getSerializableExtra(SaveBmiService.PARAM_KEY_BMI_VALUE)).thenReturn("hoge");

        SaveBmiService service = spy(new SaveBmiService());
        service.onHandleIntent(intent);
        verify(service, never()).sendLocalBroadcast(anyBoolean());
        verify(service, never()).saveToRemoteServer((BmiValue)any());
    }

    @Test
    public void onHandleIntentに正しくデータが入ったIntentを渡せばデータ保存とBroadcastが行われる() {
        //準備：SaveBmiServiceに渡すIntentを用意
        BmiValue bmiValue = mock(BmiValue.class);
        Intent intent = mock(Intent.class);
        when(intent.getSerializableExtra(SaveBmiService.PARAM_KEY_BMI_VALUE)).thenReturn(bmiValue);

        //準備：SaveBmiServiceの各メソッドは何もしないようにする
        SaveBmiService service = spy(new SaveBmiService());
        doReturn(false).when(service).saveToRemoteServer((BmiValue)any());
        doNothing().when(service).sendLocalBroadcast(anyBoolean());

        //テストとメソッド呼び出しの確認
        service.onHandleIntent(intent);
        verify(service, times(1)).sendLocalBroadcast(anyBoolean());
        verify(service, times(1)).saveToRemoteServer((BmiValue)any());
    }

    @Test
    public void Broadcastを飛ばせる() {
        LocalBroadcastManager manager = mock(LocalBroadcastManager.class);
        SaveBmiService service = new SaveBmiService();
        service.setLocalBroadcastManager(manager);
        service.sendLocalBroadcast(true);
        verify(manager, times(1)).sendBroadcast((Intent)any());
    }
}
