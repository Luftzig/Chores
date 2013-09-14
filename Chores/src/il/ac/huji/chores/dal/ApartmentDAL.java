package il.ac.huji.chores.dal;

import java.util.ArrayList;
import java.util.TooManyListenersException;

import java.util.List;

import org.json.JSONArray;

import il.ac.huji.chores.Chore;
import il.ac.huji.chores.RoommatesApartment;

import android.content.Context;
import android.util.Log;
import il.ac.huji.chores.exceptions.*;

import com.parse.*;

public class ApartmentDAL {

	public static String createApartment(RoommatesApartment apt)
			throws ApartmentAlreadyExistsException, UserNotLoggedInException {
		ParseUser curreentUser = ParseUser.getCurrentUser();
		if(curreentUser == null){
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
		apartment.add("Roommates", curreentUser.getObjectId());
		apartment.put("divisionDay", apt.getDivisionDay());
		apartment.put("frequency", apt.getDivisionFrequency());
		apartment.put("lastDivision", 0);		
		try {
			apartment.save();
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		} 
		
		if(!addRoommateToApartment(ApartmentDAL.getApartmentID(apt.getName()))){
			//TODO Do something ?
		}

		return apartment.getObjectId();
	}
	public static boolean inviteRoommate(String name,String number, String email){
		return false;
	}
	public static String getApartmentID(String apartmentName) {

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Apartment");

		query.whereEqualTo("apartmentName", apartmentName);

		List<ParseObject> apartmentsList = new ArrayList<ParseObject>();
		try {
			apartmentsList = query.find();
		} catch (ParseException e) {
			Log.d("getApartmentID", "parse exception" + e);
			return null;
		}

		if (apartmentsList.size() > 0)
			return apartmentsList.get(0).getObjectId();
		else
			return null;
	}

	public static List<String> getApartmentRoommates(String apartmentID) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Apartment");
		ParseObject apartment;
		try {
			apartment = query.get(apartmentID);
			return apartment.getList("Roommates");
		} catch (ParseException e) {
			Log.e("getApartmentRoommates",e.toString());
			return null;
		}
	}

	public static boolean addRoommateToApartment(String apartmentID)
			throws UserNotLoggedInException {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null){
			throw new UserNotLoggedInException("User not logged in");
		}
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Apartment");
		ParseObject apartment;
		try {
			String roommate = currentUser.getUsername();
			apartment = query.get(apartmentID);
			apartment.add("Roommates", currentUser.getUsername());
			apartment.save();
			RoommateDAL.addApartmentToRoommate(apartmentID);
		} catch (ParseException e) {
			Log.e("addRoommateToApatment", e.toString());
			return false;
		}
		return true;
	}

}
