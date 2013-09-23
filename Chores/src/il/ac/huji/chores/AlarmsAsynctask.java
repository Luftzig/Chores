package il.ac.huji.chores;

import java.util.Calendar;
import java.util.List;

import il.ac.huji.chores.dal.ApartmentSettingsDAL;
import il.ac.huji.chores.dal.ChoreDAL;
import il.ac.huji.chores.exceptions.FailedToGetApartmentSettings;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class AlarmsAsynctask extends AsyncTask<Long, Void, Void> {

	Context context;
	static private int id = 0;

	public AlarmsAsynctask(Context context){
		this.context = context;
	}

	@Override
	protected Void doInBackground(Long... createTime) {
		
		long create = createTime[0].longValue();
		if(create == -1){
			return null;
		}
		List<Chore> chores = ChoreDAL.getAllChoresCreatedAfter(create);
		Settings settings;
		try {
			settings = ApartmentSettingsDAL.getSettings();
		} catch (UserNotLoggedInException e) {
			LoginActivity.OpenLoginScreen(context, false);
			return null;
		} catch (FailedToGetApartmentSettings e) {
			return null;
		}
		if(settings.chores.disableRemindersAboutUpcomingChores){
			return null;
		}
		int rmndHours = settings.reminders.hours; // how many hours before deadline to remind

		for(int i=0; i<chores.size(); i++){
			
			//set reminder time in a calendar
			Calendar cal = Calendar.getInstance();
			cal.setTime(chores.get(i).getDeadline());
			cal.add(Calendar.HOUR, (-1)* (rmndHours));

			Intent intentAlarm = new Intent(context, AlarmReciever.class); 
			intentAlarm.putExtra("alarmMsg", "The deadline for the chore \n'" + chores.get(i).getName() + "'\nis in " + rmndHours + " hours");
			
			AlarmManager alarmManager = (AlarmManager)context. getSystemService(Context.ALARM_SERVICE);

			//set the alarm for particular time
			alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), PendingIntent.getBroadcast(context, id++,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
			
		}
		//context.stopService();

		return null;
	}

}
