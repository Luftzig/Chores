package il.ac.huji.chores.dal;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import il.ac.huji.chores.*;
import android.content.Context;

public  class BasicDAL {
	
	protected static Context context;
	protected static String apartmentID;
	protected static String roommateID;
	//'protected static BasicDAL basicDAL = new BasicDAL();
	protected BasicDAL(Context appContext){
		context = appContext;
		
		Parse.initialize(context,
				context.getResources().getString(R.string.parse_app_id),
				context.getResources().getString(R.string.parse_client_key));
		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		ParseACL.setDefaultACL(defaultACL, true);
		
		roommateID = ParseUser.getCurrentUser().getObjectId();
	}

	
}
