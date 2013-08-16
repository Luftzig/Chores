package il.ac.huji.chores.dal;

import java.util.ArrayList;
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
import il.ac.huji.chores.exceptions.ApartmentAlreadyExistsException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

public class DALTestActivity extends Activity {

	@Override
	protected void onStart (){
		super.onStart();
		setContentView(R.layout.activity_daltest);
		Parse.initialize(this,
				this.getResources().getString(R.string.parse_app_id),
				this.getResources().getString(R.string.parse_client_key));
		ParseACL defaultACL = new ParseACL();
		defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
		//String roommateID =RoommateDAL.createRoommateUser("anna", "anna123","anna@gmail.com");
		RoommateDAL.Login("anna", "anna123");
		String apartmentID;
		try {
			apartmentID = RoommateDAL.getApartmentID();
		} catch (UserNotLoggedInException e1) {
			apartmentID = null;
		}
		/*RoommatesApartment apt = new RoommatesApartment();
		apt.setName("apt");
		*/
		List<String> roommates = new ArrayList<String>();
		//try {
			//apartmentID = ApartmentDAL.createApartment(apt);
			try {
				ApartmentDAL.addRoomateToApartment(apartmentID);
			} catch (UserNotLoggedInException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			roommates = ApartmentDAL.getApartmentRoommates(apartmentID);
		//}// catch (ApartmentAlreadyExistsException e) {
		//	Log.e("ApartmentAlreadyExistsException", e.toString());
		//} catch (UserNotLoggedInException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
		
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
