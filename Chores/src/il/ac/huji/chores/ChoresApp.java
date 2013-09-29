package il.ac.huji.chores;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


import com.parse.Parse; 

public class ChoresApp extends Application { 
	
	public static final int PULL_ALARM_REPEAT_INTERVAL = 60 * 1000; // millis

    @Override 
    public void onCreate() { 
        super.onCreate();
        
        Context context = getApplicationContext();
        Parse.initialize(context,
                context.getResources().getString(R.string.parse_app_id),
                context.getResources().getString(R.string.parse_client_key));
        
        
        /**** schedule pull jobs ****/
        

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(context, PullSessionReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 10, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), PULL_ALARM_REPEAT_INTERVAL , pi);
    }
      



} 