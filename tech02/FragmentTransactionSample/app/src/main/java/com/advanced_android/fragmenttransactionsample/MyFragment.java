package com.advanced_android.fragmenttransactionsample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MyFragment extends Fragment {

    private static final String ARG_NO = "ARG_NO";

    public MyFragment() {
    }

    public static MyFragment getInstance(int no) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NO, no);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int no = getArguments().getInt(ARG_NO, 0);
        String text = "" + no + "番目のFragment";
        Log.d("MyFragment", "onCreate " + text);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = (TextView) view.findViewById(R.id.text);
        int no = getArguments().getInt(ARG_NO, 0);
        String text = "" + no + "番目のFragment";
        Log.d("MyFragment", "onViewCreated " + text);
        textView.setText(text);
    }
}
