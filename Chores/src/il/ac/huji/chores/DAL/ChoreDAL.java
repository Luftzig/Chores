package il.ac.huji.chores.dal;
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

	public static List<Chore> getRoomatesChores(String roomateID) {
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

}
