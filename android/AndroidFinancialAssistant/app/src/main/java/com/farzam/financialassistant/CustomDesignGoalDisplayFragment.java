package com.farzam.financialassistant;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;



/**
 * A simple {@link Fragment} subclass.
 */
public class CustomDesignGoalDisplayFragment extends Fragment {

    View view;
    public static String goalAmount;

    public CustomDesignGoalDisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_custom_design_goal_display, container, false);


        return view;
    }



}
