package il.ac.huji.chores.dal;

import java.util.ArrayList;

import java.util.List;

import org.json.JSONArray;

import il.ac.huji.chores.RoommatesApartment;

import android.content.Context;
import android.util.Log;
import il.ac.huji.chores.exceptions.*;

import com.parse.*;

public class ApartmentDAL {

	public static String createApartment(RoommatesApartment apt)
			throws ApartmentAlreadyExistsException {
		Log.d("createApartment", "apartment = " + apt.getName()
				+ " roommates = " + apt.get_roommates());
		if (getApartmentID(apt.getName()) != null) {
			throw new ApartmentAlreadyExistsException("Apartment "
					+ apt.getName() + "already exists");
		}
		ParseObject apartment = new ParseObject("Apartment");
		apartment.put("apartmentName", apt.getName());
		if (apt.get_roommates() != null)
			apartment.put("Roommates", apt.get_roommates());
		try {
			apartment.save();
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

		return getApartmentID(apt.getName());
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

	public static boolean addRoomateToApartment(String apartmentID)
			throws UserNotLoggedInException {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null){
			throw new UserNotLoggedInException("User not logged in");
		}
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Apartment");
		ParseObject apartment;
		try {
			apartment = query.get(apartmentID);
			JSONArray roomates = (JSONArray) apartment.get("Rommates");
			if (roomates == null) {
				roomates = new JSONArray();
			}
			roomates.put(currentUser.getObjectId());
			apartment.put("Roommates", roomates);
			apartment.save();
			RoommateDAL.addApartmentToRoommate(apartmentID);
		} catch (ParseException e) {
			Log.e("addRoomateToApatment", e.toString());
			return false;
		}
		return true;
	}

}
