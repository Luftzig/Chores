package il.ac.huji.chores.DAL;
import com.parse.Parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import il.ac.huji.chores.*;

public class ChoreDAL extends BasicDAL{

	public static String addChoreInfo(ChoreInfo choreInfo) {
		return null;
	}

	public static String updateChoreInfo(ChoreInfo choreInfo) {
		return null;
	}

	public static ChoreInfo getChoreInfo(String choreInfoId) {
		return null;
	}

	public static boolean updateChoreStatus(String choreId) {
		return false;
	}

	public static List<Chore> getRoommatesChores(String roomateID) {
		return null;
	}

	public static List<ChoreInfo> getAllChoreInfo(String aptID) {
		return null;
	}

	public static List<ChoreInfo> getAllChores(String aptID) {
		return null;
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


}
