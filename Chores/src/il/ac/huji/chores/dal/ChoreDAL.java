package il.ac.huji.chores.dal;

import com.parse.*;
import il.ac.huji.chores.ApartmentChore;
import il.ac.huji.chores.Chore;
import il.ac.huji.chores.Chore.CHORE_STATUS;
import il.ac.huji.chores.ChoreInfo;
import il.ac.huji.chores.ChoreInfo.CHORE_INFO_PERIOD;
import il.ac.huji.chores.ChoreInfoInstance;
import il.ac.huji.chores.exceptions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChoreDAL {

	public static String addChoreInfo(ChoreInfo choreInfo)
			throws UserNotLoggedInException, FailedToAddChoreInfoException {
		String apartmentID = RoommateDAL.getApartmentID();
		ParseObject chores = new ParseObject("ChoresInfo");
		ParseACL permissions = new ParseACL();
		permissions.setPublicWriteAccess(true);
		permissions.setPublicReadAccess(true);
		chores.setACL(permissions);
		chores.put("apartment", apartmentID);
		chores.put("choreName", choreInfo.getName());
		chores.put("frequency", choreInfo.getHowManyInPeriod());
		chores.put("coins", choreInfo.getCoinsNum());
		chores.put("period", choreInfo.getPriod().toString());
		chores.put("isEveryone", choreInfo.isEveryone());
		try {
			chores.save();
		} catch (ParseException e) {
			throw new FailedToAddChoreInfoException(
					"FailedToAddChoreInfoException:" + e.toString());
		}
		return chores.getObjectId();
	}
	
	public static void updateAssignedTo(String choreID, String newOwner)
			throws UserNotLoggedInException, DataNotFoundException {
		String apartmentID = RoommateDAL.getApartmentID();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Chores");
		ParseObject choreObj;
		try {
			choreObj = query.get(choreID);
			choreObj.put("assignedTo", newOwner);
			choreObj.save();
		} catch (ParseException e) {
			throw new DataNotFoundException("Chore with id " + choreID
					+ " wasn't found");
		}
	}
	
	//updateAssignedTo + updateChoreStatus to done
	public static void stealChore(String choreID, String newOwner)
			throws UserNotLoggedInException, DataNotFoundException {

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Chores");
		ParseObject choreObj;
		try {
			choreObj = query.get(choreID);
			choreObj.put("assignedTo", newOwner);
			choreObj.put("status", CHORE_STATUS.STATUS_DONE.toString());
			choreObj.save();
		} catch (ParseException e) {
			throw new DataNotFoundException("Chore with id " + choreID
					+ " wasn't found");
		}
	}

	public static void updateChoreInfoName(String choreID, String choreName)
			throws UserNotLoggedInException, DataNotFoundException {
		String apartmentID = RoommateDAL.getApartmentID();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ChoresInfo");
		ParseObject choreInfoObj;
		try {
			choreInfoObj = query.get(choreID);

			choreInfoObj.put("choreName", choreName);
			choreInfoObj.save();
		} catch (ParseException e) {
			throw new DataNotFoundException("Chore info with id " + choreID
					+ " wasn't found");
		}
	}

	public static void updateFrequency(String choreID, int frequency)
			throws UserNotLoggedInException, DataNotFoundException {
		String apartmentID = RoommateDAL.getApartmentID();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ChoresInfo");
		ParseObject choreInfoObj;
		try {
			choreInfoObj = query.get(choreID);
			choreInfoObj.put("frequency", frequency);
			choreInfoObj.save();
		} catch (ParseException e) {
			throw new DataNotFoundException("Chore info with id " + choreID
					+ " wasn't found");
		}
	}

	public static void updatePeriod(String choreID, CHORE_INFO_PERIOD period)
			throws UserNotLoggedInException, DataNotFoundException {
		String apartmentID = RoommateDAL.getApartmentID();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ChoresInfo");
		ParseObject choreInfoObj;
		try {
			choreInfoObj = query.get(choreID);
			choreInfoObj.put("period", period.toString());
			choreInfoObj.save();
		} catch (ParseException e) {
			throw new DataNotFoundException("Chore info with id " + choreID
					+ " wasn't found");
		}
	}

	public static void updateCoins(String choreID, int coins)
			throws UserNotLoggedInException, DataNotFoundException {
		String apartmentID = RoommateDAL.getApartmentID();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Chores");
		ParseObject choreInfoObj;
		try {
			choreInfoObj = query.get(choreID);
			choreInfoObj.put("coins", coins);
			choreInfoObj.save();
		} catch (ParseException e) {
			throw new DataNotFoundException("Chore info with id " + choreID
					+ " wasn't found");
		}
	}

	public static ChoreInfo getChoreInfo(String choreInfoId)
			throws UserNotLoggedInException, DataNotFoundException {
		String apartmentID = RoommateDAL.getApartmentID();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ChoresInfo");
		query.whereEqualTo("apartmentID", apartmentID);
		ParseObject choreInfoObj;
		try {
			choreInfoObj = query.get(choreInfoId);
			ChoreInfo choreInfo = convertParseObjectToChoreInfo(choreInfoObj);
			return choreInfo;
		} catch (ParseException e) {
			throw new DataNotFoundException("Chore info with id " + choreInfoId
					+ " wasn't found");
		}
	}

	public static boolean updateChoreStatus(String choreId, CHORE_STATUS status)
			throws FailedToUpdateStatusException {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Chores");
		ParseObject chore;
		try {
			chore = query.get(choreId);
			chore.put("status", status.toString());
			chore.save();
		} catch (ParseException e) {
			
			throw new FailedToUpdateStatusException(e.getMessage());
		}

		return false;
	}

	public static Chore convertParseObjectToChore(ParseObject obj) {
		Chore chore = new ApartmentChore();
		chore.setId(obj.getObjectId());
		chore.setAssignedTo(obj.getString("assignedTo"));
		chore.setCoinsNum(obj.getInt("coins"));
		chore.setDeadline(new Date(obj.getLong("deadline")));
		chore.setName(obj.getString("name"));
		chore.setStartsFrom(new Date(obj.getLong("startsFrom")));
		String choreStatus = obj.getString("status");
		chore.setStatus(CHORE_STATUS.valueOf(choreStatus));
		return chore;
	}

	public static String addChore(Chore chore) throws ParseException, UserNotLoggedInException {
		String apartment = RoommateDAL.getApartmentID();
		ParseObject choreObj = new ParseObject("Chores");
		choreObj.put("assignedTo", chore.getAssignedTo());
		choreObj.put("apartment", apartment);
		choreObj.put("coins", chore.getCoinsNum());
		choreObj.put("name", chore.getName());
		choreObj.put("startsFrom", chore.getStartsFrom().getTime());
		choreObj.put("deadline", chore.getDeadline().getTime());
		choreObj.put("status", chore.getStatus().toString());
		choreObj.put("choreInfoId", chore.getChoreInfoId());
		choreObj.save();
		return choreObj.getObjectId();
	}

	public static List<Chore> getRoommatesChores()
			throws UserNotLoggedInException {
        List<Chore> chores;
        List<ParseObject> parseChores;
		try {
            String username = ParseUser.getCurrentUser().getUsername();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Chores");
            query.whereEqualTo("assignedTo", username);
            chores = new ArrayList<Chore>();
			parseChores = query.find();
		} catch (ParseException e) {
            throw new UserNotLoggedInException("User not logged in");
		}
		for (ParseObject chore : parseChores) {
			chores.add(convertParseObjectToChore(chore));
		}
		return chores;
	}

	public static List<ChoreInfo> getAllChoreInfo() throws ParseException,
			UserNotLoggedInException {
		String apartmentID = RoommateDAL.getApartmentID();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ChoresInfo");
		query.whereEqualTo("apartment", apartmentID);
		List<ParseObject> results = query.find();
		List<ChoreInfo> choresInfoList = new ArrayList<ChoreInfo>();
		ChoreInfo choreInfo;
		for (ParseObject parseChore : results) {
			choreInfo = convertParseObjectToChoreInfo(parseChore);
			choresInfoList.add(choreInfo);
		}
		return choresInfoList;
	}

	public static Chore getChore(String choreID) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Chores");
		ParseObject chore;
		try {
			chore = query.get(choreID);
		} catch (ParseException e) {
			return null;
		}
		return convertParseObjectToChore(chore);

	}

	public static List<Chore> getUserAllOldChores() throws FailedToRetrieveOldChoresException, UserNotLoggedInException {
		String roommateID = RoommateDAL.getUserID();
		ParseQuery<ParseObject> queryDone = ParseQuery.getQuery("Chores");
		//.whereEqualTo("assignedTo", roommateID)
		queryDone.whereEqualTo("assignedTo", roommateID).whereEqualTo("status", CHORE_STATUS.STATUS_DONE.toString());
		ParseQuery<ParseObject> queryMissed = ParseQuery.getQuery("Chores");
		queryMissed.whereEqualTo("assignedTo", roommateID).whereEqualTo("status", CHORE_STATUS.STATUS_MISS.toString());
		List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>> ();
		queries.add(queryDone);
		queries.add(queryMissed);
		ParseQuery<ParseObject> query = ParseQuery.or(queries);
		List<ParseObject> results;
		
		
		try {
			results = query.find();
		} catch (ParseException e) {
			throw new FailedToRetrieveOldChoresException(e.toString());
		}
		List<Chore> choresList = new ArrayList<Chore>();
		Chore chore;
		for (ParseObject parseChore : results) {
			chore = convertParseObjectToChore(parseChore);
			choresList.add(chore);
		}
		return choresList;
	}

	/*
	 * returns the most chosen value for this chore Every call keeps the
	 * previous the results - to return it in the next call if there's no
	 * network. If there's no network and no saved value, return -1.
	 */
	public static int getChoreValueFromStats(String choreName) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * gets older chores. oldestChoreDisplayed is the parse id. if
	 * oldestChoreDisplayed is null, there are no chores displayed at the moment
	 * - return the first amount
	 * @throws UserNotLoggedInException 
	 * @throws FailedToRetrieveOldChoresException 
	 */
	public static List<Chore> getUserOldChores(String oldestChoreDisplayed,
			int amount) throws UserNotLoggedInException, FailedToRetrieveOldChoresException {
		if(oldestChoreDisplayed==null)
			return null;
		String roommateID = RoommateDAL.getUserID();
		ParseQuery<ParseObject> queryDone = ParseQuery.getQuery("Chores");
		queryDone.whereEqualTo("assignedTo", roommateID).whereEqualTo("name", oldestChoreDisplayed).whereEqualTo("status", CHORE_STATUS.STATUS_DONE.toString());
		ParseQuery<ParseObject> queryMissed = ParseQuery.getQuery("Chores");
		queryMissed.whereEqualTo("assignedTo", roommateID).whereEqualTo("name", oldestChoreDisplayed).whereEqualTo("status", CHORE_STATUS.STATUS_MISS.toString());
		List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>> ();
		queries.add(queryDone);
		queries.add(queryMissed);
		ParseQuery<ParseObject> query = ParseQuery.or(queries).orderByAscending("startsFrom");
		List<ParseObject> results;
		try {
			results = query.find();
		} catch (ParseException e) {
			throw new FailedToRetrieveOldChoresException(e.toString());
		}
		List<Chore> choresList = new ArrayList<Chore>();
		Chore chore;
		for (int i=0;i<Math.min(results.size(), amount);i++) {
			chore = convertParseObjectToChore(results.get(i));
			choresList.add(chore);
		}
		return choresList;
	}

	/*
	 * return all assigned (not done, deadline has not passed) chores. If there
	 * are no chores, return an empty ArrayList.
	 */
	public static List<Chore> getAllChores() throws UserNotLoggedInException, FailedToRetriveAllChoresException {
		String apartmentID = RoommateDAL.getApartmentID();
		ParseQuery<ParseObject> queryfuture = ParseQuery.getQuery("Chores");
		queryfuture.whereEqualTo("apartment", apartmentID).whereEqualTo("status", CHORE_STATUS.STATUS_FUTURE.toString());

		List<ParseObject> results;
		try {
			results = queryfuture.find();
		} catch (ParseException e) {
			throw new FailedToRetriveAllChoresException(e.toString());
		}
		List<Chore> choresList = new ArrayList<Chore>();
		Chore chore;
		for (ParseObject parseChore : results) {
			chore = convertParseObjectToChore(parseChore);
			choresList.add(chore);
		}
		return choresList;
	}

	private static ChoreInfo convertParseObjectToChoreInfo(
			ParseObject choreInfoObj) {
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
