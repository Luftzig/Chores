package il.ac.huji.chores;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.parse.*;
import il.ac.huji.chores.dal.RoommateDAL;

public class AppSetup {

    private static AppSetup instance;
    private Context context;
    private String username;
    private String password;

    private AppSetup(Context ctx) {
        context = ctx;
        Log.d("AppSetup", "Parse initialized");
        setupDAL();
        setupPushNotifications();
        setupActionBar();
        
		//start login activity (sign up inside)
		LoginActivity.OpenLoginScreen(context, true);
    }
    
    private void setupPushNotifications(){
//    	PushService.setDefaultPushCallback(context, PushNotificationsHandlerActivity.class);
    	PushService.subscribe(context, Constants.PARSE_NEW_CHORES_CHANNEL_KEY, ChoresMainActivity.class);
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        Log.d("AppSetup", "Installation objectId: " + installation.getObjectId() + "InstallationId: "
                + installation.getInstallationId());
        installation.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("setupPushNotifications", e.getMessage(), e);
                } else {
                    Log.e("setupPushNotifications", "saveInBackground succeeded");
                }

            }
        });
    }

    private void setupActionBar() {
    	
    	ActionBar bar = ((Activity) context).getActionBar();
    	bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    	String fragLabel;
    	
    	/** My chores tab handling **/
    	fragLabel = context.getResources().getString(R.string.action_bar_my_chores);
    	Tab myChoresTab = bar.newTab();
    	myChoresTab.setText(fragLabel);
    	myChoresTab.setTabListener(new ChoresTabListener());
    	bar.addTab(myChoresTab);
    	
    	/** Apartment tab handling **/
    	fragLabel = context.getResources().getString(R.string.action_bar_apartment);
    	Tab apartmentTab = bar.newTab();
    	apartmentTab.setText(fragLabel);
    	apartmentTab.setTabListener(new ChoresTabListener());
    	bar.addTab(apartmentTab);
    	
    	/** Statistics tab handling **/
    	fragLabel = context.getResources().getString(R.string.action_bar_statistics);
    	Tab statisticsTab = bar.newTab();
    	statisticsTab.setText(fragLabel);
    	statisticsTab.setTabListener(new ChoresTabListener());
    	bar.addTab(statisticsTab);
    	
    	/** Settings tab handling **/
    	fragLabel = context.getResources().getString(R.string.action_bar_settings);
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

    private String loginParse() throws ParseException{
    	return RoommateDAL.Login(username, password);
    }

    private void setupDAL(){

		Parse.initialize(context,
                context.getResources().getString(R.string.parse_app_id),
                context.getResources().getString(R.string.parse_client_key));
		ParseACL defaultACL = new ParseACL();
		defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
		
    }
}
