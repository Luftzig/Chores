package il.ac.huji.chores.dummy;
import il.ac.huji.chores.ChoreInfo;
import il.ac.huji.chores.ChoreInfo.CHORE_INFO_PERIOD;
import il.ac.huji.chores.ChoreInfoInstance;

import java.util.ArrayList;
import java.util.List;

public class ChoreInfoDal {

	public static List<ChoreInfo> getChoreInfos() {

		List<ChoreInfo> chores = new ArrayList<ChoreInfo>();
		chores.add(new ChoreInfoInstance("Washing dishes", 5, 3, CHORE_INFO_PERIOD.CHORE_INFO_DAY, false));
		chores.add(new ChoreInfoInstance("Washing dishes", 2, 1, CHORE_INFO_PERIOD.CHORE_INFO_YEAR, false));
		chores.add(new ChoreInfoInstance("Floor sweeping", 1, 6, CHORE_INFO_PERIOD.CHORE_INFO_DAY, true));
		
		return chores;
	}

//	//update existing chore info
//	public static void update() {
//		// do nothing
//		
//	}
//
//	//insert nre chore info
//	public static void insert() {
//		// do nothing
//		
//	}

}
