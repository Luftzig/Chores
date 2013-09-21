package il.ac.huji.chores;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.parse.ParseUser;
import il.ac.huji.chores.dal.ChoreDAL;
import il.ac.huji.chores.exceptions.DataNotFoundException;
import il.ac.huji.chores.exceptions.FailedToRetrieveOldChoresException;
import il.ac.huji.chores.exceptions.FailedToRetriveAllChoresException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import java.util.Collections;
import java.util.List;
public class ApartmentChoresFragment extends Fragment {
    
    private ArrayAdapter<Chore> _adapter;
    private String _oldestChoreDisplayed = null;
    private static final int HISTORY_FUNC_AMMOUNT = 5; // when the history button is pressed HISTORY_FUNC_AMMOUNT more chores are displayed
    private String _userName = null;
    
     @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         
        View view = inflater.inflate(R.layout.fragment_apartment, container, false);
        return view;
      }
     
     public void onResume()
     {
         super.onResume();

     }
     
     public String getCurUsername(){
    	 if(_userName != null){
    		 return _userName;
    	 }
    	 ParseUser user = ParseUser.getCurrentUser();
		 if(user == null){
    		//user isn't logged in
			LoginActivity.OpenLoginScreen(getActivity(), false);
			user = ParseUser.getCurrentUser();
    	}

		 return user.getUsername();
    	
     }
     
     public void onCreate(Bundle savedInstanceState)
     {
         super.onActivityCreated(savedInstanceState);
         
     }

     public void onActivityCreated (Bundle savedInstanceState)
     {
         super.onActivityCreated(savedInstanceState);
              
         List<Chore> chores=null;
		try {
			chores = ChoreDAL.getAllChores();
		} catch (UserNotLoggedInException e1) {
			
			LoginActivity.OpenLoginScreen(getActivity(), false);
		} catch (FailedToRetriveAllChoresException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
         if(chores != null){
        	 _oldestChoreDisplayed = checkOldestChoreInList(chores);
         }
         
         ListView listChores = (ListView)getActivity().findViewById(R.id.apartmentListChores);
         
         //set item click listener       
         listChores.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // if list chore item is clicked - open chore card
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
            	final Chore chore = (Chore) parent.getItemAtPosition(position);
            	Intent intent = new Intent(getActivity(), ChoreCardActivity.class);
            	intent.putExtra(getResources().getString(R.string.card_activity_extra1_name) ,chore);
            	_userName = getCurUsername();
            	intent.putExtra(getResources().getString(R.string.card_activity_extra2_name) , _userName);
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
		 
		 // apartment settings button
		 final Button history = (Button)getActivity().findViewById(R.id.ApartmentChoresFragment_historyButton);
		 history.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				//chores history button
				List<Chore> histChores=null;
				try {
					histChores = ChoreDAL.getUserOldChores(_oldestChoreDisplayed, HISTORY_FUNC_AMMOUNT);
				} catch (UserNotLoggedInException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FailedToRetrieveOldChoresException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(histChores == null){
					return;
				}
				_adapter.addAll(histChores);
				//check what is the current oldest chore
			}
		 });
     }
     
     //check what chore is the oldest (deadline-wise) from a given list.
    private String checkOldestChoreInList(List<Chore> histChores) {
    	if(histChores.size() == 0){
    		return null;
    	}
    	 
		Collections.sort(histChores, new DeadlineComparator());
		return histChores.get(0).getId();
	}

    //move suggested chore to current user, and send suggestion accepted notification
	public static void doSuggestionAccepted(String suggestedChoreId, Context context) {
		
		try {
			ChoreDAL.updateAssignedTo(suggestedChoreId, ParseUser.getCurrentUser().getUsername());
			//TODO(shani): add call to suggestion accepted.
		} catch (UserNotLoggedInException e) {
			LoginActivity.OpenLoginScreen(context, false);
			return;
		} catch (DataNotFoundException e) {
			Log.e("Exception", e.getMessage());
			return;
		}
	}
}

