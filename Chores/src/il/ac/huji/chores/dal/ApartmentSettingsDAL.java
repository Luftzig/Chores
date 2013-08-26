package il.ac.huji.chores.dal;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import il.ac.huji.chores.Settings;
import il.ac.huji.chores.exceptions.FailedToGetApartmentSettings;
import il.ac.huji.chores.exceptions.FailedToUpdateSettingsException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

public class ApartmentSettingsDAL{


	public Settings getSettings() throws UserNotLoggedInException, FailedToGetApartmentSettings{
		String apartmentID = RoommateDAL.getApartmentID();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Settings");
		query.whereEqualTo("apartment", apartmentID);
		ParseObject result;
		try {
			result = query.find().get(0);
		} catch (ParseException e) {
			throw new FailedToGetApartmentSettings(e.toString());
		}
		return convertObjectToSettings(result);
	}
	
	
	public static void updateSettings(Settings settings) throws UserNotLoggedInException, FailedToUpdateSettingsException{
		String apartmentID = RoommateDAL.getApartmentID();
		ParseObject parseSettings = new ParseObject("Settings");
		parseSettings.put("apartment", apartmentID);
		parseSettings.put("newChoresHasBeenDivided", settings.notifications.newChoresHasBeenDivided);
		parseSettings.put("roommateFinishedChore", settings.notifications.roommateFinishedChore);
		parseSettings.put("roommateMissedChore", settings.notifications.roommateMissedChore);
		parseSettings.put("roommateStoleMyChore", settings.notifications.roommateStoleMyChore);
		parseSettings.put("disableRemindersAboutUpcomingChores", settings.chores.disableRemindersAboutUpcomingChores);
		parseSettings.put("forbidRoommatesFromTakingMyChores", settings.chores.forbidRoommatesFromTakingMyChores);
		parseSettings.put("reminderMinutes", settings.reminders.min);
		parseSettings.put("remind", settings.reminders.remind);
		try {
			parseSettings.save();
		} catch (ParseException e) {
			throw new FailedToUpdateSettingsException(e.toString());
		}
	}
	private Settings convertObjectToSettings(ParseObject obj){
		Settings settings = new Settings();
		settings.notifications.newChoresHasBeenDivided =obj.getBoolean("newChoresHasBeenDivided");
		settings.notifications.roommateFinishedChore = obj.getBoolean("roommateFinishedChore");
		settings.notifications.roommateMissedChore =obj.getBoolean("roommateMissedChore");
		settings.notifications.roommateStoleMyChore=obj.getBoolean("roommateStoleMyChore");
		
		 settings.chores.disableRemindersAboutUpcomingChores=obj.getBoolean("disableRemindersAboutUpcomingChores");
		settings.chores.forbidRoommatesFromTakingMyChores= obj.getBoolean("forbidRoommatesFromTakingMyChores");
		settings.reminders.min=obj.getInt("reminderMinutes");
		settings.reminders.remind = obj.getBoolean("remind");
		return settings;
	}
	
	

}
