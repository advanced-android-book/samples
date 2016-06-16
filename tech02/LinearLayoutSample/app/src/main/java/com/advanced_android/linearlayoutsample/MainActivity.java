package com.advanced_android.linearlayoutsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button:
                startActivity(new Intent(getApplicationContext(), Activity1.class));
                break;
            case R.id.button2:
                startActivity(new Intent(getApplicationContext(), Activity2.class));
                break;
            case R.id.button3:
                startActivity(new Intent(getApplicationContext(), Activity3.class));
                break;
            default:
                break;
        }
    }
}
