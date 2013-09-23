package il.ac.huji.chores.server;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import il.ac.huji.chores.Chore;
import il.ac.huji.chores.RoommatesApartment;
import il.ac.huji.chores.server.parse.ChoresRest;
import il.ac.huji.chores.server.parse.ParseRestClient;
import il.ac.huji.chores.server.parse.ParseRestClientImpl;
import org.apache.http.client.ClientProtocolException;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@DisallowConcurrentExecution
public class RoutineJob implements Job {
	
	final static String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		//Get all apartment infos which division day can be today
		ParseRestClient parse = new ParseRestClientImpl();
		Calendar todayCal = Calendar.getInstance();
		String day = days[todayCal.get(Calendar.DAY_OF_WEEK)-1];
		List<RoommatesApartment> apartments;
		try {
			apartments = parse.getTodaysApartmentList(day);
		} catch (IOException e1) {
			return; //try again later
		}
		
		//Filter only today's divisions
		 Predicate<RoommatesApartment> predicate = new Predicate<RoommatesApartment>() {
			 
			 public boolean apply(RoommatesApartment apartment){
				 
				 if(apartment == null){
					 return false;
				 }
				 
				 return IsDivisionTime(apartment);
			 }
		};
		List<RoommatesApartment> filteredApartments = new ArrayList<RoommatesApartment>(
                Collections2.filter(apartments, predicate));
		
		//Divide chores
		String aptID = null;
		for(int i=0; i< filteredApartments.size(); i++){
			try {
				if(filteredApartments.get(i).getRoommates().size() == 0){
					continue; //fake apartment
				}
				aptID = filteredApartments.get(i).getId();
				if(aptID != null){
					DivideChoresForApartment(aptID, todayCal.getTime());
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				//continue
			} catch (IOException e) {
				e.printStackTrace();
				//continue
			}
		}
	}


	//Returns true if the right division period has passed since the last division, false otherwise.
	private boolean IsDivisionTime(RoommatesApartment apartment){
		
		//create a calendar that holds today's date
		Calendar todayCal = Calendar.getInstance();
		//create a calendar that holds lad division + division period date
		Calendar cal = Calendar.getInstance();
		Date lastDivision = apartment.getLastDivision();
		cal.setTime(lastDivision);
		
		String frequency = apartment.getDivisionFrequency();
		
		if(lastDivision.getTime() == 0){// this is the first chores division of the apartment
			return true;
		}
		
		if(frequency.equals("Once a Week")){
			cal.add(Calendar.WEEK_OF_YEAR, 1);
		}
		else if(frequency.equals("Every Two Weeks")){
			cal.add(Calendar.WEEK_OF_YEAR, 2);
		}
		else if(frequency.equals("Every Three Weeks")){
			cal.add(Calendar.WEEK_OF_YEAR, 3);
		}
	
		else if(frequency.equals("Every Four Weeks")){
			cal.add(Calendar.WEEK_OF_YEAR, 4);
		}
		else if(frequency.equals("Every Five Weeks")){
			cal.add(Calendar.WEEK_OF_YEAR, 5);
		}
		else if(frequency.equals("Every Six Weeks")){
			cal.add(Calendar.WEEK_OF_YEAR, 6);
		}
		else if(frequency.equals("Once a Month")){
			cal.add(Calendar.MONTH, 1);
		}
		else if(frequency.equals("Every Two Months")){
			cal.add(Calendar.MONTH, 2);
		}

		if(cal.before(todayCal)){
			return true;
		}
		 
		return false;
	}
	
	
	/**
	 * Handle one apartment chores division.
	 * After Division, puts scheduled and assigned chores in the DB,
	 * and send notification.
	 * today - today's date.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static void DivideChoresForApartment(String apartmentId, Date today) throws ClientProtocolException, IOException{
		
		ParseRestClient parse = new ParseRestClientImpl();
		
		//change last division date for the apartment
		parse.updateApartmentLastDivision(apartmentId, today);
		
		//Schedule chores
		List<Chore> chores = ChoresRest.scheduleChores(apartmentId);//TODO there's a problem with this function. fix.
        Calendar earliestChore = Calendar.getInstance();
        for (Chore chore : chores) {
            Calendar startsFrom = Calendar.getInstance();
            startsFrom.setTime(chore.getStartsFrom());
            if (startsFrom.before(earliestChore)) {
                earliestChore = startsFrom;
            }
        }


        //Assign chores
		ChoresDivisionAlgorithms.assignChores(chores, apartmentId);
		
		//Write assigned chores to the server
		parse.addChores(chores);
		
		//Notify roommates about the new chores
		NotificationsHandling.notifyNewChores(apartmentId, earliestChore);
		
	}
}
