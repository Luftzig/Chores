package il.ac.huji.chores.server;

import il.ac.huji.chores.Chore;

import java.util.Calendar;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;


public class ChoresServerMain {

	public static final int ROUTINE_JOB_REPEAT_INTERVAL = 2; // hours
	static long curMissedDeadlineID = 0;
	static Scheduler scheduler = null;

	public static void main(String[] args){

		try {
			// Grab the Scheduler instance from the Factory
			scheduler = StdSchedulerFactory.getDefaultScheduler();

			// and start it off
			scheduler.start();

			/**** Schedule the daily job ****/

			// define the job and tie it to our RoutineJob class
			JobDetail RoutineJob = newJob(RoutineJob.class)
					.withIdentity("job1", "group1")
					.build();

			// Trigger the job to run now, and then every 2 hours
			Trigger routineTrigger = newTrigger()
					.withIdentity("trigger1", "group1") 
					.withSchedule(simpleSchedule()
							.withIntervalInHours(ROUTINE_JOB_REPEAT_INTERVAL)
							.repeatForever()) 
							.build();

			// Tell quartz to schedule the job using our trigger
			scheduler.scheduleJob(RoutineJob, routineTrigger);

			//scheduler.shutdown();

		} catch (SchedulerException se) {
			se.printStackTrace();
		}


	}

	public static void triggerDeadlinePassed(Chore chore){

		Calendar cal = Calendar.getInstance();
		cal.setTime(chore.getDeadline());
		cal.add(Calendar.MINUTE, 1);
		
		/**** Create the missed deadline job ****/
		JobDetail missedDeadlineJob = newJob(DeadlinesJob.class)
				.withIdentity("job"+(curMissedDeadlineID++), "group2")
				.usingJobData("choreId", chore.getId())
				.build();

		//Trigger the job a minute after the deadline.
		
		int hour= ((cal.get(Calendar.AM_PM) == Calendar.AM) ? (cal.get(Calendar.HOUR)) : (cal.get(Calendar.HOUR) + 12));
		
		Trigger deadlineTrigger = newTrigger()
			    .withIdentity("deadlineTrigger"+(curMissedDeadlineID++), "group2")
			    .withSchedule(cronSchedule("" 
			    						+ cal.get(Calendar.SECOND) + " "
			    						+ cal.get(Calendar.MINUTE) + " "
			    						+ hour + " "
			    						+ cal.get(Calendar.DAY_OF_MONTH) + " "
			    						+ (cal.get(Calendar.MONTH)+1) + " "
			    						+ "? "
			    						+ cal.get(Calendar.YEAR)))
			    .build();

		// Tell quartz to schedule the job using deadline trigger
		try {
			scheduler.scheduleJob(missedDeadlineJob, deadlineTrigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return;
		}
	}



}
