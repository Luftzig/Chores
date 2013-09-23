package il.ac.huji.chores;

import android.util.Log;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;

public class RoommatesApartment implements Apartment {

    private List<String>        _roommates;
    private String              _id;
    private String              _name;
    private String              divisionDay;
    private String              divisionFrequency;
	private Date _lastDivision; //date of the last time the chores were divided


    @Override
    public String createApartment() {
        ParseObject apartment = new ParseObject("Apartment");
        apartment.put("name", getName());
        Log.d("RoommatesApartment", "Created parse object");
        apartment.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("RoommatesApartment callback", "saved successfully");
                } else {
                    Log.d("RoommatesApartment callback", "Save failed, reason:\n" + e.getMessage());
                }
            }
        });
        Log.d("RoommatesApartment", "Save in background called");
        return null;
    }

    /**
     * @return the _roommates
     */
    public List<String> getRoommates() {
        return _roommates;
    }

    /**
     * @param roommates the _roommates to set
     */
    public void setRoommates(List<String> roommates) {
        this._roommates = roommates;
    }

    /**
     * @return the _id
     */
    public String getId() {
        return _id;
    }
    
    public void setId(String id){
    	_id = id;
    }

    /**
     * @return the _name
     */
    public String getName() {
        return _name;
    }

    /**
     * @param name the _name to set
     */
    public void setName(String name) {
        this._name = name;
    }

    /**
     * @return the divisionDay
     */
    public String getDivisionDay() {
        return divisionDay;
    }

    /**
     * @param divisionDay the divisionDay to set
     */
    public void setDivisionDay(String divisionDay) {
        this.divisionDay = divisionDay;
    }

    /**
     * @return the divisionFrequency
     */
    public String getDivisionFrequency() {
        return divisionFrequency;
    }

    /**
     * @param divisionFrequency the divisionFrequency to set
     */
    public void setDivisionFrequency(
            String divisionFrequency) {
        this.divisionFrequency = divisionFrequency;
    }

    public String toString() {
        return "RoommatesApartment: " + _id + ", name " + _name + ", " 
            + divisionDay + ", " + divisionFrequency;
    }
    
	public Date getLastDivision(){
		return _lastDivision;
	}
	
	public void setLastDivision(Date lastDivision){
		_lastDivision = lastDivision;
	}
}
