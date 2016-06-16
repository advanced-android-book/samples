package com.advanced_android.configchangedsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String mText;
    private EditText mEditText;
    private TextView mSavedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mSavedText = (TextView) findViewById(R.id.text);
        findViewById(R.id.btn).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mText != null) {
            mSavedText.setText(mText);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mText = mEditText.getText().toString();
        outState.putString("EDIT_TEXT" , mText);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mText = savedInstanceState.getString("EDIT_TEXT");
    }

    @Override
    public void onClick(View v) {
        mText = mEditText.getText().toString();
        mSavedText.setText(mText);
    }
}
