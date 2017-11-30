package com.farzam.financialassistant;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class GoalDetailsFragment extends Fragment {

    private  View view;
    public static String goalTitle,goalDate,goalAmount,goalTotal,goalProgress;
    private SQLiteDatabase db;
    private Cursor cursor;
    private LinearLayout linearLayout;
    private CursorAdapter listAdapter;
    private ListView listView;
    public static String tableName;
    private TransactionEditInterface transactionEditInterface;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_goal_details, container, false);

        return view;
    }
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
       linearLayout=(LinearLayout) view.findViewById(R.id.transaction_details);
        displayTransactions();
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        transactionEditInterface=(TransactionEditInterface)activity;
    }

    // define interface for transaction edit
    public static interface TransactionEditInterface{
        public void details(String tableName,long id);
    }

    // set static parameters
    public static void setGoalparameters(String title,String amount,String date,String total,String progress){
        goalTitle=title;
        goalAmount=amount;
        goalDate=date;
        goalTotal=total;
        goalProgress=progress;
    }
    private void displayTransactions(){

        GoalHeaderFragment.setTexts(goalTitle,goalAmount,goalDate,goalTotal);
        Fragment goalHeaderFragment=new GoalHeaderFragment();
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.goal_header,goalHeaderFragment);
        ft.commit();
        final SQLiteOpenHelper transactionDatabaseHelper=new TransactionDatabaseHandler(getActivity());
        db=transactionDatabaseHelper.getWritableDatabase();
        try{
        cursor=db.query(tableName,new String[] {"_id","DESCRIPTION",
                "SAMOUNT","DATE","TOTAL"},null,null,null,null,null);
        listAdapter=new SimpleCursorAdapter(getActivity(),R.layout.transaction_custom_layout,
                cursor,new String[]{"DESCRIPTION","SAMOUNT","DATE","TOTAL"},
                new int[]{R.id.transaction_description,R.id.transaction_amount,
                        R.id.transaction_date,R.id.transaction_total},1);

        listView=new ListView(getActivity());
        listView.setAdapter(listAdapter);
        listView.setDivider(null);

       // listView.setDivider(new ColorDrawable(getResources().getColor(android.R.color.black)));
       // listView.setDividerHeight(1);
        
        linearLayout.addView(listView);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0,View v,int position,long id){

                transactionEditInterface.details(tableName,id);
                return true;
            }
        });} catch(Exception ex){

        }
    }
    // Destroy Database
    @Override
    public void onDestroy() {
        super.onDestroy();
try {
    ((CursorAdapter) listView.getAdapter()).getCursor().close();
    db.close();
}catch(Exception ex){

}
    }

}
