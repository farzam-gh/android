package com.farzam.financialassistant;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;


public class NewGoalFragment extends Fragment implements View.OnClickListener{

    View view;
    private BackToMain backToMain;
    private boolean validGoal=true;
    private String tableName,selectedDate;
    private int day,month,year;
    DatePickerDialog.OnDateSetListener date;
    EditText goalDate;
    Calendar myCalendar;



    public static interface BackToMain{
        public void displayMain();
        public void showDatePickerFragment();
        public int[] getSelectedDate();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       view= inflater.inflate(R.layout.fragment_new_goal, container, false);
        Button submit=(Button)view.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        return view;
    }
    @Override
    public void onViewCreated(View v,Bundle savedInstanceState) {
        goalDate = (EditText) view.findViewById(R.id.new_goal_deadline);

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        goalDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
                   private void updateLabel() {


                        String chosenDate=Integer.toString((myCalendar.get(Calendar.MONTH))+1)+
                                "/"+myCalendar.get(Calendar.DAY_OF_MONTH)+"/"+myCalendar.get(Calendar.YEAR);

                        goalDate.setText(chosenDate);
                       selectedDate=chosenDate.substring(0,chosenDate.length()-4);
                       selectedDate=selectedDate+chosenDate.substring(chosenDate.length()-2,chosenDate.length());
                }








    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.backToMain=(BackToMain)activity;
    }


    @Override
    public void onClick(View v){
        validGoal=true;
        String goalTitle=((EditText) view.findViewById(R.id.new_goal_title)).getText().toString();
        String goalValue=((EditText) view.findViewById(R.id.new_goal_amount)).getText().toString();
        ((EditText) view.findViewById(R.id.new_goal_deadline)).setText(goalDate.getText());
        //String goalPriority=((EditText) view.findViewById(R.id.new_goal_priority)).getText().toString();
        SQLiteOpenHelper goalOpenHelper=new GoalDatabaseHandler(getActivity());
        SQLiteDatabase db=goalOpenHelper.getWritableDatabase();
        ContentValues newGoal=new ContentValues();
        if(validString(goalTitle,true))
        newGoal.put("TITLE",tableName);
        if(validInteger(goalValue))
        newGoal.put("GOAL",goalValue);
        //if(validString(goalDeadline,false))
        newGoal.put("DEADLINE",selectedDate);

        newGoal.put("TOTAL",0);
        newGoal.put("PROGRESS","0");
        if(validGoal) {

            TransactionDatabaseHandler.createTable(tableName);
            TransactionDatabaseHandler tdh=new TransactionDatabaseHandler(getActivity());
            if(tdh.createNewTable(tableName)){
                db.insert("GOALs", null, newGoal);
                db.close();

            };



            backToMain.displayMain();
        }else{
            Toast.makeText(getActivity(), "Incorrect Value(s) entered, Try again", Toast.LENGTH_LONG).show();
        }

    }
    private boolean validString(String s,boolean b){

        if(s.length()<=0) {
            validGoal=false;
            return false;
        }else if (b){
         tableName=s.replace(" ","_");
        }
        return true;
    }
    private boolean validInteger(String s){
        try {
             int i=Integer.parseInt(s);
                return true;

        }
        catch (Exception ex){
            validGoal=false;
            return false;
        }
    }



}
