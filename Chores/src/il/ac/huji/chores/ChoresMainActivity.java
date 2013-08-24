package il.ac.huji.chores;

import il.ac.huji.chores.dal.RoommateDAL;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ChoresMainActivity extends Activity {

	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_chores_main);
			
	        AppSetup setup = AppSetup.getInstance((Context) this);

			
	        String apartmentId = null;
	        try {
	            apartmentId = RoommateDAL.getApartmentID();
	        } catch (UserNotLoggedInException e) {
	            // TODO login
	        }
	        if (apartmentId == null) {
	            Intent intent = new Intent(this, NewApartmentDialogActivity.class);
	            startActivity(intent);
	            // TODO should get the apartmentID from the returned activity
	        }
	        // TODO remove this static login
	        
	 }
}
