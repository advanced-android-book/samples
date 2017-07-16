package com.advanced_android.nestedfragmentsample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 親のFragment
 */
public class ParentFragment extends Fragment {

    public static final String TAG_CHILD = "TAG_CHILD";
    public static final String KEY_NUMBER = "KEY_NUMBER";
    private int mNumber = 0;

    private FragmentManager.OnBackStackChangedListener mListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            FragmentManager fragmentManager = getChildFragmentManager();
            int count = 0;
            for (Fragment f: fragmentManager.getFragments()) {
                if (f != null) {
                    count++;
                }
            }
            mNumber = count;
            Log.d("ParentFragment", "onBackStackChanged mNumber=" + mNumber);
        }
    };

    public ParentFragment() {
    }

    public static ParentFragment getInstance() {
        return new ParentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_parent, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager childFragmentManager = getChildFragmentManager();
                childFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, ChildFragment.getInstance(mNumber))
                        .addToBackStack(null)
                        .commit();
            }
        });
        view.findViewById(R.id.remove_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNumber == 0) {
                    return;
                }
                FragmentManager childFragmentManager = getChildFragmentManager();
                childFragmentManager.popBackStack();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("ParentFragment", "onActivityCreated");
        if (savedInstanceState != null) {
            mNumber = savedInstanceState.getInt(KEY_NUMBER, 0);
        }
        FragmentManager childFragmentManager = getChildFragmentManager();
        Fragment fragment = childFragmentManager.findFragmentByTag(TAG_CHILD);
        Log.d("ParentFragment", "onActivityCreated childFragment=" + fragment + ", mNumber=" + mNumber);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = childFragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, ChildFragment.getInstance(mNumber), TAG_CHILD);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        childFragmentManager.addOnBackStackChangedListener(mListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("ParentFragment", "onSaveInstanceState mNumber=" + mNumber);
        outState.putInt(KEY_NUMBER, mNumber);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ParentFragment", "onDestroy");
    }
}
