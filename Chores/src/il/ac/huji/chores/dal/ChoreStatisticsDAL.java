package il.ac.huji.chores.dal;

import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import il.ac.huji.chores.ApartmentChore;
import il.ac.huji.chores.ChoreStatistics;
import il.ac.huji.chores.exceptions.ChoreStatisticsException;

public class ChoreStatisticsDAL {

	public static ChoreStatistics getMostAccomplishedChore(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("choreStatistics");
		try {
			int max =0;
			String choreName="";
			List<ParseObject> chores =query.find();
			for(ParseObject chore :chores){
				int missed = chore.getInt("totalMissed");
				int done = chore.getInt("totalDone");
				if((missed+done>0)&& (done/(done+missed))>max){
					max=done/(done+missed);
					choreName = chore.getString("chore");
				}
			}
			return getChoreStatistic("choreName");
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static int getChoreAverageValue(String choreName){
		return getChoreStatistic(choreName).getAverageValue();
	}
	public static ChoreStatistics getMostMissedChore(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("choreStatistics");
		try {
			int max =0;
			String choreName="";
			List<ParseObject> chores =query.find();
			for(ParseObject chore :chores){
				int missed = chore.getInt("totalMissed");
				int done = chore.getInt("totalDone");
				if((missed+done>0)&& (missed/(done+missed))>max){
					max=missed/(done+missed);
					choreName = chore.getString("chore");
				}
			}
			return getChoreStatistic("choreName");
		} catch (ParseException e) {
			return null;
		}
	}
	public static void updateChoreMissedCount(String choreName, int count) throws ChoreStatisticsException {
		ParseObject choreStatistics = getChoreStatisticsObj(choreName);
		if(choreStatistics==null){
			choreStatistics = createChoreStatistic(choreName);
		}
		int currentCount = choreStatistics.getInt("totalMissed");
		currentCount+=count;
		choreStatistics.put("totalMissed", currentCount);
		try {
			choreStatistics.save();
		} catch (ParseException e) {
			throw new ChoreStatisticsException(e.getMessage());
		}
	}

	public static void updateChoreDoneCount(String choreName, int count) throws ChoreStatisticsException {
		ParseObject choreStatistics = getChoreStatisticsObj(choreName);
		if(choreStatistics==null){
			choreStatistics = createChoreStatistic(choreName);
		}
		int currentCount = choreStatistics.getInt("totalDone");
		currentCount+=count;
		choreStatistics.put("totalDone", currentCount);
		try {
			choreStatistics.save();
		} catch (ParseException e) {
			throw new ChoreStatisticsException(e.getMessage());
		}

	}

	public static void updateChoreTotalCount(String choreName, int count) throws ChoreStatisticsException {
		ParseObject choreStatistics = getChoreStatisticsObj(choreName);
		if(choreStatistics==null){
			choreStatistics = createChoreStatistic(choreName);
		}
		int currentCount = choreStatistics.getInt("totalCount");
		currentCount+=count;
		choreStatistics.put("totalCount", currentCount);
		try {
			choreStatistics.save();
		} catch (ParseException e) {
			throw new ChoreStatisticsException(e.getMessage());
		}

	}

	public static void updateChorePointsTotalCount(String choreName, int count) throws ChoreStatisticsException {
		ParseObject choreStatistics = getChoreStatisticsObj(choreName);
		if(choreStatistics==null){
			choreStatistics = createChoreStatistic(choreName);
		}
		int currentCount = choreStatistics.getInt("totalCoins");
		currentCount+=count;
		choreStatistics.put("totalCoins", currentCount);
		try {
			choreStatistics.save();
		} catch (ParseException e) {
			throw new ChoreStatisticsException(e.getMessage());
		}

	}

	public static ParseObject createChoreStatistic(String choreName) {
		ParseObject choreStatistics = new ParseObject("choreStatistics");
		choreStatistics.put("chore", choreName);
		choreStatistics.put("totalCount", 0);
		choreStatistics.put("totalMissed", 0);
		choreStatistics.put("totalDone", 0);
		choreStatistics.put("totalCoins", 0);
		try {
			choreStatistics.save();
		} catch (ParseException e) {
			return null;
		}
		return choreStatistics;
	}
	
	public static boolean choreStatisticsExists(String choreName){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("choreStatistics");
		query.whereEqualTo("chore", choreName);
		try {
			List<ParseObject> results = query.find();
			return results.size()>0;
		} catch (ParseException e) {
			return false;
		}
	}

	public static ParseObject getChoreStatisticsObj(String choreName) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("choreStatistics");
		query.whereEqualTo("chore", choreName);
		try {
			List<ParseObject> results = query.find();
			return results.get(0);
		} catch (ParseException e) {
			return null;
		}

	}

	public static ChoreStatistics getChoreStatistic(String choreName) {
		ParseObject choreStaticsObj = getChoreStatisticsObj(choreName);
		if (choreStaticsObj == null)
			return null;
		ChoreStatistics choreStatistics = new ChoreStatistics();
		choreStatistics.setChoreName(choreName);
		choreStatistics.setTotalCount(choreStaticsObj.getInt("totalCount"));
		choreStatistics.setTotalDone(choreStaticsObj.getInt("totalDone"));
		choreStatistics.setTotalMissed(choreStaticsObj.getInt("totalMissed"));
		choreStatistics.setTotalPoints(choreStaticsObj.getInt("totalCoins"));
		if (choreStatistics.getTotalCount() > 0) {
			choreStatistics.setAverageValue(choreStatistics.getTotalPoints()
					/ choreStatistics.getTotalCount());
		} else {
			choreStatistics.setAverageValue(0);
		}
		return choreStatistics;
	}
}
