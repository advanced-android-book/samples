package com.advanced_android.nestedfragmentsample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChildFragment extends Fragment {

    public static final String ARG_NO = "ARG_NO";

    public ChildFragment() {
    }

    public static ChildFragment getInstance(int no) {
        ChildFragment fragment = new ChildFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NO, no);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_child, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = (TextView) view.findViewById(R.id.text);
        int no = getArguments().getInt(ARG_NO, 0);
        String text = "" + no + " 番目のChild Fragment";
        textView.setText(text);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ChildFragment", "onDestroy");
    }
}
