package com.farzam.financialassistant;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class NewTransactionFragment extends Fragment implements View.OnClickListener {
    private View view;
    private boolean validTransaction;
    private String tableName=TransactionDatabaseHandler.DB_NAME;
    private DisplaytransactionDetails displaytransactionDetails;
    EditText transactionDate;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    String selectedDate;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_transaction, container, false);
        Button submitTransaction = (Button) (view.findViewById(R.id.submit_transaction));
        submitTransaction.setOnClickListener(this);
        return view;
    }
    @Override
    public void onViewCreated(View v,Bundle savedInstanceState){
        transactionDate = (EditText) view.findViewById(R.id.new_transaction_date);
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

        transactionDate.setOnClickListener(new View.OnClickListener() {

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
        transactionDate.setText(chosenDate);
        selectedDate=chosenDate.substring(0,chosenDate.length()-4);
        selectedDate=selectedDate+chosenDate.substring(chosenDate.length()-2,chosenDate.length());
    }


    public static interface DisplaytransactionDetails{
        public void backToTransaction(String tablename);
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        displaytransactionDetails=(DisplaytransactionDetails)activity;
    }

    @Override
    public void onClick(View v) {
        int newTotal=0;
        validTransaction=true;
        String title=((EditText) view.findViewById(R.id.new_transaction_title)).getText().toString();
        String value=((EditText) view.findViewById(R.id.new_transaction_amount)).getText().toString();
        String date=((EditText) view.findViewById(R.id.new_transaction_date)).getText().toString();

        SQLiteOpenHelper transactionOpenHelper=new TransactionDatabaseHandler(getActivity());
        SQLiteDatabase db=transactionOpenHelper.getWritableDatabase();
        ContentValues newTransaction=new ContentValues();
        if(validString(title))
            newTransaction.put("DESCRIPTION",title);
        if(validInteger(value)) {
            newTransaction.put("AMOUNT", value);
            String sAmount=converToScientific(Integer.parseInt(value));
            newTransaction.put("SAMOUNT",sAmount);
        }
            newTransaction.put("DATE",selectedDate);
        try {
            newTotal = totalCalc(Integer.parseInt(value));
            if(newTotal>Double.parseDouble(GoalDetailsFragment.goalAmount)){
                validTransaction=false;
                Toast.makeText(getActivity(),"Entered Amount is higher than Goal!", Toast.LENGTH_SHORT).show();
            }
            newTransaction.put("TOTAL", newTotal);
        }catch (Exception ex){
            validTransaction=false;
            Toast.makeText(getActivity(),"Invalid Entry, Try again!", Toast.LENGTH_SHORT).show();
        }

        if(validTransaction) {
            db.insert(tableName, null, newTransaction);
            db.close();
            updateTotal(newTotal);
            displaytransactionDetails.backToTransaction(tableName);
        }else{
            Toast.makeText(getActivity(), "Incorrect Value(s) entered, Try again", Toast.LENGTH_LONG).show();
        }

    }
    private int totalCalc(int t){
        Integer sum=Integer.parseInt(GoalDetailsFragment.goalTotal)+t;
        return sum;
    }

    private void updateTotal(int value){

        SQLiteOpenHelper goalDatabaseHelper=new GoalDatabaseHandler(getActivity());
        SQLiteDatabase goalDb=goalDatabaseHelper.getWritableDatabase();
        Double progress=(value/Double.parseDouble(GoalDetailsFragment.goalAmount))*100.0;
        String prog=(Double.toString(progress)).format("%.2f",progress);
        prog=prog+"%";
        ContentValues cv=new ContentValues();
        cv.put("TOTAL",value);
        cv.put("PROGRESS",prog);
        String column="TITLE";
        goalDb.update("GOALS",cv,column+"="+"'"+tableName+"'",null);
        GoalDetailsFragment.goalTotal=Integer.toString(value);
        goalDb.close();

    }

    private boolean validString(String s) {
        if (s.length() <= 0) {
            validTransaction = false;
            return false;
        } else
            return true;
    }

    private boolean validInteger(String s) {
        try {
            if (Integer.parseInt(s) >= 0) {
                return true;
            } else
                validTransaction = false;
            return false;
        } catch (Exception ex) {
            validTransaction = false;
            return false;
        }

    }
    @Override
    public void onStop() {
        super.onStop();

    }
    private String converToScientific(int rawAmount){
        String modifiedAmount=Integer.toString(rawAmount);
        if(rawAmount>=1000000){
            double temp=rawAmount/1000000;
            if(rawAmount % 1000000==0)
               modifiedAmount= (Double.toString(temp).substring(0,(Double.toString(temp)).length()-2))+"M";
            else
                modifiedAmount=(Double.toString(temp))+"M";
        }else if(rawAmount>=10000){
            double temp=rawAmount/1000;
            if(rawAmount % 1000==0)
                modifiedAmount= (Double.toString(temp).substring(0,(Double.toString(temp)).length()-2))+"K";
            else
                modifiedAmount=(Double.toString(temp))+"K";
        }else{
                modifiedAmount=Integer.toString(rawAmount);
        }
        return modifiedAmount;
    }


}