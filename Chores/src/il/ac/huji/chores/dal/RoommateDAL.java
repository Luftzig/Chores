package il.ac.huji.chores.dal;

import android.util.Log;
import com.parse.*;
import il.ac.huji.chores.Roommate;
import il.ac.huji.chores.exceptions.FailedToGetChoreException;
import il.ac.huji.chores.exceptions.FailedToGetRoommateException;
import il.ac.huji.chores.exceptions.FailedToSaveOperationException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import javax.annotation.Nullable;
import java.util.List;

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

    @Nullable
    public static Roommate getRoommateByName(String roommateName) throws FailedToGetRoommateException{
		ParseQuery<ParseUser> query =ParseUser.getQuery().whereEqualTo("username", roommateName);
		try {
            List<ParseUser> parseUsers = query.find();
            if (parseUsers.size() == 0) {
                return null;
            }
            ParseObject obj = (ParseObject) parseUsers.get(0);
            Roommate roommate = convertObjToRoommate(obj);
            return roommate;
        } catch (ParseException e) {
			throw new FailedToGetRoommateException(e.getMessage());
		}
		
	}

    @Nullable
    public static Roommate getRoommateById(String id) throws FailedToGetRoommateException{
        ParseQuery<ParseUser> query =ParseUser.getQuery().whereEqualTo("objectId", id);
        try {
            List<ParseUser> parseUsers = query.find();
            if (parseUsers.size() == 0) {
                return null;
            }
            ParseObject obj = (ParseObject) parseUsers.get(0);
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
	
	public static void initRoommateProperties(String phoneNumber) throws UserNotLoggedInException{
		ParseUser roommate = ParseUser.getCurrentUser();
		roommate.put("coinsCollected", 0);
        roommate.put("phoneNumber", sanitizePhoneNumber(phoneNumber));
        try {
			roommate.save();
		} catch (ParseException e) {
			throw new UserNotLoggedInException(e.getMessage());
		}
		
	}

    private static String sanitizePhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("[^0-9]", "");
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
		roommate.setId(obj.getObjectId());
		roommate.setUsername(obj.getString("username"));
		roommate.setCoinsCollected(obj.getInt("coinsCollected"));
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

    public static String createRoommateUser(String username, String password, String phoneNumber)
            throws ParseException, UserNotLoggedInException {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        try {
            user.signUp();
            initRoommateProperties(phoneNumber);
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put("owner", ParseUser.getCurrentUser());
            installation.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d("createRoommateUser", "Installation created");
                    } else {
                        Log.w("createRoommateUser", "Installation failed with " + e.getMessage());
                    }
                }
            });
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
