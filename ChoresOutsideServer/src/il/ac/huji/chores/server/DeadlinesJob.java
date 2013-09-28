package il.ac.huji.chores.server;

import java.io.IOException;

import il.ac.huji.chores.Chore;
import il.ac.huji.chores.Chore.CHORE_STATUS;
import il.ac.huji.chores.server.parse.JsonConverter;
import il.ac.huji.chores.server.parse.ParseRestClientImpl;

import org.json.JSONObject;
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

			JSONObject status = new JSONObject();
			status.put("status", CHORE_STATUS.STATUS_FUTURE.toString());
	

			if(chore.getStatus() == CHORE_STATUS.STATUS_FUTURE){
				
				parse.updateObject("Chores", chore.getId(), status.toString());

				NotificationsHandling.notifyMissedChore(chore);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

}

