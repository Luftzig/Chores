package il.ac.huji.chores.server.parse;

import il.ac.huji.chores.ApartmentChore;
import il.ac.huji.chores.Chore;
import il.ac.huji.chores.Chore.CHORE_STATUS;
import il.ac.huji.chores.ChoreInfo;
import il.ac.huji.chores.ChoreInfo.CHORE_INFO_PERIOD;
import il.ac.huji.chores.Constants;
import il.ac.huji.chores.RoommatesApartment;
import il.ac.huji.chores.dal.RoommateDAL;
import il.ac.huji.chores.server.ChoresDivisionAlgorithms;
import il.ac.huji.chores.server.ChoresFunFacts;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sound.midi.MidiDevice.Info;

public class ChoresRest {
	
	public static List<Chore> scheduleChores(RoommatesApartment apt){
	
		List<Chore> chores = new ArrayList<Chore>();
		ParseRestClientImpl parse = new ParseRestClientImpl();
		List<ChoreInfo> choreInfoList=null;
		try {
			choreInfoList = parse.getApartmentChoreInfos(apt.getId());
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		Date currentDate = cal.getTime();
		
		for(ChoreInfo choreInfo :choreInfoList){
			
			int times = getPeriodsNumInAptFrequency(choreInfo.getPriod(), apt.getDivisionFrequency(), cal);
			
			try {
				chores.addAll(scheduleChore(choreInfo,currentDate, apt.getId(), times));
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			
		}
		
		return chores;
	}
	
	//returns how many times one chore division period is in apartment division frequency. Round down, unless the result is 0.
	private static int getPeriodsNumInAptFrequency(CHORE_INFO_PERIOD period, String frequency, Calendar todayCal) {

		if(period.equals(CHORE_INFO_PERIOD.CHORE_INFO_NOT_REPEATED)){
			return 1;
		}
		
		if(frequency.equals("Once a Week")){
			switch(period){
				case CHORE_INFO_DAY: 
					return 7;
				
				case CHORE_INFO_WEEK:
				case CHORE_INFO_MONTH:
				case CHORE_INFO_YEAR:
					return 1;
				default:
					System.out.println("Error: can't happen");
			}
			
		}
		else if(frequency.equals("Every Two Weeks")){
			switch(period){
				case CHORE_INFO_DAY: 
					return 14;
				case CHORE_INFO_WEEK:
					return 2;
				case CHORE_INFO_MONTH:
				case CHORE_INFO_YEAR:
					return 1;
				default:
					System.out.println("Error: can't happen");
			}
		}
		else if(frequency.equals("Every Three Weeks")){
			switch(period){
				case CHORE_INFO_DAY: 
					return 21;
				case CHORE_INFO_WEEK:
					return 3;
				case CHORE_INFO_MONTH:
				case CHORE_INFO_YEAR:
					return 1;
				default:
					System.out.println("Error: can't happen");
			}
		}
		else if(frequency.equals("Every Four Weeks")){
			switch(period){
				case CHORE_INFO_DAY: 
					return 28;
				case CHORE_INFO_WEEK:
					return 4;
				case CHORE_INFO_MONTH:
				case CHORE_INFO_YEAR:
					return 1;
				default:
					System.out.println("Error: can't happen");
			}
		}
		else if(frequency.equals("Once a Month")){
			switch(period){
				case CHORE_INFO_DAY: 
					return todayCal.getActualMaximum(Calendar.DAY_OF_MONTH);
				case CHORE_INFO_WEEK:
					return todayCal.getActualMaximum(Calendar.WEEK_OF_MONTH);
				case CHORE_INFO_MONTH:
				case CHORE_INFO_YEAR:
					return 1;
				default:
					System.out.println("Error: can't happen");
			}
		}
		else if(frequency.equals("Every Two Months")){
			Calendar nextMonthCal = Calendar.getInstance();
			nextMonthCal.add(Calendar.MONTH, 1);
			switch(period){
				case CHORE_INFO_DAY: 
					return todayCal.getActualMaximum(Calendar.DAY_OF_MONTH) + nextMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH);
				case CHORE_INFO_WEEK:
					return todayCal.getActualMaximum(Calendar.WEEK_OF_MONTH) + nextMonthCal.getActualMaximum(Calendar.WEEK_OF_MONTH);
				case CHORE_INFO_MONTH:
					return 2;
				case CHORE_INFO_YEAR:
					return 1;
				default:
					System.out.println("Error: can't happen");
			}
		}
		System.err.println("Period or frequency doesn't exist");
		return 0;
	}

	//schedule one chore. times is how many sequential periods to schedue to.
	private static List<Chore> scheduleChore(ChoreInfo choreInfo, Date currentDate,String aptId, int times) throws ClientProtocolException, IOException{
		
		List<Chore> chores=new ArrayList<Chore>();
		
		Calendar curCal = Calendar.getInstance(); // a calendar that holds the start date of the current period tp schedule in.
		curCal.setTime(currentDate);
		curCal.set(Calendar.SECOND, 0);
		curCal.set(Calendar.MINUTE, 0);
		curCal.set(Calendar.HOUR, 0);
		
		Calendar startCal = Calendar.getInstance();// a calendar that holds the start time of the next chore to schedule
		Calendar sanityCal = Calendar.getInstance();//to check deadline is after start
		
		
		for(int j=0; j < times; j++){
		
			startCal.setTime(curCal.getTime());
			for(int i=0 ; i<choreInfo.getHowManyInPeriod();i++){
				
				Chore chore = new ApartmentChore();
				chore.setChoreInfoId(choreInfo.getChoreInfoID());
				chore.setCoinsNum(choreInfo.getCoinsNum());
				chore.setName(choreInfo.getName());
				chore.setStartsFrom(startCal.getTime());
				chore.setApartment(aptId);
				chore.setStatus(CHORE_STATUS.STATUS_FUTURE);
				chore.setFunFact(ChoresFunFacts.getFactForChore(chore.getName()));
				if(choreInfo.isEveryone()){
					chore.setAssignedTo(Constants.CHORE_ASSIGNED_TO_EVERYONE);
				}
	
				Date deadline = calculateDeadline(choreInfo,curCal.getTime(),i);
				sanityCal.setTime(deadline);
				if(sanityCal.before(startCal)){
					sanityCal.setTime(chore.getStartsFrom());
					deadline = sanityCal.getTime();
				}
				
				chore.setDeadline(deadline);
				
				chores.add(chore);
				
				//set start time for the next chore
				startCal.setTime(deadline);
				startCal.add(Calendar.DAY_OF_MONTH, 1);
				curCal.set(Calendar.SECOND, 0);
				curCal.set(Calendar.MINUTE, 0);
				curCal.set(Calendar.HOUR, 0);
				
			}
			
			if(choreInfo.getPriod().equals(CHORE_INFO_PERIOD.CHORE_INFO_NOT_REPEATED)){
				break;
			}
			jumpPeriod(curCal, choreInfo);
		}
		return chores;
		
	}
	
	//set the calendar with the date of the next period 
	//(day/ week/month/etc after this calendar's date, according to the choreInfo)
	private static void jumpPeriod(Calendar curCal, ChoreInfo info){
		
	switch(info.getPriod()){
		case CHORE_INFO_DAY:
			curCal.add(Calendar.DAY_OF_MONTH, 1);
			break;	
		case CHORE_INFO_WEEK:
			curCal.add(Calendar.WEEK_OF_MONTH, 1);
			break;
		case CHORE_INFO_MONTH:
			curCal.add(Calendar.MONTH, 1); 
			break;
		case CHORE_INFO_YEAR:
			curCal.add(Calendar.YEAR, 1);
			break;
		default:
			System.out.println("Error: can't happen");
	}
	}

    @SuppressWarnings("deprecation")
	private static Date calculateDeadline(ChoreInfo choreInfo, Date currentDate,int offset){
		Calendar c = Calendar.getInstance();
		System.out.println("Current date : "+currentDate.toGMTString());
		c.setTime(currentDate); // Now use today date.
		int days=0;
		switch(choreInfo.getPriod()){
		
			case CHORE_INFO_DAY:
				days=0;
				break;	
			case CHORE_INFO_WEEK:
				days=6;
				break;
			case CHORE_INFO_MONTH:
				days = c.getActualMaximum(Calendar.DAY_OF_MONTH)-1; 
				break;
			case CHORE_INFO_YEAR:
				days=364;
				break;		
		}
		days=(offset+1)*days/choreInfo.getHowManyInPeriod();
		c.add(Calendar.DATE, days);
		Date deadline = c.getTime();
		deadline.setHours(23);
		deadline.setMinutes(59);
		deadline.setSeconds(59);
		System.out.println("Calculated deadline : "+deadline.toGMTString());
		return deadline;
		
	}
}
