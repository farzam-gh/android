package com.farzam.financialassistant;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class GoalHeaderFragment extends Fragment {
    View view;
    public static String title;
    public static String amount;
    public static String date;
    public static String total;

    public GoalHeaderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_goal_header, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View v,Bundle savedInstanceState){
        TextView tvTitle=(TextView) view.findViewById(R.id.goalH);
        TextView tvAmount=(TextView) view.findViewById(R.id.goalA);
        TextView tvDate=(TextView) view.findViewById(R.id.goalD);
        TextView tvTotal=(TextView) view.findViewById(R.id.goalT);
        tvTitle.setText(title);
        double tempAmount=0.0;
        if (Integer.parseInt(amount) >= 1000000) {
            tempAmount = (Integer.parseInt(amount) / 1000000.0);
            if (Integer.parseInt(amount) % 1000000.0 == 0) {
                amount = Double.toString(tempAmount).
                        substring(0, Double.toString(tempAmount).length() - 2) + "M";
            } else {
                amount = tempAmount + "M";
            }
        } else if (Integer.parseInt(amount) >= 10000) {
            tempAmount = (Integer.parseInt(amount) / 1000.0);
            if (Integer.parseInt(amount) % 1000.0 == 0) {
                amount = Double.toString(tempAmount).
                        substring(0, Double.toString(tempAmount).length() - 2) + "K";
            } else {
                amount = tempAmount + "K";
            }
        }


            tvAmount.setText(amount);
        tvDate.setText(date);
        tvTotal.setText(total);
    }
    public static void setTexts(String t,String a,String d,String tot){
        title=t;
        amount=a;
        date=d;
        total=tot;

    }

}
