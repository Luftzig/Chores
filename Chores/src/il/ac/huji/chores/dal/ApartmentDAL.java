package il.ac.huji.chores.dal;

import android.util.Log;
import com.parse.*;

import il.ac.huji.chores.RoommatesApartment;
import il.ac.huji.chores.exceptions.ApartmentAlreadyExistsException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import java.util.ArrayList;
import java.util.List;

public class ApartmentDAL {

	public static String createApartment(RoommatesApartment apt)
			throws ApartmentAlreadyExistsException, UserNotLoggedInException,
			ParseException {
		ParseUser curreentUser = ParseUser.getCurrentUser();
		if (curreentUser == null) {
			throw new UserNotLoggedInException("User not logged in");
		}
		if (getApartmentID(apt.getName()) != null) {
			throw new ApartmentAlreadyExistsException("Apartment "
					+ apt.getName() + "already exists");
		}
		ParseObject apartment = new ParseObject("Apartment");
		ParseACL permissions = new ParseACL();
		permissions.setPublicWriteAccess(true);
		permissions.setPublicReadAccess(true);
		apartment.setACL(permissions);
		apartment.put("apartmentName", apt.getName());
		apartment.add("Roommates", curreentUser.getUsername());
		apartment.put("divisionDay", apt.getDivisionDay());
		apartment.put("frequency", apt.getDivisionFrequency());
		apartment.put("lastDivision", 0);
		apartment.save();
		return apartment.getObjectId();
	}

	public static String getApartmentID(String apartmentName)
			throws ParseException {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Apartment");
		query.whereEqualTo("apartmentName", apartmentName);
		List<ParseObject> apartmentsList = new ArrayList<ParseObject>();
		apartmentsList = query.find();
		if (apartmentsList.size() > 0) {
			return apartmentsList.get(0).getObjectId();
		} else {
			return null;
		}
	}

	/**
	 * @param apartmentID
	 * @return a list of roommates usernames of the the requested apartment
	 * @throws ParseException 
	 */
	public static List<String> getApartmentRoommates(String apartmentID) throws ParseException {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Apartment");
		ParseObject apartment;
		apartment = query.get(apartmentID);
		if (apartment == null) {
			return new ArrayList<String>();
		}
		return apartment.getList("Roommates");
	}

	public static void addRoommateToApartment(String apartmentID)
			throws UserNotLoggedInException, ParseException {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			throw new UserNotLoggedInException("User not logged in");
		}
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Apartment");
		ParseObject apartment;
		String roommate = currentUser.getUsername();
		apartment = query.get(apartmentID);
		apartment.add("Roommates", currentUser.getUsername());
		apartment.save();
		RoommateDAL.addApartmentToRoommate(apartmentID);
	}

}
