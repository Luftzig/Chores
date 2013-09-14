package il.ac.huji.chores.server.parse;

import il.ac.huji.chores.Apartment;
import il.ac.huji.chores.Chore;
import il.ac.huji.chores.ChoreInfo;
import il.ac.huji.chores.Roommate;
import il.ac.huji.chores.RoommatesApartment;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

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
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public List<Roommate> getApartmentRoommates(String apartmentId) throws ClientProtocolException, IOException;

    /**
     * @return all apartments on Parse.
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public List<RoommatesApartment> getApartmentList() throws ClientProtocolException, IOException;
    
    /**
     * @return all apartments on Parse, whose division day is day.
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public List<RoommatesApartment> getTodaysApartmentList(String day) throws ClientProtocolException, IOException;

    /**
     *
     * @return all apartment IDs in Parse.
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public List<String> getApartmentIds() throws ClientProtocolException, IOException;

    public List<ChoreInfo> getApartmentChoreInfos(String apartmentId) throws ClientProtocolException, IOException;

    public void sendChores(String apartmentId, List<Chore> assignedChores);
}
