package com.farzam.financialassistant;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
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


public class EditGoalFragment extends Fragment {

    private View view;
    public static long id;
    private SQLiteDatabase db;
    private String goalHeader,goalDate,goalAmount,goalPriority,oldTable,newTable;
    private boolean validUpdate,shouldRename;
    private int goalValue,goalImportance;
    private NewGoalFragment.BackToMain backToMainFromEdit;
    String previousName;
    EditText edittedGoalDate;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_edit_goal, container, false);
        updateGoal();
        displayContent(id);
        return view;
    }

    @Override
    public void onViewCreated(View v,Bundle savedInstanceState){
        edittedGoalDate = (EditText) view.findViewById(R.id.edit_goal_deadline);
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

        edittedGoalDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }
    private void updateLabel() {


        String chosenDate=Integer.toString(myCalendar.get(Calendar.MONTH))+
                "/"+myCalendar.get(Calendar.DAY_OF_MONTH)+"/"+myCalendar.get(Calendar.YEAR);
        edittedGoalDate.setText(chosenDate);
    }



    public static interface EditGoalDetailDisplay{
        public void displayEdittedGoalDetails();
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.backToMainFromEdit=(NewGoalFragment.BackToMain)activity;
    }

    public void displayContent(long id){
        SQLiteOpenHelper goalOpenHelper=new GoalDatabaseHandler(getActivity());
        db=goalOpenHelper.getWritableDatabase();
        Cursor cursor=db.query("GOALS",new String[] {"_id","TITLE","DEADLINE","GOAL","PROGRESS"},
                "_id=?",new String[] {Long.toString(id)},null,null,null);
        if (cursor.moveToFirst()) {
            previousName = cursor.getString(1);
            goalDate = cursor.getString(2);
            goalAmount = Integer.toString(cursor.getInt(3));
            goalPriority = Integer.toString(cursor.getInt(0));
            EditText editHeader = (EditText) view.findViewById(R.id.edit_goal_title);
            EditText editAmount = (EditText) view.findViewById(R.id.edit_goal_amount);
            EditText editDeadline = (EditText) view.findViewById(R.id.edit_goal_deadline);

            editHeader.setText(previousName);
            editAmount.setText(goalAmount);
            editDeadline.setText(goalDate);

            cursor.close();
            db.close();
        }

    }
    public void updateGoal(){
        Button editButton=(Button)view.findViewById(R.id.edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validUpdate=true;
                EditText editHeader = (EditText) view.findViewById(R.id.edit_goal_title);
                EditText editAmount = (EditText) view.findViewById(R.id.edit_goal_amount);
                EditText editDeadline = (EditText) view.findViewById(R.id.edit_goal_deadline);

                ContentValues contentValues=new ContentValues();
                try {
                    goalHeader = editHeader.getText().toString();
                    if (isValidString(goalHeader)) {
                        goalHeader = goalHeader.replace(" ", "_");
                        contentValues.put("TITLE", goalHeader);
                        if(!goalHeader.equals(previousName))
                            renameTable(previousName,goalHeader);
                    }
                    goalAmount = editAmount.getText().toString();
                    goalValue = Integer.valueOf(goalAmount);
                    if (isValidInteger(goalValue)) {
                        contentValues.put("GOAL", goalValue);
                    }
                    goalDate = editDeadline.getText().toString();
                    if (isValidString(goalDate))
                        contentValues.put("DEADLINE", goalDate);

                    if (validUpdate) {
                        SQLiteOpenHelper goalOpenHelper = new GoalDatabaseHandler(getActivity());
                        db = goalOpenHelper.getWritableDatabase();
                        Cursor cursor = db.query("GOALS", new String[]{"_id", "TITLE", "DEADLINE", "GOAL", "TOTAL", "PROGRESS"},
                                "_id=?", new String[]{Long.toString(id)}, null, null, null);
                        if (cursor.moveToFirst()) {
                            double temp=(cursor.getInt(4)*100.00/goalValue);
                            String prog=(Double.toString(temp)).format("%.2f",temp);
                            prog=prog+"%";
                            contentValues.put("PROGRESS",prog);
                            db.update("GOALS", contentValues, "_id=" + id, null);
                            cursor.close();
                            db.close();
                            if(shouldRename)
                            replaceTable();
                            backToMainFromEdit.displayMain();
                        } else {
                            Toast.makeText(getActivity(), "Invalid Entries, Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
               }catch(Exception ex){
                    Toast.makeText(getActivity(),"An Error Occured!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button deleteButton=(Button)view.findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete Goal")
                        .setMessage("Are you sure you want to delete this Goal?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteOpenHelper goalOpenHelper=new GoalDatabaseHandler(getActivity());
                                db=goalOpenHelper.getWritableDatabase();
                                db.delete("GOALS","_id=?",new String[] {Long.toString(id)});
                                db.close();
                                TransactionDatabaseHandler tdh=new TransactionDatabaseHandler(getActivity());
                                tdh.deleteTable(previousName);
                                backToMainFromEdit.displayMain();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                      //  .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        }
        );

    }
    public boolean isValidString(String s){
        try {
            if (s.length() == 0) {
                validUpdate = false;
                return false;
            } else {
                return true;
            }
        }catch(Exception ex){
            return false;
        }

    }
    public boolean isValidInteger(int i){
        try {
            if (i <= 0) {
                validUpdate = false;
                return false;
            } else {
                return true;
            }
        }catch(Exception ex){
            return false;
        }
    }
    public void renameTable(String oldName,String newName) {
        this.oldTable=oldName;
        this.newTable=newName;
        shouldRename=true;

//        db2.execSQL("INSERT INTO " + newName + " SELECT * FROM " +oldName);
    }
    private void replaceTable(){
        TransactionDatabaseHandler.createTable(newTable);
        Toast.makeText(getActivity(),newTable+" Creted!", Toast.LENGTH_LONG).show();
        TransactionDatabaseHandler transactionDatabaseHelper=new TransactionDatabaseHandler(getActivity());
        transactionDatabaseHelper.renameTable(oldTable,newTable);


    }


}
