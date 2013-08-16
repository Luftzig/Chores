package il.ac.huji.chores.dal;

import java.util.List;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import il.ac.huji.chores.R;
import il.ac.huji.chores.R.layout;
import il.ac.huji.chores.R.menu;
import il.ac.huji.chores.RoommatesApartment;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import il.ac.huji.chores.RoommatesApartment;

public class DALTestActivity extends Activity {

	@Override
	protected void onStart (){
		super.onStart();
		Log.d("test","test");
		System.out.println("test!");
		Parse.initialize(this,
				this.getResources().getString(R.string.parse_app_id),
				this.getResources().getString(R.string.parse_client_key));
		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		ParseACL.setDefaultACL(defaultACL, true);
		setContentView(R.layout.activity_daltest);
		RoommatesApartment apt = new RoommatesApartment();
		apt.setName("apt");
		String apartmentID = ApartmentDAL.createApartment(apt);
		List<String> roommates = ApartmentDAL.getApartmentRoomates(apartmentID);
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
