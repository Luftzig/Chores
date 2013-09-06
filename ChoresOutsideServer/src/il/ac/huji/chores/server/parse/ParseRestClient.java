package il.ac.huji.chores.server.parse;

import il.ac.huji.chores.Apartment;
import il.ac.huji.chores.Chore;
import il.ac.huji.chores.ChoreInfo;
import il.ac.huji.chores.Roommate;

import java.util.List;

/**
 * User: Yoav
 * Date: 06/09/13
 * Email: yoav.luft@gmail.com
 *
 * Defines access to the parse server.
 */
public interface ParseRestClient {

    /**
     * @param apartmentId apartment parse identification string
     * @return all roommates associated with this apartment
     */
    public List<Roommate> getApartmentRoommates(String apartmentId);

    /**
     * @return all apartments on Parse.
     */
    public List<Apartment> getApartmentList();

    /**
     *
     * @return all apartment IDs in Parse.
     */
    public List<String> getApartmentIds();

    public List<ChoreInfo> getApartmentChoreInfos(String apartmentId);

    public void sendChores(String apartmentId, List<Chore> assignedChores);
}
