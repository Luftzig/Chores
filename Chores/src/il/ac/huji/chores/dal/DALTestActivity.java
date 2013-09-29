package il.ac.huji.chores.dal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import il.ac.huji.chores.ApartmentChore;
import il.ac.huji.chores.Chore;
import il.ac.huji.chores.ChoreInfo;
import il.ac.huji.chores.ChoreInfo.CHORE_INFO_PERIOD;
import il.ac.huji.chores.ChoreInfoInstance;
import il.ac.huji.chores.ChoreStatistics;
import il.ac.huji.chores.Coins;
import il.ac.huji.chores.R;
import il.ac.huji.chores.exceptions.ChoreStatisticsException;
import il.ac.huji.chores.exceptions.DataNotFoundException;
import il.ac.huji.chores.exceptions.FailedToSaveOperationException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

//import il.ac.huji.chores.exceptions.ChoreStatisticsException;

public class DALTestActivity extends Activity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onStart() {
		super.onStart();
		setContentView(R.layout.activity_daltest);
		Parse.initialize(this,
				this.getResources().getString(R.string.parse_app_id), this
						.getResources().getString(R.string.parse_client_key));
		ParseACL defaultACL = new ParseACL();
		defaultACL.setPublicReadAccess(true);
		defaultACL.setPublicWriteAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);

		try {
			RoommateDAL.Login("Yoav Luft", "1234");
			ParseUser curr = ParseUser.getCurrentUser();
			Calendar c = Calendar.getInstance();
			c.set(2014, 1, 1);
			
			Date old =  c.getTime();
			curr.put("lastUpdate", old);
			curr.saveInBackground();
		//	PullNotificationsDAL.notifySuggestChore(chore, sender, roommates);
			List<JSONObject> results = PullNotificationsDAL.pullAllNotifications();
			
			//Date today = new Date();
			//List<Chore> results = ChoreDAL.getAllChoresCreatedAfter(today.getTime());
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.daltest, menu);
		return true;
	}

}
