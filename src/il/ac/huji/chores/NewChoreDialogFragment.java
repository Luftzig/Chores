package il.ac.huji.chores;

import il.ac.huji.chores.ChoreInfo.CHORE_INFO_PERIOD;
import android.R.xml;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

public class NewChoreDialogFragment extends Fragment {

	
	private String _choreName = null;
	private String _howMany="";
	private String _period="";
	private boolean _isEveryone=false;
	private int _value;
	private String[] _xmlList_howMany;
	private String[] _xmlList_every;
	private Boolean _isEdit = false;
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_chore_dialog,
                                    container, false);
        return view;
    }
    
    public void onActivityCreated (Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
    	
    	_xmlList_howMany = getResources().getStringArray(R.array.how_many_array);
    	_xmlList_every = getResources().getStringArray(R.array.every_array);
    	
    	//if it's edit chore, defaults are according to the existing chore
    	final ChoreInfo chore = (ChoreInfo)getActivity().getIntent().getSerializableExtra(getResources().getString(R.string.new_chore_extra1_name));
    	
    	Button createButton = (Button)getActivity().findViewById(R.id.newChoreDialogCreateButton);
    	
    	if(chore != null){
    		
    		//button text should be update chore
    		createButton.setText(getResources().getString(R.string.new_chore_dialog_update_button_text));
    	
    		_isEdit = true;
    		_choreName = chore.getName();
    		_value = chore.getCoinsNum();
    		
    		_isEveryone = chore.isEveryone();
    		
    		//default how many
    		int howMany = chore.getHowManyInPeriod();
    		if(howMany == 1){
    			_howMany = _xmlList_howMany[0];
    		}
    		else if(howMany == 2){
    			_howMany = _xmlList_howMany[1];
    		}
    		else if(howMany == 3){
    			_howMany = _xmlList_howMany[2];
    		}
    		else if(howMany == 4){
    			_howMany = _xmlList_howMany[3];
    		}
    		else if(howMany == 5)
    		{
    			_howMany = _xmlList_howMany[4];
    		}
    		else{ //_howMany == 6
    			_howMany = _xmlList_howMany[5];
    		}
    		//default period
    		CHORE_INFO_PERIOD period = chore.getPriod();
    		if(period == CHORE_INFO_PERIOD.CHORE_INFO_DAY){
    			_period = _xmlList_every[0];
    		}
    		else if(period == CHORE_INFO_PERIOD.CHORE_INFO_MONTH){
    			_period = _xmlList_every[1];
    		}
    		else if(period == CHORE_INFO_PERIOD.CHORE_INFO_WEEK){
    			_period = _xmlList_every[2];
    		}
    		else if(period == CHORE_INFO_PERIOD.CHORE_INFO_YEAR){
    			_period = _xmlList_every[3];
    		}
    		else{
    			_period = _xmlList_every[4];
    		}
    	}

    	//choose chore name from the spinner
    	Spinner choreNameSpinner = (Spinner) getActivity().findViewById(R.id.newChoreDialogWhatSpinner);
    	ArrayAdapter<CharSequence> choresAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.chore_names_array, android.R.layout.simple_spinner_item);
    	choresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	// Apply the adapter to the spinner
    	choreNameSpinner.setAdapter(choresAdapter);
    	if(_isEdit){//set default 
    		int ind = choresAdapter.getPosition(chore.getName());
    		choreNameSpinner.setSelection(ind);
    	}
    
    	choreNameSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    	    @Override
    	    public void onItemSelected(AdapterView<?> parent, View selectedItemView, int position, long id) {
    	        _choreName = parent.getItemAtPosition(position).toString();
    	    }

    	    @Override
    	    public void onNothingSelected(AdapterView<?> parent) {

    	    }

    	});
    	
    	//choose how many from the spinner
    	Spinner howManySpinner = (Spinner) getActivity().findViewById(R.id.newChoreDialogHowManySpinner);
    	ArrayAdapter<CharSequence> howManyAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.how_many_array, android.R.layout.simple_spinner_item);
    	howManyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	// Apply the adapter to the spinner
    	howManySpinner.setAdapter(howManyAdapter);
    	if(_isEdit){//set default 
    		int ind = howManyAdapter.getPosition(_howMany);
    		howManySpinner.setSelection(ind);
    	}
    
    	howManySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    	    @Override
    	    public void onItemSelected(AdapterView<?> parent, View selectedItemView, int position, long id) {
    	        _howMany =parent.getItemAtPosition(position).toString();
    	    }

    	    @Override
    	    public void onNothingSelected(AdapterView<?> parent) {

    	    }

    	});
    	
    	//choose every from the spinner
    	Spinner everySpinner = (Spinner) getActivity().findViewById(R.id.newChoreDialogEverySpinner);
    	ArrayAdapter<CharSequence> everyAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.every_array, android.R.layout.simple_spinner_item);
    	everyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	// Apply the adapter to the spinner
    	everySpinner.setAdapter(everyAdapter);
    	if(_isEdit){//set default 
    		int ind = everyAdapter.getPosition(_period);
    		everySpinner.setSelection(ind);
    	}
    
    	everySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    	    @Override
    	    public void onItemSelected(AdapterView<?> parent, View selectedItemView, int position, long id) {
    	        _period = parent.getItemAtPosition(position).toString();
    	    }

    	    @Override
    	    public void onNothingSelected(AdapterView<?> parent) {

    	    }

    	});
    	
    	//choose value from the spinner
    	Spinner valueSpinner = (Spinner) getActivity().findViewById(R.id.newChoreDialogValueSpinner);
    	ArrayAdapter<CharSequence> valueAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.values_array, android.R.layout.simple_spinner_item);
    	valueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	// Apply the adapter to the spinner
    	valueSpinner.setAdapter(valueAdapter);
    	if(_isEdit){//set default 
    		int ind = valueAdapter.getPosition(Integer.toString(chore.getCoinsNum()));
    		valueSpinner.setSelection(ind);
    	}
    
    	valueSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    	    @Override
    	    public void onItemSelected(AdapterView<?> parent, View selectedItemView, int position, long id) {
    	        _value = (Integer.parseInt(parent.getItemAtPosition(position).toString()));
    	    }

    	    @Override
    	    public void onNothingSelected(AdapterView<?> parent) {

    	    }

    	});
    	
    	//is everyone check box
    	 final CheckBox checkBox = (CheckBox) getActivity().findViewById(R.id.newChoreDialogEveryoneCheckbox);
         _isEveryone = checkBox.isChecked();
         
         //Create chore button
		 createButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				// get period
				CHORE_INFO_PERIOD period;
				
				 if(_period.equals(_xmlList_every[0])){
					 period = CHORE_INFO_PERIOD.CHORE_INFO_DAY;
				 }
				 else if(_period.equals(_xmlList_every[1])){
					 period = CHORE_INFO_PERIOD.CHORE_INFO_WEEK;
				 }
				 else if(_period.equals(_xmlList_every[2])){
					 period = CHORE_INFO_PERIOD.CHORE_INFO_MONTH;
				 }
				 else if(_period.equals(_xmlList_every[3])){
					 period = CHORE_INFO_PERIOD.CHORE_INFO_DAY;
				 }
				 else{ //not repeated
					 period = CHORE_INFO_PERIOD.CHORE_INFO_NOT_REPEATED;
				 }
				 
				 //get how many
				 int howMany=0;
				 if(_howMany.equals(_xmlList_howMany[0])){
					 howMany = 1;
				 }
				 else if(_howMany.equals(_xmlList_howMany[1])){
					 howMany = 2;
				 }
				 else if(_howMany.equals(_xmlList_howMany[2])){
					 howMany = 3;
				 }
				 else if(_howMany.equals(_xmlList_howMany[3])){
					 howMany = 4;
				 }
				 else if(_howMany.equals(_xmlList_howMany[4])){
					 howMany=5;
				 }
				 else{
					 howMany = 6;
				 }
				 
				
				ChoreInfo newChore = (ChoreInfo) new ChoreInfoInstance(_choreName, _value, howMany, period, _isEveryone);
				
				//send back new chore
				Intent resultIntent = new Intent();
				resultIntent.putExtra(getResources().getString(R.string.new_chore_extra1_name), newChore);
				getActivity().setResult(Activity.RESULT_OK, resultIntent);
				getActivity().finish();
			}
			 
		 });
		 
		 //Create cancel button
         Button cancelButton = (Button)getActivity().findViewById(R.id.newChoreDialogCancelButton);
         cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent resultIntent = new Intent();
				getActivity().setResult(Activity.RESULT_CANCELED, resultIntent);
				getActivity().finish();
			}
			
		 });

    }
    

}
