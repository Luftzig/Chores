package il.ac.huji.chores.DAL;
import com.parse.Parse;
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
	public static int getChoreStatisticsVal(String _choreName) {
		// TODO Auto-generated method stub
		return 0;
	}

}
