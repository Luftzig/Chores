package il.ac.huji.chores;

import java.util.List;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class RoommatesApartment implements Apartment {

    private List<String>    _roommates;
    private String          _id;
    private String          _name;

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
    public List<String> get_roommates() {
        return _roommates;
    }

    /**
     * @param _roommates the _roommates to set
     */
    public void set_roommates(List<String> _roommates) {
        this._roommates = _roommates;
    }

    /**
     * @return the _id
     */
    public String get_id() {
        return _id;
    }

    /**
     * @return the _name
     */
    public String getName() {
        return _name;
    }

    /**
     * @param _name the _name to set
     */
    public void setName(String name) {
        this._name = name;
    }

}
