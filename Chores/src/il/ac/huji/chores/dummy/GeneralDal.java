package il.ac.huji.chores.dummy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import il.ac.huji.chores.Apartment;
import il.ac.huji.chores.Roommate;
import il.ac.huji.chores.RoommatesApartment;
import il.ac.huji.chores.dal.ApartmentDAL;
import il.ac.huji.chores.dal.RoommateDAL;
import il.ac.huji.chores.exceptions.ApartmentAlreadyExistsException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

public class GeneralDal {

	static public String getUserName()
	{
		return "baba";
	}
	
	static public void addDummyApartments(){
		
		try{
		String aptId;
		////////////////////////////////////////////////
		
		RoommatesApartment apt = new RoommatesApartment();
		apt.setName("apt4");
		apt.setDivisionDay("Sunday");
		apt.setDivisionFrequency("Once a Week");
		List<String> roommates = new ArrayList<String>();
		roommates.add("AAA");
		roommates.add("BBB");
		roommates.add("CCC");
		apt.setLastDivision(null);
		apt.setRoommates(roommates);
		try {
			aptId = createApartment_dummy(apt);
		} catch (ApartmentAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (UserNotLoggedInException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
//		RoommateDAL.createRoommateUser("AAA", "AAA");
//		RoommateDAL.createRoommateUser("BBB", "BBB");
//		RoommateDAL.createRoommateUser("CCC", "CCC");
		//ApartmentDAL.addRoommateToApartment(aptId);


		///////////////////////////////////////////////
		RoommatesApartment apt2 = new RoommatesApartment();
		apt2.setName("apt5");
		apt2.setDivisionDay("Sunday");
		apt2.setDivisionFrequency("Every Two Weeks");
		List<String> roommates2 = new ArrayList<String>();
		roommates.add("AAA2");
		roommates.add("BBB2");
		roommates.add("CCC2");
		apt2.setRoommates(roommates2);
		apt2.setLastDivision(new Date(2013-1900, 7, 31));
		Log.e("last division date of apt2&&&&&&&&&&&", new Date(2013-1900, 7, 31).toGMTString());
		Log.e("last division date of apt2-long&&&&&&&&&&&", "" + new Date(2013-1900, 7, 31).getTime());
		try {
			aptId = createApartment_dummy(apt2);
		} catch (ApartmentAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (UserNotLoggedInException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
//		RoommateDAL.createRoommateUser("AAA2", "AAA2");
//		RoommateDAL.createRoommateUser("BBB2", "BBB2");
//		RoommateDAL.createRoommateUser("CCC2", "CCC2");
		//ApartmentDAL.addRoommateToApartment(aptId);
		////////////////////////////////////////////////
		RoommatesApartment apt3 = new RoommatesApartment();
		apt3.setName("apt6");
		apt3.setDivisionDay("Monday");
		apt3.setDivisionFrequency("Once a week");
		List<String> roommates3 = new ArrayList<String>();
		roommates3.add("AAA3");
		roommates3.add("BBB3");
		roommates3.add("CCC3");
		apt.setRoommates(roommates3);
		apt.setLastDivision(null);
		try {
			aptId = createApartment_dummy(apt3);
		} catch (ApartmentAlreadyExistsException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return;
		} catch (UserNotLoggedInException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return;
		}
//		RoommateDAL.createRoommateUser("AAA3", "AAA3");
//		RoommateDAL.createRoommateUser("BBB3", "BBB3");
//		RoommateDAL.createRoommateUser("CCC3", "CCC3");
		//ApartmentDAL.addRoommateToApartment(aptId);
		///////////////////////////////////////////////
		
		}catch(Exception e){
			Log.e("*********", e.getMessage());
		}
	}
	
	public static String createApartment_dummy(RoommatesApartment apt)
			throws ApartmentAlreadyExistsException, UserNotLoggedInException {
		ParseUser curreentUser = ParseUser.getCurrentUser();	
		if(curreentUser == null){
			throw new UserNotLoggedInException("User not logged in");
		}
		if (ApartmentDAL.getApartmentID(apt.getName()) != null) {
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
		
		if(apt.getLastDivision() != null){
			apartment.put("lastDivision", apt.getLastDivision().getTime());	
		}
		else{
			apartment.put("lastDivision", 0);	
		}
		try {
			apartment.save();
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		} 
		
		if(!ApartmentDAL.addRoommateToApartment(ApartmentDAL.getApartmentID(apt.getName()))){
			//TODO Do something ?
		}

		return apartment.getObjectId();
	}
	
	public static boolean addRoommateToApartment_dal(String apartmentID, List<String> usernames)
			throws UserNotLoggedInException {

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Apartment");
		ParseObject apartment;
		try {
	
			apartment = query.get(apartmentID);
			for(int i =0; i< usernames.size(); i++){
				apartment.add("Roommates", usernames.get(i));
			}
			apartment.save();
			//RoommateDAL.addApartmentToRoommate(apartmentID);
		} catch (ParseException e) {
			Log.e("addRoommateToApatment", e.toString());
			return false;
		}
		return true;
	}

}
