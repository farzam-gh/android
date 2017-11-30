package com.farzam.financialassistant;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;



public class DatePickerFragment extends Fragment {

    private View view;
    private static int[]date=new int[3];
    private BackToNewGoal backToNewGoal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_date_picker, container, false);
        getDate();
        return view;
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.backToNewGoal=(BackToNewGoal)activity;
    }

    public static interface BackToNewGoal{
        public void hitBackStack(int[] args);
    }
    public void getDate(){


        DatePicker myDatePicker=(DatePicker)view.findViewById(R.id.datePicker);
        myDatePicker.init(2017,1,1,new DatePicker.OnDateChangedListener(){
            @Override
            public void onDateChanged(DatePicker v,int y,int m,int d){
                setDate(y,m,d);

                backToNewGoal.hitBackStack(getGoalDate());

            }
        });
    }
    public void setDate(int y,int m,int d){
        date[0]=y;
        date[1]=m+1;
        date[2]=d;
    }
    public int[] getGoalDate(){
        return date;
    }

}
