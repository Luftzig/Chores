package il.ac.huji.chores.server;

import java.io.IOException;

import il.ac.huji.chores.Chore;
import il.ac.huji.chores.Chore.CHORE_STATUS;
import il.ac.huji.chores.server.parse.ParseRestClientImpl;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DeadlinesJob implements Job {

	@Override
	public void execute(JobExecutionContext jobContext) throws JobExecutionException {

		JobDataMap dataMap = jobContext.getJobDetail().getJobDataMap();
		String choreId = dataMap.getString("choreId");
		
		ParseRestClientImpl parse = new ParseRestClientImpl();
		Chore chore;
		try {
			chore = parse.getChoreObj(choreId);

			if(chore.getStatus() == CHORE_STATUS.STATUS_FUTURE){
				chore.setStatus(CHORE_STATUS.STATUS_MISS);
				parse.addChoreObj(chore);
				
				NotificationsHandling.notifyMissedChore(chore);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

}
