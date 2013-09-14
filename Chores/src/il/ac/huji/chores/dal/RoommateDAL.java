package il.ac.huji.chores.dal;

import java.util.List;

import il.ac.huji.chores.Roommate;
import il.ac.huji.chores.exceptions.FailedToGetChoreException;
import il.ac.huji.chores.exceptions.FailedToGetRoommateException;
import il.ac.huji.chores.exceptions.FailedToSaveOperationException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
	public static String getRoomateUsername(){
		return ParseUser.getCurrentUser().getUsername();
	}
	public static Roommate getRoommate(String roommateName) throws FailedToGetRoommateException{
		
		ParseQuery<ParseUser> query =ParseUser.getQuery().whereEqualTo("username", roommateName);
		try {
			ParseObject obj =(ParseObject) query.find().get(0);
			Roommate roommate = convertObjToRoommate(obj);
			return roommate;
		} catch (ParseException e) {
			throw new FailedToGetRoommateException(e.getMessage());
		}
		
	}
	
	
	public static void increaseCoinsCollected(int coins) throws FailedToSaveOperationException{
		ParseUser currentUser = ParseUser.getCurrentUser();
		int currentCoins = currentUser.getInt("coins");
		currentCoins+=coins;
		currentUser.put("coins",currentCoins);
		try {
			currentUser.save();
		} catch (ParseException e) {
			throw new FailedToSaveOperationException(e.getMessage());
		}
		
	}
	public static int getRoommateCollectedCoins(String roommate) throws FailedToGetChoreException{
		ParseQuery<ParseObject> query= ParseQuery.getQuery("Chores").whereEqualTo("assignedTo", roommate).whereEqualTo("status","STATUS_DONE");
		try {
			List<ParseObject> results = query.find();
			int coinsCollected = 0;
			for(ParseObject obj : results){
				coinsCollected+=obj.getInt("coins");
			}
			return coinsCollected;
		} catch (ParseException e) {
			throw new FailedToGetChoreException(e.getMessage());
		}
	}
	
	public static void initRoommateCollectedCoins() throws UserNotLoggedInException{
		ParseUser roommate = ParseUser.getCurrentUser();
		roommate.put("coinsCollected", 0);
		try {
			roommate.save();
		} catch (ParseException e) {
			throw new UserNotLoggedInException(e.getMessage());
		}
		
	}
	
	public static int getRoommateDept(String roommate) throws FailedToGetChoreException{
		ParseQuery<ParseObject> query= ParseQuery.getQuery("Chores").whereEqualTo("assignedTo", roommate).whereEqualTo("status","STATUS_FUTURE");
		try {
			List<ParseObject> results = query.find();
			int dept = 0;
			for(ParseObject obj : results){
				dept+=obj.getInt("coins");
			}
			return dept;
		} catch (ParseException e) {
			throw new FailedToGetChoreException(e.getMessage());
		}
	}
	private static Roommate convertObjToRoommate(ParseObject obj){
		Roommate roommate = new Roommate();
		roommate.set_id(obj.getObjectId());
		roommate.set_username(obj.getString("username"));
		roommate.set_coinsCollected(obj.getInt("coinsCollected"));
		return roommate;
	
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
	
	public static String createRoommateUser(String username, String password) throws ParseException, UserNotLoggedInException {

		ParseUser user = new ParseUser();
		user.setUsername(username);
		user.setPassword(password);
		

		try {
			user.signUp();
			initRoommateCollectedCoins();
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
