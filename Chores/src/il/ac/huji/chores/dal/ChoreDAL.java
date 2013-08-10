package il.ac.huji.chores.dal;
import com.parse.Parse;
import java.util.List;

import il.ac.huji.chores.*;

public class ChoreDAL extends BasicDAL{

	public String addChoreInfo(ChoreInfo choreInfo) {
		return null;
	}

	public String updateChoreInfo(ChoreInfo choreInfo) {
		return null;
	}

	public ChoreInfo getChoreInfo(String choreInfoId) {
		return null;
	}

	public boolean updateChoreStatus(String choreId) {
		return false;
	}

	public List<Chore> getRoomatesChores(String roomateID) {
		return null;
	}

	public List<ChoreInfo> getAllChoreInfo(String aptID) {
		return null;
	}

	public List<ChoreInfo> getAllChores(String aptID) {
		return null;
	}

	public Chore getChore(String choreID) {
		return null;
	}

	public Chore getUserOldChores(Chore oldestdisplayed, int amount) {
		return null;
	}

}
