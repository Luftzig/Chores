package il.ac.huji.chores;

import java.util.Calendar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class ChoreCardFragment extends Fragment {

	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 
	    View view = inflater.inflate(R.layout.fragment_card, container, false);
	    return view;
	  }

	 public void onActivityCreated (Bundle savedInstanceState)
	 {
		 super.onActivityCreated(savedInstanceState);
		  
	 }
	 
	 /*
	  * this method receives arguments that contain info about the current chore,
	  * and organiz UI accordingly (set right text in text view and buttons, removes buttons if needed).
	  * ownerOpen - true if the the owner of this card is the one who opened the card. false otherwise.
	  */
	 public void OrganizeUIParts(Chore chore, boolean ownerOpen){
		 
		 //set chore name
		 TextView name = (TextView)getActivity().findViewById(R.id.card_chore_name);
		 name.setText(chore.getName());
		 
		 //set number of coins 
		 TextView coinsNum = (TextView)getActivity().findViewById(R.id.card_coin_count);
		 coinsNum.setText(chore.getCoinsNum() + getResources().getString(R.string.card_coins_num_textend));
		 
		 //set chore type
		 TextView type = (TextView)getActivity().findViewById(R.id.card_chore_type);
		 type.setText(getResources().getString(R.string.card_choretype_textStart) + chore.getType());
		 
		 //set deadline
		 TextView deadline = (TextView)getActivity().findViewById(R.id.card_deadline_txt);
		 String deadlineStr = chore.getPrintableDate(chore.getDeadline());
		 deadline.setText(getResources().getString(R.string.card_deadline_textStart) + deadlineStr);
		 
		 //fun fact
		 TextView funFact = (TextView)getActivity().findViewById(R.id.card_fun_fact);
		 funFact.setText(getResources().getString(R.string.card_funfact_textStart) + chore.getFunFact());
		 
		 //statistics
		 TextView statistics = (TextView)getActivity().findViewById(R.id.card_statistics);
		 statistics.setText(getResources().getString(R.string.card_statistics_textStart) + chore.getStatistics());
		 
		 //button handling
		 
		 //check if chore start time have passed
		 Calendar today = Calendar.getInstance();
		 Calendar otherTime = Calendar.getInstance();
		 otherTime.setTime(chore.getStartsFrom());
		 boolean beforeStartDate = today.before(otherTime);

		 
		 //check if deadline have passed
		 otherTime.setTime(chore.getDeadline());
		 boolean afterDeadline = today.after(otherTime);
		 
		 Button leftButton = (Button)getActivity().findViewById(R.id.card_button_left);
		 Button rightButton = (Button)getActivity().findViewById(R.id.card_button_right);

		 
		 if(beforeStartDate){ // can't do chore yet
			 rightButton.setEnabled(false);
			 rightButton.setVisibility(Button.GONE);
			 
			 if(ownerOpen) { //only suggest chore button
				 
				 leftButton.setEnabled(true);
				 leftButton.setVisibility(Button.VISIBLE);
				 leftButton.setText(getResources().getString(R.string.button_suggestchore_text));
			 }
			 else{ // no buttons
				 leftButton.setEnabled(false);
				 leftButton.setVisibility(Button.GONE);
			 }
				 
		 }
		 else if(afterDeadline){ // deadline have passed
	
			 // no buttons
			 rightButton.setEnabled(false);
			 rightButton.setVisibility(Button.GONE);
			 leftButton.setEnabled(false);
			 leftButton.setVisibility(Button.GONE);
		 }
		 else{ // chore time
			 
			 if(ownerOpen) { // I'm done button, and suggest chore button
				 
				 rightButton.setEnabled(true);
				 rightButton.setVisibility(Button.VISIBLE);
				 rightButton.setText(getResources().getString(R.string.button_done_text));
				 
				 leftButton.setEnabled(true);
				 leftButton.setVisibility(Button.VISIBLE);
				 leftButton.setText(getResources().getString(R.string.button_suggestchore_text));
			 }
			 else{ // steal chore button
				 
				 rightButton.setEnabled(false);
				 rightButton.setVisibility(Button.GONE);
				 
				 leftButton.setEnabled(true);
				 leftButton.setVisibility(Button.VISIBLE);
				 leftButton.setText(getResources().getString(R.string.button_stealchore_text));
			 }
		 }
	 }

}

