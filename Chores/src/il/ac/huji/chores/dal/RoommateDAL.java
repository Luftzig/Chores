package il.ac.huji.chores.dal;

import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import android.util.Log;

import com.parse.ParseException;

import com.parse.*;

public class RoommateDAL {

	public static String Login(String username, String password) throws ParseException {
		try {
			ParseUser.logIn(username, password);
			return ParseUser.getCurrentUser().getObjectId();
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static String getApartmentID() throws UserNotLoggedInException{
		ParseUser currentUser = ParseUser.getCurrentUser();
		if(currentUser==null)
			throw new UserNotLoggedInException("User is not logged in");
		return (String) currentUser.get("apartmentID");
	}
	
	public static String getUserID() throws UserNotLoggedInException{
		ParseUser currentUser = ParseUser.getCurrentUser();
		if(currentUser==null)
			throw new UserNotLoggedInException("User is not logged in");
		return (String) currentUser.getObjectId();
		
	}
	
	public static String createRoommateUser(String username, String password) throws ParseException {

		ParseUser user = new ParseUser();
		user.setUsername(username);
		user.setPassword(password);

		try {
			user.signUp();
			return user.getObjectId();
		} catch (ParseException e) {
			Log.e("createRoommateUser", e.toString());
			throw e;
		}
	}

	public static boolean addApartmentToRoommate(String apartmentID)
			throws UserNotLoggedInException {

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			throw new UserNotLoggedInException("User not logged in");
		}
		try {
			currentUser.put("apartmentID", apartmentID);
			currentUser.save();
		} catch (ParseException e) {

			Log.e("addRoommateToApatment", e.toString());
			return false;
		}
		return false;
	}
}
