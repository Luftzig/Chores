package il.ac.huji.chores;

public class Settings {
	
	public Notifications notifications;
	public Chores chores;
	public Reminders reminders;
	
	public class Notifications{
		public boolean roommateStoleMyChore;
		public boolean roommateMissedChore;
		public boolean newChoresHasBeenDivided;
		public boolean roommateFinishedChore;
	}
	
	public class Chores{
		public boolean forbidRoommatesFromTakingMyChores;
		public boolean disableRemindersAboutUpcomingChores;
	}
	
	public class Reminders{
		public int hours;
	}




}
