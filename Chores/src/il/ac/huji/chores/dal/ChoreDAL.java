package il.ac.huji.chores.dal;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import il.ac.huji.chores.*;
import il.ac.huji.chores.ChoreInfo.CHORE_INFO_PERIOD;
import il.ac.huji.chores.exceptions.DataNotFoundException;
import il.ac.huji.chores.exceptions.FailedToAddChoreInfoException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

public class ChoreDAL{

	public static String addChoreInfo(ChoreInfo choreInfo) throws UserNotLoggedInException, FailedToAddChoreInfoException {
		String apartmentID = RoommateDAL.getApartmentID();
		ParseObject chores = new ParseObject("ChoresInfo");
		chores.put("apartment", apartmentID);
		chores.put("choreName", choreInfo.getName());
		chores.put("frequency", choreInfo.getHowManyInPeriod());
		chores.put("coins", choreInfo.getCoinsNum());
		chores.put("period", choreInfo.getPriod().toString());
		chores.put("isEveryone", choreInfo.isEveryone());
		try {
			chores.save();
		} catch (ParseException e) {
			throw new FailedToAddChoreInfoException("FailedToAddChoreInfoException:"+e.toString());
		}
		return chores.getObjectId();
	}

	public static void updateChoreInfoName(String choreID,String choreName) throws UserNotLoggedInException, DataNotFoundException {
		String apartmentID = RoommateDAL.getApartmentID();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ChoresInfo");
		ParseObject choreInfoObj;
		try {
			choreInfoObj = query.get(choreID);
			
			choreInfoObj.put("choreName", choreName);
			choreInfoObj.save();
		} catch (ParseException e) {
			throw new DataNotFoundException("Chore info with id "+choreID+ " wasn't found");
		}
	}
	
	public static void updateFrequency(String choreID,int frequency) throws UserNotLoggedInException, DataNotFoundException {
		String apartmentID = RoommateDAL.getApartmentID();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ChoresInfo");
		ParseObject choreInfoObj;
		try {
			choreInfoObj = query.get(choreID);
			choreInfoObj.put("frequency", frequency);
			choreInfoObj.save();
		} catch (ParseException e) {
			throw new DataNotFoundException("Chore info with id "+choreID+ " wasn't found");
		}
	}
	
	public static void updatePeriod(String choreID,CHORE_INFO_PERIOD period ) throws UserNotLoggedInException, DataNotFoundException {
		String apartmentID = RoommateDAL.getApartmentID();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ChoresInfo");
		ParseObject choreInfoObj;
		try {
			choreInfoObj = query.get(choreID);
			choreInfoObj.put("period",period.toString() );
			choreInfoObj.save();
		} catch (ParseException e) {
			throw new DataNotFoundException("Chore info with id "+choreID+ " wasn't found");
		}
	}
	
	public static void updateCoins(String choreID,int coins) throws UserNotLoggedInException, DataNotFoundException {
		String apartmentID = RoommateDAL.getApartmentID();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ChoresInfo");
		ParseObject choreInfoObj;
		try {
			choreInfoObj = query.get(choreID);
			choreInfoObj.put("coins", coins);
			choreInfoObj.save();
		} catch (ParseException e) {
			throw new DataNotFoundException("Chore info with id "+choreID+ " wasn't found");
		}
	}
	

	public static ChoreInfo getChoreInfo(String choreInfoId) throws UserNotLoggedInException, DataNotFoundException {
		String apartmentID = RoommateDAL.getApartmentID();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ChoresInfo");
		query.whereEqualTo("apartmentID", apartmentID);
		ParseObject choreInfoObj;
		try {
			choreInfoObj = query.get(choreInfoId);
			ChoreInfo choreInfo= convertParseObjectToChoreInfo(choreInfoObj);
			return choreInfo;
		} catch (ParseException e) {
			throw new DataNotFoundException("Chore info with id "+choreInfoId+ " wasn't found");
		}
	}

	public static boolean updateChoreStatus(String choreId) {
		return false;
	}

	public static List<Chore> getRoommatesChores(String roommateID) {
		return null;
	}

	public static List<ChoreInfo> getAllChoreInfo() throws ParseException, UserNotLoggedInException {
		String apartmentID = RoommateDAL.getApartmentID();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ChoresInfo");
		query.whereEqualTo("apartment", apartmentID);
		List<ParseObject> results = query.find();
		List<ChoreInfo> choresInfoList = new ArrayList<ChoreInfo>();
		ChoreInfo choreInfo;
		for(ParseObject parseChore:results){
			choreInfo=convertParseObjectToChoreInfo(parseChore);
			choresInfoList.add(choreInfo);
		}
		return choresInfoList;
	}


	public static Chore getChore(String choreID) {
		return null;
	}

	public static Chore getUserOldChores(Chore oldestdisplayed, int amount) {
		return null;
	}

	/*
	 * returns the most chosen value for this chore
	 * Every call keeps the previous the results - to return it in the next call if there's no network.
	 * If there's no network and no saved value, return -1.
	 */
	public static int getChoreValueFromStats(String choreName) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * gets older chores.
	 * oldestChoreDisplayed is the parse id.
	 * if oldestChoreDisplayed is null, there are no chores displayed at the moment - return the first amount
	 */
	public static List<Chore> getUserOldChores(String oldestChoreDisplayed,int amount) {
		// TODO Auto-generated method stub
	  
		return null;
	}

	/*
	 * return all assigned (not done, deadline has not passed) chores.
	 * If there are no chores, return an empty ArrayList.
	 */
	public static List<Chore> getAllChores() {
		// TODO Auto-generated method stub
		return new ArrayList<Chore>();
	}

	private static ChoreInfo convertParseObjectToChoreInfo(ParseObject choreInfoObj){
		ChoreInfo choreInfo = new ChoreInfoInstance();
		choreInfo.setChoreInfoID(choreInfoObj.getObjectId());
		choreInfo.setChoreInfoName(choreInfoObj.getString("choreName"));
		choreInfo.setCoins(choreInfoObj.getInt("coins"));
		choreInfo.setHowMany(choreInfoObj.getInt("frequency"));
		choreInfo.setPeriod(choreInfoObj.getString("period"));
		choreInfo.setIsEveryone(choreInfoObj.getBoolean("isEveryone"));
		return choreInfo;
		
	}

}
