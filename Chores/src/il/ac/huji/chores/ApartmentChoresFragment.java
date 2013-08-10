package il.ac.huji.chores;

import android.app.Fragment;
import android.content.Intent;

import il.ac.huji.chores.Chore.CHORE_STATUS;
import il.ac.huji.chores.dal.ChoreDAL;
import il.ac.huji.chores.dummy.DummyChoreDAL;
import il.ac.huji.chores.dummy.GeneralDal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ApartmentChoresFragment extends Fragment {
    
    private ArrayAdapter<Chore> _adapter;
    
     @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         
        View view = inflater.inflate(R.layout.fragment_apartment, container, false);
        return view;
      }

     public void onActivityCreated (Bundle savedInstanceState)
     {
         super.onActivityCreated(savedInstanceState);
         
         getActivity().setContentView(R.layout.fragment_apartment);

         List<Chore> chores = DummyChoreDAL.getAllChores();//TODO change to real chore DAL
         
         ListView listChores = (ListView)getActivity().findViewById(R.id.apartmentListChores);
         
         //set item click listener       
         listChores.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // if list chore item is clicked - open chore card
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
            	final Chore chore = (Chore) parent.getItemAtPosition(position);
            	Intent intent = new Intent(getActivity(), ChoreCardActivity.class);
            	intent.putExtra(getResources().getString(R.string.card_activity_extra1_name) ,chore);
            	intent.putExtra(getResources().getString(R.string.card_activity_extra2_name) , IsThisTheUser(chore.getAssignedTo(), GeneralDal.getUserName()));//TODO(Shani): move it inside the card or get it from the caller
            	startActivity(intent);
            }
        });
         
         //set adapter
         _adapter = new ApartmentChoresDisplayAdapter(getActivity(), chores);
         listChores.setAdapter(_adapter);
         
       	 /**  Buttons listeners **/
         
         // add/edit chores button
		 Button editChores = (Button)getActivity().findViewById(R.id.ApartmentChoresFragment_editChores_button);
		 editChores.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				//open add/edit chores activity
				 Intent intent = new Intent(getActivity(), AddEditChoresActivity.class);
				 startActivity(intent);
			}
			 
		 });
		 
		 
		 // apartment settings button
		 Button apartSettings = (Button)getActivity().findViewById(R.id.ApartmentChoresFragment_apartmentSettings_button);
		 apartSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				//apartment settings button
				 Intent intent = new Intent(getActivity(), ApartmentSettingsActivity.class);
				 startActivity(intent);
			}
		 });
         
     }
     
     static private boolean IsThisTheUser(String choreOwner, String userName)
     {
    	 return choreOwner.equals(userName);
     }
     
}

