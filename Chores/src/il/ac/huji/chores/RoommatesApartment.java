package il.ac.huji.chores;

import java.util.List;

import com.parse.ParseObject;

public class RoommatesApartment implements Apartment {

    private List<String>    _roommates;
    private String          _id;
    private String          _name;

    @Override
    public String createApartment() {
        ParseObject apartment = new ParseObject("Apartment");
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
    public String get_name() {
        return _name;
    }

    /**
     * @param _name the _name to set
     */
    public void set_name(String _name) {
        this._name = _name;
    }

}
