package com.farzam.financialassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity  implements NewChoiceFragment.SetNewGoalListener,NewGoalFragment.BackToMain,
        GoalFragment.GoalDetailDisplay, NewTransactionFragment.DisplaytransactionDetails,GoalFragment.EditGoal,
        GoalDetailsFragment.TransactionEditInterface,EditTransactionFragment.BackFromTransactionEdit,DatePickerFragment.BackToNewGoal{

    Fragment fragment;
    private String currentFragment;
    private boolean removeFragment;
    private Menu myMenu;
    private boolean addToBackStack;
    private FragmentTransaction ft;
    int[] selectedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment=new GoalFragment();
        currentFragment="GoalFragment";
        setFrameLayout(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        myMenu=menu;
      //  MenuItem editMenu=menu.findItem(R.id.edit_goal);
       // editMenu.setEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.new_goal :
                if (isCurrentFragment()){
                    fragment=new NewGoalFragment();
                    currentFragment="NewGoalFragment";


                }else{
                    fragment=new NewTransactionFragment();
                    currentFragment="NewTransactionFragment";

                }

                setFrameLayout(fragment);
                break;
            case R.id.home :
                fragment=new GoalFragment();
                currentFragment="GoalFragment";
                setFrameLayout(fragment);
                break;
            case R.id.help :
                fragment=new HelpFragment();
                currentFragment="HelpFragment";
                setFrameLayout(fragment);
                break;


            /*case R.id.edit_goal :
                Fragment f = this.getFragmentManager().findFragmentById(R.id.set_goal);
                if (f instanceof GoalDetailsFragment) {
                    fragment=new EditTransactionFragment();
                    currentFragment="EditTransactionFragment";
                    addToBackStack=true;
                    setFrameLayout(fragment);

                }else{

                }*/

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void chooseGoalMode(int position){

        switch (position){
            case 0:
                fragment=new NewGoalFragment();
                currentFragment="NewGoalFragment";

                break;
            case 1:
                fragment=new TransactionFragment();
                currentFragment="TransactionFragment";
                break;

        }

        setFrameLayout(fragment);
    }
    // Implement GoalDetailDisplay method
    @Override
    public void displayGoalDetails(){
        fragment=new GoalDetailsFragment();
        currentFragment="GoalDetailsFragment";

        setFrameLayout(fragment);
    }


    @Override
    public void displayMain(){
        fragment= new GoalFragment();
        currentFragment="GoalFragment";
       /* Fragment f =getFragmentManager().findFragmentById(R.id.set_goal);
        while(getFragmentManager().getBackStackEntryCount()>0) {
            getFragmentManager().popBackStackImmediate();
        }*/

        setFrameLayout(fragment);
    }

    @Override
    public void backToTransaction(String tableName){
        GoalDetailsFragment.tableName=tableName;
        fragment=new GoalDetailsFragment();
        currentFragment="GoalDetailsFragment";
        setFrameLayout(fragment);
    }

    @Override
    public void goalIdGetter(long id){
        EditGoalFragment.id=id;
        fragment=new EditGoalFragment();
        currentFragment="EditGoalFragment";

        setFrameLayout(fragment);
    }

    @Override
    public void details(String name,long id){
        TransactionDatabaseHandler tdh=new TransactionDatabaseHandler(this);
        tdh.getColumn(name,id);
        fragment= new EditTransactionFragment();
        currentFragment="EditTransactionFragment";

        setFrameLayout(fragment);
    }
    @Override
    public void backToGoalDetails(){
       // getFragmentManager().popBackStack();
        fragment=new GoalDetailsFragment();
        currentFragment="GoalDetailsFragment";
        setFrameLayout(fragment);


    }


    public void setFrameLayout(Fragment fragment){
        ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.set_goal,fragment);
            ft.addToBackStack(null);

        ft.commit();
    }
    public boolean isCurrentFragment(){
        Fragment f =getFragmentManager().findFragmentById(R.id.set_goal);
        if ((f instanceof GoalFragment)||(f instanceof AddGoalHintFragment))
            return true;
        else
            return false;
    }
    @Override
    public void showDatePickerFragment(){
        fragment=new DatePickerFragment();
        currentFragment="DatePickerFragment";

        setFrameLayout(fragment);
    }
    @Override
    public void hitBackStack(int[] args){
        selectedDate=args;
        getFragmentManager().beginTransaction().
        remove(getFragmentManager().findFragmentById(R.id.set_goal)).commit();

    }
    @Override
    public int[] getSelectedDate(){

        return selectedDate;
    }
    public static void showMessage(Context context,String message){
        Toast.makeText(context,context.toString(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed(){

        Fragment f =getFragmentManager().findFragmentById(R.id.set_goal);

        if(isCurrentFragment()){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit App")
                    .setMessage("Are you sure you want to close this App?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }else if((f instanceof EditTransactionFragment)) {
            super.onBackPressed();
        }else{
            fragment=new GoalFragment();
            setFrameLayout(fragment);
        }
       /*while(getFragmentManager().getBackStackEntryCount()>1) {
           getFragmentManager().popBackStackImmediate();
       }*/


    }
    @Override
    public void displayNewGoalHint(){
        fragment=new AddGoalHintFragment();
        currentFragment="AddGoalHintFragment";
        setFrameLayout(fragment);
    }
    public void shareFeedback(View v){
        Intent sharingIntent = new Intent(Intent.ACTION_SENDTO);
        sharingIntent.setType("text/plain");
        sharingIntent.setData(Uri.parse("mailto:"));
        sharingIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"f_ghanbarnezhad@hotmail.com"});
        String shareBody = "Your valuable suggestions go here";
        sharingIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
        startActivity(sharingIntent);
    }


}
