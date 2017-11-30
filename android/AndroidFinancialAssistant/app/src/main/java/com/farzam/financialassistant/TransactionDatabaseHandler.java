package com.farzam.financialassistant;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by farzam on 10/28/2016.
 */

public class TransactionDatabaseHandler extends SQLiteOpenHelper{
    public static  String DB_NAME;
    private final static int DB_VERSION=1;
    private Cursor cursor;
    SQLiteDatabase db2;
    public static String newTableName;
    private String oldName,newName;
    Context context;
    int k;

    TransactionDatabaseHandler(Context context){
        super(context,"TRANSACTIONS",null,DB_VERSION);
        this.context=context;
    }


    public static void createTable(String name){
        DB_NAME=name;

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            db.execSQL("CREATE TABLE " + DB_NAME + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "DESCRIPTION TEXT, " + "AMOUNT INTEGER, " + "DATE TEXT, " + "TOTAL INTEGER, "+"SAMOUNT TEXT);");
        } catch (Exception e) {

        }
    }
    public void renameTable(String oldName,String newName){
        this.oldName=oldName;
        this.newName=newName;
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + newName +" AS SELECT * FROM "+oldName+";");
        db.delete(oldName,null,null);
        db.execSQL("DROP TABLE IF EXISTS "+oldName);
        db.close();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){


    }
    public boolean createNewTable(String name){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("CREATE TABLE IF NOT EXISTS " + name + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "DESCRIPTION TEXT, " + "AMOUNT INTEGER, " + "DATE TEXT, " + "TOTAL INTEGER, "+"SAMOUNT TEXT);");
            db.close();
            return true;
        } catch(Exception ex){
            MainActivity.showMessage(context,"Invalid Goal, Try again!");
            return false;
        }
    }
    public void deleteTable(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+name);
        db.close();
        }
    public void getColumn(String name,long id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.query(name,new String[]{"DESCRIPTION","AMOUNT","DATE","TOTAL"},
                "_id=?",new String[]{Long.toString(id)},null,null,null);
        if(cursor.moveToFirst()){
            String goalHeader = cursor.getString(0);
            String goalDate = cursor.getString(2);
            String goalAmount = Integer.toString(cursor.getInt(1));
            String goalTotal = Integer.toString(cursor.getInt(3));
            EditTransactionFragment.setParameters(name,goalHeader,goalAmount,goalDate,goalTotal,id);
        }
        cursor.close();
        db.close();
    }
    public int updateRecord(String name,long id,String description,int amount,String date,int newTotal,int difference){
        ArrayList<String> oldTotals=new ArrayList();
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("DESCRIPTION",description);
        contentValues.put("AMOUNT",amount);
        contentValues.put("DATE",date);
        contentValues.put("TOTAL",newTotal);
        db.update(name, contentValues, "_id=?", new String[]{Long.toString(id)});
        k=newTotal;
        if(difference!=0) {
            ContentValues cv=new ContentValues();
            long cnt  = DatabaseUtils.queryNumEntries(db,name);
            long j=id;
            while(j++<cnt) {
                Cursor crsr = db.query(name, new String[]{"DESCRIPTION", "AMOUNT", "DATE", "TOTAL"},
                        "_id=?", new String[]{Long.toString(j)}, null, null, null);
                crsr.moveToFirst();
                int newValue = crsr.getInt(3)+difference;
                k=newValue;
                cv.put("TOTAL",newValue);
                db.update(name,cv,"_id=?",new String[]{Long.toString(j)});

                crsr.close();

            }

        }
        db.close();
        return k;
    }
    public int deleteRecord(String name,long id,int difference){
        SQLiteDatabase db=this.getWritableDatabase();


        if(difference!=0) {
            ContentValues cv=new ContentValues();
            long cnt  = DatabaseUtils.queryNumEntries(db,name);
            long j=id;
            if ((cnt>1)&&(j==cnt)){
                j--;
            }else if(cnt==1){
                this.deleteTable(name);
                return k;

            }

            while(j++<cnt) {
                Cursor crsr = db.query(name, new String[]{"DESCRIPTION", "AMOUNT", "DATE", "TOTAL"},
                        "_id=?", new String[]{Long.toString(j)}, null, null, null);
                crsr.moveToFirst();
                int newValue = crsr.getInt(3)+difference;
                k=newValue;
                cv.put("TOTAL",newValue);
                db.update(name,cv,"_id=?",new String[]{Long.toString(j)});

                crsr.close();
            }

        }
        db.delete(name,"_id=?",new String[]{Long.toString(id)});
        db.close();


        db.close();
        return k;
    }


}
