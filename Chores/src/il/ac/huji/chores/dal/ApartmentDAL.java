package il.ac.huji.chores.dal;
import java.util.ArrayList;

import java.util.List;
import il.ac.huji.chores.RoommatesApartment;
import com.parse.*;

public class ApartmentDAL extends BasicDAL {
	
	public static RoommatesApartment apartment;
	public static String CreateApartment(RoommatesApartment apt){
		apartment = apt;
		final ParseObject apartment = new ParseObject("Apartment");
		apartment.add("apartmentName",apt.getName());
		//apartment.add("apartmentCreator",apt.getApartmentCreator());
		ArrayList<String> roommates = new ArrayList<String>();
		roommates.add(roommateID);
		apartment.add("Roomates",roommates);
		apartment.saveInBackground(new SaveCallback() {
			   public void done(ParseException e) {
				     if (e == null) {
				    	 apartmentID = apartment.getObjectId();
				    	 //TODO :save in preferences
				     }
				   }
				 });
			return apartmentID;
		}

	public static List<String> getApartmentRoomates(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Apartment");
		query.getInBackground(apartmentID, new GetCallback<ParseObject>() {
			  @SuppressWarnings("unchecked")
			public void done(ParseObject apartmentObj, ParseException e) {
			    if (e == null) {
			    	apartment.set_roommates((List<String>)apartmentObj.get("Roomates"));
			    } 
			  }
			});
		return apartment.get_roommates();
	}
	

}
