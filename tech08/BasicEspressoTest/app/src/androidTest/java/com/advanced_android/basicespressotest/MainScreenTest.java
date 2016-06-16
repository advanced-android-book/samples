package com.advanced_android.basicespressotest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainScreenTest {

    //fabをクリックしたあとに表示されるメッセージ
    final String MESSAGE = MainActivity.DONE_MESSAGE;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void 画面にFAB関連の文字列が表示されていない() {
        onView(withText(MESSAGE)).check(doesNotExist()); //メッセージが表示されていない
    }

    @Test
    public void FABをクリックするとfab関連の文言が表示される() {
        onView(withId(R.id.fab)).perform(click()); //FABをクリック
        onView(withText(MESSAGE)).check(matches(isDisplayed())); //メッセージが表示されている
    }

}
