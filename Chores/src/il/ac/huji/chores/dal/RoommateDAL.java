package il.ac.huji.chores.dal;

import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import org.json.JSONArray;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;

import com.parse.*;

public class RoommateDAL {

	public static String Login(String username, String password) {
		try {
			ParseUser.logIn(username, password);
			return ParseUser.getCurrentUser().getObjectId();
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getApartmentID() throws UserNotLoggedInException{
		ParseUser currentUser = ParseUser.getCurrentUser();
		if(currentUser==null)
			throw new UserNotLoggedInException("User is not logged in");
		return (String) currentUser.get("apartmentID");
	}
	
	public static String createRoommateUser(String username, String password,
			String email) {

		ParseUser user = new ParseUser();
		user.setUsername(username);
		user.setPassword(password);
		if (email != null)
			user.setEmail(email);
		try {
			user.signUp();
			return user.getObjectId();
		} catch (ParseException e) {
			Log.e("createRoommateUser", e.toString());
			return null;
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
