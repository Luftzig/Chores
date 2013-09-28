package il.ac.huji.chores.server.parse;

import il.ac.huji.chores.Chore;
import il.ac.huji.chores.ChoreInfo;
import il.ac.huji.chores.Coins;
import il.ac.huji.chores.Roommate;
import il.ac.huji.chores.RoommatesApartment;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Date: 06/09/13
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
    
    /**
     * adds a list of apartment chores to the db. and also triggers a deadline passed check.
     * @param newChores
     * @throws ClientProtocolException
     * @throws IOException
     */
    public void addChores(List<Chore> newChores) throws ClientProtocolException, IOException;

    
    /**
     * updates the lastDivision field of the apartment in the db to date.
     * @param apartmentId - the id of the apartment
     * @param date - the date to update to.
     * @throws IOException 
     * @throws ClientProtocolException 
     */
	public void updateApartmentLastDivision(String apartmentId, Date date) throws ClientProtocolException, IOException;
	
	/**
	 * Sets debt to the user (instead of the old one)
	 * @param username - the username of the user to set new debt to
	 * @param debt - the new debt to set
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public void setRommateDebt(String username, int debt)throws ClientProtocolException, IOException;
	
	public Coins getRoommateCoins(String username) throws ClientProtocolException, IOException;
	
	public void updatePassedDeadlinesChores() throws ClientProtocolException, IOException;
	
	public void addChoreObj(Chore chore) throws ClientProtocolException, IOException;

}
