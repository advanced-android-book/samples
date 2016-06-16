package com.advanced_android.uiautometertest;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class UiAutometorSampleTest {

    private static final String GOOGLE_PLAY_PACKAGE = "com.android.vending";
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen() {
        // UiDeviceを初期化し、ホームボタンを押す
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressHome();

        // ランチャーアプリの起動を待つ
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);
    }

    @Test
    public void GooglePlayを起動しテキスト入力したあとに検索画面に遷移できる() throws UiObjectNotFoundException {
        // Google Playを起動
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(GOOGLE_PLAY_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Google Playの起動を待つ
        mDevice.wait(Until.hasObject(By.pkg(GOOGLE_PLAY_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);

        // 検索ボックス画像をクリック
        UiObject searchBoxImage = mDevice.findObject(new UiSelector()
                .resourceId("com.android.vending:id/search_box_idle_text")
                .className("android.widget.ImageView"));
        searchBoxImage.click();

        // 検索するためのEditTextが表示される
        UiObject searchBox = mDevice.findObject(new UiSelector()
                .resourceId("com.android.vending:id/search_box_text_input")
                .className("android.widget.EditText"));
        assertTrue(searchBox.exists());
    }

}
