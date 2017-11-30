package com.farzam.financialassistant;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class EditTransactionFragment extends Fragment {

    private static String tableName,description,amount,date,total;
    private static long id;
    private EditText transDescription,transactionAmount,transactionDate;
    private boolean isValidUpdate=true;
    private BackFromTransactionEdit back;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view= inflater.inflate(R.layout.fragment_edit_transaction, container, false);
        eidtButtonDefinition();
        deleteButtonDefinition();
        return view;
    }
    public static interface BackFromTransactionEdit{
        public void backToGoalDetails();
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.back=(BackFromTransactionEdit)activity;
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceInstance){
        transDescription=(EditText)v.findViewById(R.id.edit_transaction_title);
        transactionAmount=(EditText)v.findViewById(R.id.edit_transaction_amount);
        transactionDate=(EditText)v.findViewById(R.id.edit_transaction_date);
        transDescription.setText(description);
        transactionAmount.setText(amount);
        transactionDate.setText(date);
    }

    public static void setParameters(String name,String desc,String v,String d,
                                     String t,long Id){
        tableName=name;
        description=desc;
        amount=v;
        date=d;
        total=t;
        id=Id;
    }
    public void eidtButtonDefinition(){
        Button editTransactionButton=(Button)view.findViewById(R.id.edit_transaction);
        editTransactionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String editeddescription=transDescription.getText().toString();
                int editedAmount=0;
                try {
                    editedAmount = Integer.parseInt(transactionAmount.getText().toString());
                    isValidUpdate=true;
                }catch(Exception ex){
                    isValidUpdate=false;
                    Toast.makeText(getActivity(),"Invalid Entry, Please try again.", Toast.LENGTH_SHORT).show();
                }
                String editedDate=transactionDate.getText().toString();
                TransactionDatabaseHandler tdh=new TransactionDatabaseHandler(getActivity());
                if(isValidUpdate) {
                    int difference = editedAmount- Integer.parseInt(amount);
                    int newTotal=Integer.parseInt(total)+difference;
                    int oldTotals=tdh.updateRecord(tableName, id, editeddescription, editedAmount, editedDate,newTotal,difference);
                    GoalDatabaseHandler gdh=new GoalDatabaseHandler(getActivity());
                    gdh.updateRecord(tableName,oldTotals);


                }
                back.backToGoalDetails();
            }
        });
    }
    public void deleteButtonDefinition() {
        Button deleteTransactionButton = (Button) view.findViewById(R.id.delete_transaction);
        deleteTransactionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                TransactionDatabaseHandler tdh=new TransactionDatabaseHandler(getActivity());
                int newTotal=tdh.deleteRecord(tableName,id,(Integer.parseInt(amount))*-1);
                GoalDatabaseHandler gdh=new GoalDatabaseHandler(getActivity());
                gdh.updateRecord(tableName,newTotal);
                back.backToGoalDetails();
            }
        });

    }
    }
