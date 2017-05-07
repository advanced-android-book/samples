package com.advanced_android.fragmenttransactionsample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    private static final String KEY_NUMBER = "KEY_NUMBER";
    private int mNumber = 0;
    private FragmentManager.OnBackStackChangedListener mListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            FragmentManager fragmentManager = getSupportFragmentManager();
            int count = 0;
            List<Fragment> fragments = fragmentManager.getFragments();
            // nullチェックを入れる #8
            if (fragments != null) {
				for (Fragment f : fragments) {
					if (f != null) {
						count++;
					}
				}
			}
            mNumber = count;
            Log.d("MainActivity", "onBackStackChanged mNumber=" + mNumber);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container, MyFragment.getInstance(mNumber))
                        .addToBackStack(null)
                        .commit();
            }
        });
        findViewById(R.id.remove_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNumber == 0) {
                    return;
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(mListener);
        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        Log.d("MainActivity", "onCreate fragment=" + fragment + ", mNumber=" + mNumber);
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, MyFragment.getInstance(mNumber), FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.removeOnBackStackChangedListener(mListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_NUMBER, mNumber);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mNumber = savedInstanceState.getInt(KEY_NUMBER);
    }
}
