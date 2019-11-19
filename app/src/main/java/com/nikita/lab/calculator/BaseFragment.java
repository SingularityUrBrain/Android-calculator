package com.nikita.lab.calculator;

import android.content.Context;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BaseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BaseFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;


    private final String PADDING = "\n\n\n\n\n";
    //StringBuilder buff = new StringBuilder(PADDING);
    private StringBuilder result = new StringBuilder("0");


    public BaseFragment() {
        // Required empty public constructor
    }


    public static BaseFragment newInstance(String param1) {
        BaseFragment fragment = new BaseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String uri);
    }
}
