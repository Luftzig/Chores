package il.ac.huji.chores.dummy;
import il.ac.huji.chores.ChoreInfo;
import il.ac.huji.chores.ChoreInfo.CHORE_INFO_PERIOD;
import il.ac.huji.chores.ChoreInfoInstance;

import java.util.ArrayList;
import java.util.List;

public class ChoreInfoDal {

	public static List<ChoreInfo> getChoreInfos() {

		List<ChoreInfo> chores = new ArrayList<ChoreInfo>();
		chores.add(new ChoreInfoInstance("dish washing", 5, 3, CHORE_INFO_PERIOD.CHORE_INFO_WEEK));
		chores.add(new ChoreInfoInstance("dish breaking", 2, 1, CHORE_INFO_PERIOD.CHORE_INFO_YEAR));
		chores.add(new ChoreInfoInstance("hulahoop cleaning", 1, 1000, CHORE_INFO_PERIOD.CHORE_INFO_DAY));
		chores.add(new ChoreInfoInstance("walking cow", 5, 3, CHORE_INFO_PERIOD.CHORE_INFO_WEEK));
		
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
