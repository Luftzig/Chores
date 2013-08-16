package il.ac.huji.chores;

import il.ac.huji.chores.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class PushNotificationsHandlerActivity extends Activity {

	public class AddEditChoresActivity extends Activity{
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Log.d("int handler", "success");
		}

	}
}
