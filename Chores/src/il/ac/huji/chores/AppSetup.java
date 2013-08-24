package il.ac.huji.chores;

import il.ac.huji.chores.dal.RoommateDAL;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

public class AppSetup {

    private static AppSetup instance;
    private Context _ctx;
    private String _username;
    private String _password;

    private AppSetup(Context ctx) {
        _ctx = ctx;
        //Parse.initialize(_ctx, _ctx.getResources().getString(R.string.parse_app_id), _ctx.getResources().getString(R.string.parse_client_key));
        Log.d("AppSetup", "Parse initialized");
        setupDAL();
        // loginParse();
        //  PushService.subscribe(_ctx, "", AppSetup.class);
        //  PushService.setDefaultPushCallback(_ctx, AppSetup.class);
        setupActionBar();
        
    }

    private void setupActionBar() {
    	
    	ActionBar bar = ((Activity)_ctx).getActionBar();
    	bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    	String fragLabel = null;
    	
    	/** My chores tab handling **/
    	fragLabel = ((Activity)_ctx).getResources().getString(R.string.action_bar_my_chores);
    	Tab myChoresTab = bar.newTab();
    	myChoresTab.setText(fragLabel);
    	myChoresTab.setTabListener(new ChoresTabListener());
    	bar.addTab(myChoresTab);
    	
    	/** Apartment tab handling **/
    	fragLabel = ((Activity)_ctx).getResources().getString(R.string.action_bar_apartment);
    	Tab apartmentTab = bar.newTab();
    	apartmentTab.setText(fragLabel);
    	apartmentTab.setTabListener(new ChoresTabListener());
    	bar.addTab(apartmentTab);
    	
    	/** Statistics tab handling **/
    	fragLabel = ((Activity)_ctx).getResources().getString(R.string.action_bar_statistics);
    	Tab statisticsTab = bar.newTab();
    	statisticsTab.setText(fragLabel);
    	statisticsTab.setTabListener(new ChoresTabListener());
    	bar.addTab(statisticsTab);
    	
    	/** Settings tab handling **/
    	fragLabel = ((Activity)_ctx).getResources().getString(R.string.action_bar_settings);
    	Tab settingsTab = bar.newTab();
    	settingsTab.setText(fragLabel);
    	settingsTab.setTabListener(new ChoresTabListener());
    	bar.addTab(settingsTab);
		
	}

	public static AppSetup getInstance(Context ctx) {
        if (instance == null) {
            instance = new AppSetup(ctx);
        }
        return instance;
    }

    private String loginParse(){
    	return RoommateDAL.Login(_username, _password);
    }

    private void setupDAL(){

		Parse.initialize(_ctx,
				_ctx.getResources().getString(R.string.parse_app_id),
				_ctx.getResources().getString(R.string.parse_client_key));
		ParseACL defaultACL = new ParseACL();
		defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
		RoommateDAL.Login("anna", "anna123");
		
//    	PushService.setDefaultPushCallback(_ctx, PushNotificationsHandlerActivity.class);
//    	ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
//
//			@Override
//			public void done(ParseException arg0) {
//				if(arg0 != null){
//					Log.e("Exception", arg0.getStackTrace().toString());
//				}
//				else{
//					Log.e(" Null", "saveInBackground succeeded");
//				}
//	
//			}
//		});
    	
    }
}
