package il.ac.huji.chores;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class AlarmDialog extends Activity {
	
	@Override
    protected void onCreate(Bundle bundle) {
		
		super.onCreate(bundle);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alarm_dialog);
		
		TextView view = (TextView)findViewById(R.id.AlarmDialog_msg);
		Intent intent = getIntent();
		view.setText(intent.getStringExtra("alarmMsg"));
	
		Button button = (Button)findViewById(R.id.AlarmDialog_button);
		
		Uri alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarm);
		r.play();
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				r.stop();
				finish();
				
			}
		});
	}
	
}
