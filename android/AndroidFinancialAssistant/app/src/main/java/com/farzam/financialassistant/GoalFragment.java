package com.farzam.financialassistant;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class GoalFragment extends Fragment  {
    View view;
    int i;
    private LinearLayout linearLayout;
    private SimpleCursorAdapter listAdapter;
    private ListView listView;
    private SQLiteDatabase db;
    private Cursor cursor;
    private GoalDetailDisplay goalDetail;
    private EditGoal editGoal;
    private String goalAmount;
    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_goal, container, false);
        linearLayout=(LinearLayout) view.findViewById(R.id.goals_linear_layout);
        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){

        displayGoals();
    }
    //define Interface
    public static interface GoalDetailDisplay{
        public void displayGoalDetails();
        public void displayNewGoalHint();
    }
    // define an Interface for Editting goals
    public static interface EditGoal{
        public void goalIdGetter(long id);
    }

    // define onAttach
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.goalDetail=(GoalDetailDisplay)activity;
        this.editGoal=(EditGoal)activity;
    }
    // Display goals & define click listener
    public void displayGoals(){
        GoalDatabaseHandler gdh=new GoalDatabaseHandler(getActivity());
        SQLiteOpenHelper goalDatabaseHelper=gdh;
        db=goalDatabaseHelper.getWritableDatabase();
        long cnt=gdh.getNumberOfRecords();
        if(cnt<1){
            goalDetail.displayNewGoalHint();
        }else {
            cursor = db.query("GOALS", new String[]{"_id", "TITLE", "TOTAL", "GOAL", "PROGRESS"}, null, null, null, null, null);
            listAdapter = new SimpleCursorAdapter(view.getContext(),
                    R.layout.fragment_custom_design_goal_display, cursor,
                    new String[]{"TITLE", "GOAL", "TOTAL", "PROGRESS"}, new int[]{R.id.row11, R.id.row22,
                    R.id.row22, R.id.progress2}, 0);
            listAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                    int progressValue = 0;

                    String columnName = cursor.getColumnName(columnIndex);
                    String temp = "";
                    double tempAmount = 0.0;
                    switch (columnName) {
                        case "PROGRESS":
                            temp = (cursor.getString(columnIndex));
                            if (temp.length() > 1)
                                temp = temp.substring(0, temp.length() - 1);
                            ProgressBar progressIndicator = (ProgressBar) view.findViewById(R.id.progress2);
                            progressValue = (int) (Double.parseDouble(temp));
                            progressIndicator.setProgress(progressValue);
                            temp = "";
                            if (progressValue == 100)
                                progressIndicator.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                            break;
                        case "TOTAL":

                            temp = (cursor.getString(columnIndex));
                            TextView textProgress1 = (TextView) view.findViewById(R.id.row22);

                            if (Integer.parseInt(temp) > 1000000) {
                                tempAmount = (Integer.parseInt(temp) / 1000000.0);
                                if (Integer.parseInt(temp) % 1000000.0 == 0) {
                                    temp = Double.toString(tempAmount).
                                            substring(0, Double.toString(tempAmount).length() - 2) + "M";
                                } else {
                                    temp = tempAmount + "M";
                                }
                            } else if (Integer.parseInt(temp) >= 100000) {
                                tempAmount = (Integer.parseInt(temp) / 1000.0);
                                if (Integer.parseInt(temp) % 1000.0 == 0) {
                                    temp = Double.toString(tempAmount).
                                            substring(0, Double.toString(tempAmount).length() - 2) + "K";
                                } else {
                                    temp = tempAmount + "K";
                                }

                            }

                            textProgress1.setText(temp + "/" + goalAmount);
                            temp = "";
                            break;
                        case "GOAL":

                            goalAmount = (cursor.getString(columnIndex));
                            if (Integer.parseInt(goalAmount) >= 1000000) {
                                tempAmount = (Integer.parseInt(goalAmount) / 1000000.0);
                                if (Integer.parseInt(goalAmount) % 1000000.0 == 0) {
                                    goalAmount = Double.toString(tempAmount).
                                            substring(0, Double.toString(tempAmount).length() - 2) + "M";
                                } else {
                                    goalAmount = tempAmount + "M";
                                }
                            } else if (Integer.parseInt(goalAmount) > 10000) {
                                tempAmount = (Integer.parseInt(goalAmount) / 1000.0);
                                if (Integer.parseInt(goalAmount) % 1000.0 == 0) {
                                    goalAmount = Double.toString(tempAmount).
                                            substring(0, Double.toString(tempAmount).length() - 2) + "K";
                                } else {
                                    goalAmount = tempAmount + "K";
                                }

                            }
                            // CustomDesignGoalDisplayFragment.goalAmount=temp;
                            temp = "";
                            break;
                        case "TITLE":
                            temp = (cursor.getString(columnIndex));
                            TextView textTitle = (TextView) view.findViewById(R.id.row11);
                            textTitle.setText(temp);
                            temp = "";
                            break;

                    }
                    return true;
                }
            });


            listView = new ListView(getActivity());
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cursor = db.query("GOALS", new String[]{"_id", "TITLE", "DEADLINE", "GOAL", "TOTAL", "PROGRESS"},
                            "_id=?", new String[]{Long.toString(id)}, null, null, null);

                    if (cursor.moveToFirst()) {
                        String goalHeader = cursor.getString(1);
                        String goalDate = cursor.getString(2);
                        String goalAmount = Integer.toString(cursor.getInt(3));
                        String goalTotal = Integer.toString(cursor.getInt(4));
                        String goalProgress = cursor.getString(5);
                        TransactionDatabaseHandler.DB_NAME = goalHeader;
                        GoalDetailsFragment.setGoalparameters(goalHeader, goalAmount, goalDate, goalTotal, goalProgress);
                        cursor.close();
                        db.close();
                        GoalDetailsFragment.tableName = goalHeader;
                        goalDetail.displayGoalDetails();


                    }


                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View v, int position, long id) {
                    editGoal.goalIdGetter(id);
                    return true;
                }
            });
            linearLayout.addView(listView);
        }
    }
    // Destroy Database
   /* @Override
    public void onDestroy() {
        super.onDestroy();

        ((CursorAdapter) listView.getAdapter()).getCursor().close();
        db.close();
    }*/


}
