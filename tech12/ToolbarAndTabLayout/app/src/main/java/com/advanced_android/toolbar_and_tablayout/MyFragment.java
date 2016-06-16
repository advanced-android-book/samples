package com.advanced_android.toolbar_and_tablayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyFragment extends Fragment {

    private static final String ARGS_KEY_INDEX = "index";

    @Bind(R.id.text)
    TextView mText;

    public MyFragment() {}

    // Fragmentを生成し、引数を設定
    public static MyFragment createInstance(int index){
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_KEY_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int index = getArguments().getInt(ARGS_KEY_INDEX);
        View view = inflater.inflate(R.layout.fragment_my, null);
        ButterKnife.bind(this,view);
        mText.setText("this tab is index:" + index);
        return view;
    }
}
