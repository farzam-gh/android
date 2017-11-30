package com.farzam.financialassistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by farzam on 10/18/2016.
 */

public class GoalDatabaseHandler extends SQLiteOpenHelper {
    private static final String DB_NAME="GOALS";
    private static final int DB_VERSION=1;
    private Cursor cursor;

    GoalDatabaseHandler(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE GOALS ("+"_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
        "TITLE TEXT, "+"DEADLINE TEXT, "+"GOAL INTEGER, "+"TOTAL INTEGER, "+"PROGRESS TEXT);");
       /* ContentValues goalValues=new ContentValues();
        goalValues.put("TITLE","Goal Title");
        goalValues.put("DEADLINE","Deadline");
        goalValues.put("GOAL",0);
        goalValues.put("TOTAL",0);
        goalValues.put("PROGRESS","0%");
        db.insert("GOALS",null,goalValues);*/



    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
    public void showGoals(){


    }
    public void updateRecord(String name,int newTotal){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("TOTAL",newTotal);
        Double progress=(newTotal/Double.parseDouble(GoalDetailsFragment.goalAmount))*100.0;
        String prog=(Double.toString(progress)).format("%.2f",progress);
        prog=prog+"%";
        cv.put("PROGRESS",prog);
        db.update("GOALS",cv,"TITLE=?",new String[]{name});
        GoalDetailsFragment.goalTotal=Integer.toString(newTotal);
        db.close();

    }
    public long getNumberOfRecords(){
        SQLiteDatabase db=this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db,"GOALS");
        return cnt;
    }

}
