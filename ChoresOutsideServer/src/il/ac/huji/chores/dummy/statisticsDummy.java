package il.ac.huji.chores.dummy;

import il.ac.huji.chores.ChoreStatistics;
import il.ac.huji.chores.dal.ChoreStatisticsDAL;
import il.ac.huji.chores.exceptions.ChoreStatisticsException;

public class statisticsDummy {
	public static void main(String [] args){
		//System.out.println("Exists choreTest ? "+ChoreStatisticsDAL.choreStatisticsExists("choreTest"));
		ChoreStatisticsDAL.createChoreStatistic("choreTest");
		ChoreStatistics stats =ChoreStatisticsDAL.getChoreStatistic("choreTest");
		try {
			ChoreStatisticsDAL.updateChoreDoneCount("choreTest",1);
		ChoreStatisticsDAL.updateChoreMissedCount("choreTest", 2);
		ChoreStatisticsDAL.updateChorePointsTotalCount("choreTest", 9);
		ChoreStatisticsDAL.updateChoreTotalCount("choreTest", 3);
		} catch (ChoreStatisticsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		stats  =ChoreStatisticsDAL.getChoreStatistic("choreTest");
		System.out.println("avg: "+stats.getAverageValue());
		System.out.println("totalcount :"+stats.getTotalCount());
		System.out.println("total missed : "+stats.getTotalMissed());
		System.out.println("total done:"+stats.getTotalDone());
		
	}

}
