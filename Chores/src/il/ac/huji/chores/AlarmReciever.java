package il.ac.huji.chores;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {		
		
		Intent dialogIntent = new Intent(context, AlarmDialog.class);
		dialogIntent.putExtra("alarmMsg", intent.getStringExtra("alarmMsg"));
		dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(dialogIntent);

	}

}
