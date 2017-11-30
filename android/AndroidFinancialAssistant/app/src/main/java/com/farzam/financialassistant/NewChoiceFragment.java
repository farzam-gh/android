package com.farzam.financialassistant;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;



/**
 * A simple {@link Fragment} subclass.
 */
public class NewChoiceFragment extends Fragment {

    private AdapterView.OnItemClickListener listener;


    public static interface SetNewGoalListener {
        public void chooseGoalMode(int position);
    }
    private SetNewGoalListener fragmentInterface;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        return inflater.inflate(R.layout.fragment_new, container, false);
    }

   @Override
    public void onActivityCreated(Bundle savedInstanceState){

       super.onActivityCreated(savedInstanceState);
       ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity()
               ,android.R.layout.simple_list_item_1,
               getResources().getStringArray(R.array.new_choice));

       listener=new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               switch(position){
                   case 0:
                       fragmentInterface.chooseGoalMode(0);
                       break;
                   case 1:
                       fragmentInterface.chooseGoalMode(1);


               }
           }
       };


       ListView list=(ListView) getActivity().findViewById(R.id.new_choices);
       list.setAdapter(adapter);
       list.setOnItemClickListener(listener);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try {
            this.fragmentInterface = (SetNewGoalListener) activity;
        } catch (ClassCastException cce){
            throw new ClassCastException(activity.toString()
                    + " must implement interface");
        }

    }









}
