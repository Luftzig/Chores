package il.ac.huji.chores.dal;

import java.util.ArrayList;

import java.util.List;
import il.ac.huji.chores.RoommatesApartment;

import android.content.Context;
import android.util.Log;

import com.parse.*;

public class ApartmentDAL {

	public static String createApartment(RoommatesApartment apt) {
		System.out.println("apartment = " + apt.getName() + " roommates = "
				+ apt.get_roommates());
		ParseObject apartment = new ParseObject("Apartment");
		apartment.put("apartmentName", apt.getName());
		if (apt.get_roommates() != null)
			apartment.put("Roomates", apt.get_roommates());
		apartment.saveInBackground();

		return getApartmentID(apt.getName());
	}

	public static String getApartmentID(String apartmentName) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Apartment");
		query.whereEqualTo("apartmentName", apartmentName);
		List<ParseObject> apartmentsList = new ArrayList<ParseObject>();
		try {
			apartmentsList = query.find();
		} catch (ParseException e) {
			System.out.println("parse exception"+e);
			return null;
		}
		
		if (apartmentsList.size() > 0)
			return apartmentsList.get(0).getObjectId();
		else
			return null;
	}

	public static List<String> getApartmentRoomates(String apartmentID) {
		List<ParseObject> apartmentsList = new ArrayList<ParseObject>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Apartment");
		query.whereEqualTo("objectId", apartmentID);
		try {
			apartmentsList = query.find();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		if(apartmentsList.size() >0)
		return apartmentsList.get(0).getList("Roommates");
		else
			return null;
	}


}
