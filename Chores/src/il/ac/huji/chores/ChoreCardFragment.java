package il.ac.huji.chores;

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
import il.ac.huji.chores.Chore.CHORE_STATUS;
import il.ac.huji.chores.dal.ApartmentDAL;
import il.ac.huji.chores.dal.ChoreDAL;
import il.ac.huji.chores.dal.NotificationsDAL;
import il.ac.huji.chores.dal.RoommateDAL;
import il.ac.huji.chores.exceptions.DataNotFoundException;
import il.ac.huji.chores.exceptions.FailedToUpdateStatusException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;


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
	 public void OrganizeUIParts(Chore chore, String curUser){
		
		boolean ownerOpen = ((curUser.equals(chore.getAssignedTo())) ? (true) : (false));
		boolean everyone = (((Constants.CHORE_ASSIGNED_TO_EVERYONE).equals(chore.getAssignedTo())) ? (true) : (false)); 
		 
		 //set chore name
		 TextView name = (TextView)getActivity().findViewById(R.id.card_chore_name);
		 name.setText(chore.getName());
		 
		 //set number of coins 
		 TextView coinsNum = (TextView)getActivity().findViewById(R.id.card_coin_count);
		 coinsNum.setText(chore.getCoinsNum() + getResources().getString(R.string.card_coins_num_textend));
		 
		 //set chore type
		 TextView type = (TextView)getActivity().findViewById(R.id.card_chore_type);
		 type.setText(getResources().getString(R.string.card_choretype_textStart) + getChoreType(chore.getName()));
		 
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
		 
		 //get roommates list
		 List<String> roommatesList=new ArrayList<String>();

		 try {
			 roommatesList = ApartmentDAL.getApartmentRoommates(RoommateDAL.getApartmentID());
		 } catch (UserNotLoggedInException e) {
			 // TODO Auto-generated catch block
			 LoginActivity.OpenLoginScreen(getActivity(), false);
			 return;
		 }
		 

		 //handle buttons
		 if(beforeStartDate){ // can't do chore yet
			 rightButton.setEnabled(false);
			 rightButton.setVisibility(Button.GONE);
			 
			 if(ownerOpen) { //only suggest chore button
				 
				 if(roommatesList.size() == 0){
					 return;
				 }
				 
				 leftButton.setEnabled(true);
				 leftButton.setVisibility(Button.VISIBLE);
				 leftButton.setText(getResources().getString(R.string.button_suggestchore_text));
				 setListenersToCardButtons(null, leftButton, null, chore, curUser, roommatesList);
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
			 
			 if(ownerOpen || everyone) { // I'm done button, and suggest chore button if owner opens. only done if everyone.
				
				 rightButton.setEnabled(true);
				 rightButton.setVisibility(Button.VISIBLE);
				 rightButton.setText(getResources().getString(R.string.button_done_text));

				 if((roommatesList.size() != 0) && (!everyone)){
					 leftButton.setEnabled(true);
					 leftButton.setVisibility(Button.VISIBLE);
					 leftButton.setText(getResources().getString(R.string.button_suggestchore_text));
					 
				 }
				 else{
					 leftButton.setEnabled(false);
					 leftButton.setVisibility(Button.GONE);
					 leftButton = null;
				 }
				 setListenersToCardButtons(rightButton, leftButton, null, chore, curUser, roommatesList);
				 
			 }
			 else{ // steal chore button
				 

				 
				 rightButton.setEnabled(false);
				 rightButton.setVisibility(Button.GONE);
				 
				 leftButton.setEnabled(true);
				 leftButton.setVisibility(Button.VISIBLE);
				 leftButton.setText(getResources().getString(R.string.button_stealchore_text));
				 
				 setListenersToCardButtons(null, null, leftButton, chore, curUser, roommatesList);
				 
			 }
		 }
	 }
	 
	 // Sets button listeners to the steal chore, done chore, and suggest chore buttons.
	 //The function's arguments are the buttons, or null if the button shouldn't have any functionality.
	 //roommates - a list of the user roommates. needed only for chore suggestion.
	 private void setListenersToCardButtons(Button doneButton, Button suggestButton, Button StealButton, final Chore chore, final String curUser, final List<String> roommates){
		

		 if(doneButton != null){
			 doneButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// update chore status to done
					try {
						ChoreDAL.updateChoreStatus(chore.getId(), CHORE_STATUS.STATUS_DONE);
						String sender = RoommateDAL.getRoomateUsername();
						//MessagesToServer.notifyRoomates(roommates, "DONE", chore.getId());
						NotificationsDAL.notifyChoreDone(chore, sender, roommates);
						
					} catch (FailedToUpdateStatusException e) {
						// TODO (shani) decide what to do
					}
					getActivity().finish();
				}
			});
		 }
		 if(suggestButton != null){
			 suggestButton.setOnClickListener(new OnClickListener() {

				 @Override
				 public void onClick(View v) {
					 //start alert dialog to choose the roommate.
					 
					 openChoreSuggestionDialog(chore, roommates);
				 }
			 });

		 }
		 if(StealButton != null){
			 StealButton.setOnClickListener(new OnClickListener() {

				 @Override
				 public void onClick(View v) {
					 
					 try {
						String oldOwner = chore.getAssignedTo();// This line should be before the stealing.
						ChoreDAL.stealChore(chore.getId(), curUser);
						List<String> list = new ArrayList<String>();
						list.add(oldOwner);
						//MessagesToServer.notifyRoomates(list , "STEAL", chore.getId());
						String sender = RoommateDAL.getRoomateUsername();
						NotificationsDAL.notifySuggestStealChore(chore, sender, roommates);
					} catch (UserNotLoggedInException e) {
						LoginActivity.OpenLoginScreen(getActivity(), false);
						e.printStackTrace();
					} catch (DataNotFoundException e) {
						// TODO decide what to do
					}
					 getActivity().finish();
				 }
			 });
		 }
	 }
	 
	 
	 /*
	  * chore id - the id of the chore object to suggest.
	  */
	 private void openChoreSuggestionDialog(final Chore chore, final List<String> roommatesList) {
		 				
		 final String[] roommates = roommatesList.toArray(new String[0]);
		 
		final List<String> selectedItems = new ArrayList<String>();

		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); 

		 builder.setTitle(getResources().getString(R.string.chore_suggestion_dialog_title))

		           .setMultiChoiceItems((String[])roommates, null, new DialogInterface.OnMultiChoiceClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int position,
		                       boolean isChecked) {
		                   if (isChecked) {
		                       // add if item is not in the list
		                       selectedItems.add(roommates[position]);
		                   } else if (selectedItems.contains(roommates[position])) {
		                       // remove if item is in the list and isn't checked
		                       selectedItems.remove(roommates[position]);
		                   }
		               }
		           })
		
		           .setPositiveButton(R.string.chore_card_suggest_chore_suggest_button, new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		            	   
		            	   String sender = RoommateDAL.getRoomateUsername();
							NotificationsDAL.notifySuggestChore(chore, sender, selectedItems);
							 getActivity().finish();
		               }
		           })
		           .setNegativeButton(R.string.chore_card_suggest_chore_cancel_button, new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		                   getActivity().finish();
		                   return;
		               }
		           });
		 builder.show();
		
	}

	 //gets the chore type to display in the card
	 private String getChoreType(String name) {

		 String[] chores = getResources().getStringArray(R.array.chore_names_array);
		 
		 if(name.equals(chores[0])){//Cook dinner
			 return getResources().getString(R.string.chore_type_cook_dinner);
		 }
		 else if(name.equals(chores[1])){//Dish washing
			 return getResources().getString(R.string.chore_type_dish_washing);
		 }
		 else if(name.equals(chores[2])){//Do the laundry
			 return getResources().getString(R.string.chore_type_do_the_laundry);
		 }
		 else if(name.equals(chores[3])){//Dusting
			 return getResources().getString(R.string.chore_type_dusting);
		 }
		 else if(name.equals(chores[4])){//Floor sweeping
			 return getResources().getString(R.string.chore_type_floor_sweeping);
		 }
		 else if(name.equals(chores[5])){//Floor washing
			 return getResources().getString(R.string.chore_type_floor_washing);
		 }
		 else if(name.equals(chores[6])){//Grocery shopping
			 return getResources().getString(R.string.chore_type_grocery_shopping);
		 }
		 else if(name.equals(chores[7])){//Take out the trash
			 return getResources().getString(R.string.chore_type_take_out_trash);
		 }
		 else if(name.equals(chores[8])){//Toilet cleaning
			 return getResources().getString(R.string.chore_type_toilet_cleaning);
		 }
		 else if(name.equals(chores[9])){//walk the dog
			 return getResources().getString(R.string.chore_type_walk_dog);
		 }
		 else{
			 Log.e("error", "chore name doesn't exist");
			 return null;
		 }
	 }

}

