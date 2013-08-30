package il.ac.huji.chores;

import il.ac.huji.chores.Chore.CHORE_STATUS;
import il.ac.huji.chores.dal.ApartmentDAL;
import il.ac.huji.chores.dal.ChoreDAL;
import il.ac.huji.chores.dal.RoommateDAL;
import il.ac.huji.chores.exceptions.FailedToUpdateStatusException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
				 setListenersToCardButtons(null, leftButton, null, chore.getId());
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
				 setListenersToCardButtons(rightButton, leftButton, null, chore.getId());
			 }
			 else{ // steal chore button
				 
				 rightButton.setEnabled(false);
				 rightButton.setVisibility(Button.GONE);
				 
				 leftButton.setEnabled(true);
				 leftButton.setVisibility(Button.VISIBLE);
				 leftButton.setText(getResources().getString(R.string.button_stealchore_text));
				 setListenersToCardButtons(null, null, rightButton, chore.getId());
				 
			 }
		 }
	 }
	 
	 // Sets button listeners to the steal chore, done chore, and suggest chore buttons.
	 //The function's arguments are the buttons, or null if the button shouldn't have any functionality.
	 private void setListenersToCardButtons(Button doneButton, Button suggestButton, Button StealButton, String choreId){
		
		 final String id = choreId;

		 if(doneButton != null){
			 doneButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// update chore status to done
					try {
						ChoreDAL.updateChoreStatus(id, CHORE_STATUS.STATUS_FUTURE);
						notifyRoomate();
					} catch (FailedToUpdateStatusException e) {
						// TODO (shani) decide what to do
					}
					
				}
			});
		 }
		 if(suggestButton != null){
			 suggestButton.setOnClickListener(new OnClickListener() {

				 @Override
				 public void onClick(View v) {
					 //start alert dialog to choose the roommate.
					 openChoreSuggestionDialog();
				 }
			 });
		 }
		 if(StealButton != null){
			 StealButton.setOnClickListener(new OnClickListener() {

				 @Override
				 public void onClick(View v) {
					 // TODO Auto-generated method stub

				 }
			 });
		 }
	 }
	 
	 
	 private void openChoreSuggestionDialog() {//TODO
		 
//		 List<String> roommates = ApartmentDAL.getApartmentRoommates(RoommateDAL.getApartmentID());
//		final  List<String> mSelectedItems = new ArrayList<String>();
//
//		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); 
//
//		 builder.setTitle(getResources().getString(R.string.chore_suggestion_dialog_title))
//
//		           .setMultiChoiceItems(R.array.toppings, null, new DialogInterface.OnMultiChoiceClickListener() {
//		               @Override
//		               public void onClick(DialogInterface dialog, int which,
//		                       boolean isChecked) {
//		                   if (isChecked) {
//		                       // If the user checked the item, add it to the selected items
//		                       mSelectedItems.add(which);
//		                   } else if (mSelectedItems.contains(which)) {
//		                       // Else, if the item is already in the array, remove it 
//		                       mSelectedItems.remove(Integer.valueOf(which));
//		                   }
//		               }
//		           })
//		    // Set the action buttons
//		           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//		               @Override
//		               public void onClick(DialogInterface dialog, int id) {
//		                   // User clicked OK, so save the mSelectedItems results somewhere
//		                   // or return them to the component that opened the dialog
//		                   ...
//		               }
//		           })
//		           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//		               @Override
//		               public void onClick(DialogInterface dialog, int id) {
//		                   ...
//		               }
//		           });

		
	}

	//ask server to notify roomates about a change
	 private void notifyRoomate()
	 {
		 //TODO
	 }

}

