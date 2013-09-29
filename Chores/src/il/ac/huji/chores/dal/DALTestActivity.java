package il.ac.huji.chores.dal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;

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
			RoommateDAL.Login("Yoav", "1234");
			/*Chore chore = new ApartmentChore();
			chore.setApartment("apt id");
			chore.setName("chore to pull");
			chore.setId("chore ID");
			String sender = "anna";
			List<String> roommates = new ArrayList<String>();
			roommates.add("Shani");
			roommates.add("Yoav");*/
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
