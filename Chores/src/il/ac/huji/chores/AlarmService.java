package il.ac.huji.chores;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlarmService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		new AlarmsAsynctask(this).execute(intent.getLongExtra("createTime", -1));
		return Service.START_NOT_STICKY;
	}


	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	


}

