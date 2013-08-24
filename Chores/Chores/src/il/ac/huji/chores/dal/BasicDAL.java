package il.ac.huji.chores.dal;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.parse.SaveCallback;

import il.ac.huji.chores.*;
import android.content.Context;
import android.util.Log;

public  class BasicDAL {
	
	protected static Context context;
	protected static String apartmentID;
	protected static String roommateID;
	
	public static void Setup(Context appContext){
		context = appContext;
		
		Parse.initialize(context,
				context.getResources().getString(R.string.parse_app_id),
				context.getResources().getString(R.string.parse_client_key));
		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		ParseACL.setDefaultACL(defaultACL, true);
		
		//push notifications 
		PushService.setDefaultPushCallback(context, PushNotificationsHandlerActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException arg0) {
				if(arg0 != null){
					Log.e("exception", arg0.getStackTrace().toString());
				}
				else{
					Log.e(" null", "nooooot");
				}
				
			}
		});
		
		roommateID = ParseUser.getCurrentUser().getObjectId();
		 
	}

//	public static void Setup(){
//
//		Parse.initialize(context,
//				"G3vUvpy8mCgucxXp2RnSnFBs490CqfpWItmfA9hc",
//				"CBnyHLzYGwlBz1YAOFMFakzalqWVosN3JRmpLmx4");
//		ParseUser.enableAutomaticUser();
//		ParseACL defaultACL = new ParseACL();
//		ParseACL.setDefaultACL(defaultACL, true);
//		
//		roommateID = ParseUser.getCurrentUser().getObjectId();
//		 
//	}
}
