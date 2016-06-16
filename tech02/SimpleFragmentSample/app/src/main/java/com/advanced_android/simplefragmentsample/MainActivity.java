package com.advanced_android.simplefragmentsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MyFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onFragmentInteraction() {
        Toast.makeText(getApplicationContext(), "ボタンが押されました", Toast.LENGTH_SHORT).show();
    }
}
