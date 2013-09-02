package il.ac.huji.chores;

import il.ac.huji.chores.dal.ApartmentSettingsDAL;
import il.ac.huji.chores.exceptions.FailedToGetApartmentSettings;
import il.ac.huji.chores.exceptions.FailedToUpdateSettingsException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class SettingsFragment extends Fragment {

	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 
	    View view = inflater.inflate(R.layout.fragment_settings, container, false);
	    return view;
	  }

	 public void onActivityCreated (Bundle savedInstanceState)
	 {
		 super.onActivityCreated(savedInstanceState);
		 
		 try{
		 
		 /** Read current settings and display them in the UI **/
		 Settings defaultSettings = ApartmentSettingsDAL.getSettings();
		 
		 //Notifications
		 final CheckBox roommateMissed_checkBox = (CheckBox)(getActivity().findViewById(R.id.NotificationsSettingRoomateMissedChoreCheckbox));
		 boolean check = defaultSettings.notifications.roommateMissedChore;
		 roommateMissed_checkBox.setChecked(check);
		 
		 final CheckBox roommateDone_checkBox = (CheckBox)(getActivity().findViewById(R.id.NotificationsSettingsRoomateFinishedChoreCheckbox));
		 check = defaultSettings.notifications.roommateFinishedChore;
		 roommateDone_checkBox.setChecked(check);

		 final CheckBox roommateStole_checkBox = (CheckBox)(getActivity().findViewById(R.id.NotificationsSettingsRoomateStoleChoreCheckbox));
		 check = defaultSettings.notifications.roommateStoleMyChore;
		 roommateStole_checkBox.setChecked(check);
		 
		 final CheckBox newChoresDivided_checkBox = (CheckBox)(getActivity().findViewById(R.id.NotificationsSettingsNewChoresCheckbox));
		 check = defaultSettings.notifications.roommateStoleMyChore;
		 newChoresDivided_checkBox.setChecked(check);
		 
		 //Chores
		 final CheckBox choreReminder_checkBox = (CheckBox)(getActivity().findViewById(R.id.ChoresSettingsDisableRemindersCheckbox));
		 check = defaultSettings.chores.disableRemindersAboutUpcomingChores;
		 choreReminder_checkBox.setChecked(check);

		 final CheckBox forbidStealing_checkBox = (CheckBox)(getActivity().findViewById(R.id.ChoresSettingsForbidCheckbox));
		 check = defaultSettings.chores.forbidRoommatesFromTakingMyChores;
		 forbidStealing_checkBox.setChecked(check);
	
		 //Reminders
		 
		 final EditText reminderTime_editText = (EditText)(getActivity().findViewById(R.id.RemindersHoursText));
		 reminderTime_editText.setText(Integer.toString(defaultSettings.reminders.hours));
		 
		 /** Buttons handling and sending settings to database **/
		 
		 final Button saveButton = (Button)(getActivity().findViewById(R.id.SettingsFragment_saveButton));
		 saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Get new settings and save in database
				Settings newSettings = new Settings();
				
				//Notifications
				newSettings.notifications = newSettings.new Notifications();
				newSettings.notifications.roommateMissedChore = roommateMissed_checkBox.isChecked();
				newSettings.notifications.roommateFinishedChore = roommateDone_checkBox.isChecked();
				newSettings.notifications.roommateStoleMyChore = roommateStole_checkBox.isChecked();
				
				//Chores
				newSettings.chores = newSettings.new Chores();
				newSettings.chores.disableRemindersAboutUpcomingChores = choreReminder_checkBox.isChecked();
				newSettings.chores.forbidRoommatesFromTakingMyChores = forbidStealing_checkBox.isChecked();
				
				//Reminders
				newSettings.reminders = newSettings.new Reminders();
				newSettings.reminders.hours = Integer.parseInt(reminderTime_editText.getText().toString());
				
				//Send to database
				try {
					ApartmentSettingsDAL.updateSettings(newSettings);
				} catch (UserNotLoggedInException e) {
					 //TODO(Shani) open login screen
				} catch (FailedToUpdateSettingsException e) {
					//TODO(shani) think what to do 
				}
			}
		});
		 
		 }catch(UserNotLoggedInException e){
			 //TODO(Shani) open login screen
		 }
		 catch(FailedToGetApartmentSettings e){
			//TODO(shani) think what to do 
		 }
	 }
}
