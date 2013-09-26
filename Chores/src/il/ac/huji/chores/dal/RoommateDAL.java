package il.ac.huji.chores.dal;

import android.util.Log;
import com.parse.*;

import il.ac.huji.chores.Roommate;
import il.ac.huji.chores.exceptions.FailedToGetChoreException;
import il.ac.huji.chores.exceptions.FailedToGetRoommateException;
import il.ac.huji.chores.exceptions.FailedToSaveOperationException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import java.util.List;

//import javax.annotation.Nullable;

public class RoommateDAL {

	public static String Login(String username, String password)
			throws ParseException {
		ParseUser.logIn(username, password);
		return ParseUser.getCurrentUser().getObjectId();
	}

	public static String getRoomateUsername() {
		return ParseUser.getCurrentUser().getUsername();
	}

	public static boolean isUserLoggedIn() {
		if (ParseUser.getCurrentUser() == null) {
			return false;
		}
		return true;
	}

	/**
	 * @param roommateName
	 * @return Roommate object for given roommate username
	 * @throws FailedToGetRoommateException
	 *             if user name does not exist or data is malformed.
	 */
	public static Roommate getRoommateByName(String roommateName)
			throws FailedToGetRoommateException {
        if (roommateName == null || roommateName.isEmpty()) {
            return null;
        }
        ParseObject obj = getParseUserByName(roommateName);
        if (obj == null)
            return null;
        Roommate roommate = convertObjToRoommate(obj);
        return roommate;
    }

	/**
	 * @param roommateName
	 * @return ParseUser object for given roommate username
	 * @throws FailedToGetRoommateException
	 *             if user name does not exist or data is malformed.
	 */
	private static ParseUser getParseUserByName(String roommateName)
			throws FailedToGetRoommateException {

		ParseQuery<ParseUser> query = ParseUser.getQuery().whereEqualTo(
				"username", roommateName);
		try {
			List<ParseUser> parseUsers = query.find();
			if (parseUsers.size() == 0) {
				return null;
			}
			ParseUser userObj = (ParseUser) parseUsers.get(0);
			return userObj;

		} catch (ParseException e) {
			throw new FailedToGetRoommateException(e.getMessage());
		}
	}

	// @Nullable
	public static Roommate getRoommateById(String id) throws ParseException {
		ParseQuery<ParseUser> query = ParseUser.getQuery().whereEqualTo(
				"objectId", id);
		List<ParseUser> parseUsers = query.find();
		if (parseUsers.size() == 0) {
			return null;
		}
		ParseObject obj = (ParseObject) parseUsers.get(0);
		Roommate roommate = convertObjToRoommate(obj);
		return roommate;
	}

	public static void initRoommateProperties(String phoneNumber)
			throws UserNotLoggedInException {
		ParseUser roommate = ParseUser.getCurrentUser();
		roommate.put("phoneNumber", sanitizePhoneNumber(phoneNumber));
		try {
			roommate.save();
			CoinsDAL.createDefaultCoinsForRoommate();
		} catch (ParseException e) {
			throw new UserNotLoggedInException(e.getMessage());
		}

	}

	private static String sanitizePhoneNumber(String phoneNumber) {
		return phoneNumber.replaceAll("[^0-9]", "");
	}

	public static int getRoommateDebtFromChores(String roommate)
			throws FailedToGetChoreException {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Chores")
				.whereEqualTo("assignedTo", roommate)
				.whereEqualTo("status", "STATUS_FUTURE");
		try {
			List<ParseObject> results = query.find();
			int debt = 0;
			for (ParseObject obj : results) {
				debt += obj.getInt("coins");
			}
			return debt;
		} catch (ParseException e) {
			throw new FailedToGetChoreException(e.getMessage());
		}
	}

	private static Roommate convertObjToRoommate(ParseObject obj) {
		Roommate roommate = new Roommate();
		roommate.setId(obj.getObjectId());
		roommate.setUsername(obj.getString("username"));
		return roommate;

	}

	public static String getApartmentID() throws UserNotLoggedInException {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null)
			throw new UserNotLoggedInException("User is not logged in");
		return (String) currentUser.get("apartmentID");
	}

	public static String getUserID() throws UserNotLoggedInException {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null)
			throw new UserNotLoggedInException("User is not logged in");
		return (String) currentUser.getObjectId();

	}

	public static String createRoommateUser(String username, String password,
			String phoneNumber) throws ParseException, UserNotLoggedInException {
		ParseUser user = new ParseUser();
		user.setUsername(username);
		user.setPassword(password);
		user.signUp();
		initRoommateProperties(phoneNumber);
		ParseInstallation installation = ParseInstallation
				.getCurrentInstallation();
		installation.put("userId", user.getObjectId());
		installation.put("username", username);
		installation.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Log.d("createRoommateUser", "Installation created");
				} else {
					Log.w("createRoommateUser",
							"Installation failed with " + e.getMessage());
				}
			}
		});
		return user.getObjectId();

	}

	public static boolean addApartmentToRoommate(String apartmentID)
			throws UserNotLoggedInException, ParseException {

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			throw new UserNotLoggedInException("User not logged in");
		}
		currentUser.put("apartmentID", apartmentID);
		currentUser.save();
		return false;
	}

}
