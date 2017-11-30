package layout;


import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.farzam.financialassistant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewChoiceFragment extends ListFragment implements AdapterView.OnItemClickListener{
    private String[] newChoices;
    private Fragment fragment;

public static interface SetNewGoalListener {
    public void chooseGoalMode(int position);
}
    public SetNewGoalListener goalListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        newChoices=getResources().getStringArray(R.array.new_choice);
        ArrayAdapter<String> newChoicesAdapter=new ArrayAdapter<String>(
                inflater.getContext(),
                android.R.layout.simple_list_item_1,newChoices);
        setListAdapter(newChoicesAdapter);




        return super.onCreateView(inflater,container,savedInstanceState);
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        this.goalListener=(SetNewGoalListener)getActivity();
    }

    @Override
    public void onItemClick(AdapterView<?> parent,View view,int position,long id){

        String text=Integer.toString(position);
        Toast.makeText(getActivity(),text, Toast.LENGTH_SHORT).show();
        goalListener.chooseGoalMode(position);





    }

}
