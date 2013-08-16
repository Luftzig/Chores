package il.ac.huji.chores.dal;

import org.json.JSONArray;

import com.parse.GetCallback;

import com.parse.*;


public class RoommateDAL {

	private static String _username;
	public static void setUserName(String username) {
		_username = username;
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Roomate");
		query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
		  public void done(ParseObject roomate, ParseException e) {
		    if (e == null) {
		    	roomate.put("username", _username);
		        roomate.saveInBackground();
		    }
		  }
		});
	}

	public String getUserName() {
		return _username;
	}

	public boolean createRoomateUser(String username,String apartmentID) {

		addNewRoomate(username,apartmentID);
		addRoomateToApatment(username,apartmentID);
		return true;
	}

	private void addNewRoomate(String username,String apartmentID) {

		ParseObject roomate = new ParseObject("Roomate");
		roomate.put("username", username);
		roomate.put("apartmentID", apartmentID);
		roomate.put("roomateID", ParseUser.getCurrentUser());
		roomate.setACL(new ParseACL(ParseUser.getCurrentUser()));
		roomate.saveInBackground();
	}

	private void addRoomateToApatment(String roommateID,String apartmentID){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Apartment");
		final String finalRoommateID = roommateID;
		// Retrieve the object by id
		query.getInBackground(apartmentID, new GetCallback<ParseObject>() {
		  public void done(ParseObject apartment, ParseException e) {
		    if (e == null) {
		    	JSONArray roomates = (JSONArray)apartment.get("Rommates");
		    	roomates.put(finalRoommateID);
		    	apartment.put("Roomates", roomates);
		        apartment.saveInBackground();
		    }
		  }
		});
	}
}
