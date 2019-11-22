package com.nikita.lab.calculator;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;



public class BaseFragment extends Fragment {

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        if (BuildConfig.FLAVOR.equals("demo") || BuildConfig.FLAVOR.equals("demoDebug")) {
            Button zeroB = view.findViewById(R.id.zero_button);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(zeroB.getLayoutParams());
            params.columnSpec = GridLayout.spec(0, 2, 1);
            params.rowSpec = GridLayout.spec(4, 1, 0.4f);
            zeroB.setLayoutParams(params);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
