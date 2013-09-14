package il.ac.huji.chores.server;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;


public class ChoresServerMain {
	
	public static final int ROUTINE_JOB_REPEAT_INTERVAL = 2; // hours

	public static void main(String[] args){

		try {
			// Grab the Scheduler instance from the Factory
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

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
	
}
