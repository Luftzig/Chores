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
			return apartmentsList.get(0).getString("apartmentName");
		else
			return null;
	}

	public List<String> getApartmentRoomates(String apartmentID) {
		List<ParseObject> apartmentsList = new ArrayList<ParseObject>();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Apartment");
		query.whereEqualTo("_id", apartmentID);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> apartmentsList, ParseException e) {
				if (e == null) {
					System.out.println( "Retrieved "
							+ apartmentsList.size() + " results");
				} else {
					System.out.println( "Error: " + e.getMessage());
				}
			}
		});
		return apartmentsList.get(0).getList("Roommates");
	}


}
