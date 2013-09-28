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
    private ActionBar actionBar;

    private AppSetup(Context ctx) {
        context = ctx;
        Log.d("AppSetup", "Parse initialized");
        setupDAL();
        
      //start login activity (sign up inside)
        if (ParseUser.getCurrentUser() == null) {
            LoginActivity.OpenLoginScreen(context, true);
        } else {
            Log.d("AppSetup.constructor", "Already logged in as " + ParseUser.getCurrentUser().getUsername());
        }

        setupPushNotifications();
    }

    public static Context getApplicationContext() {
        return instance.context.getApplicationContext();
    }

    /**
     * Return the context that was used to create this AppSetup, usually the launcher activity.
     * @return
     */
    public static Context getContext() {
        return instance.context;
    }

    public static ActionBar getActionBar() {
        return instance.actionBar;
    }

    private void destory() {
        context = null;
    }

    public static void destroy() {
        instance.destory();
        instance = null;
    }

    //VERY IMPORTANT COMMENT: If you change the channel subscribing, you must update the un-subscribing to this channel!!!
    //(the un-subscribing is done on the app setting)
    private void setupPushNotifications(){
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
    	PushService.subscribe(context,
                Constants.ParseChannelKeys.PARSE_NEW_CHORES_CHANNEL_KEY.toString(), ChoresMainActivity.class);
    	PushService.subscribe(context,
                Constants.ParseChannelKeys.PARSE_STEAL_CHANNEL_KEY.toString(), ApartmentActivity.class);
    	PushService.subscribe(context,
                Constants.ParseChannelKeys.PARSE_MISSED_CHANNEL_KEY.toString(), ApartmentActivity.class);
    	PushService.subscribe(context,
                Constants.ParseChannelKeys.PARSE_DONE_CHANNEL_KEY.toString(), ApartmentActivity.class);
    	PushService.subscribe(context,
                Constants.ParseChannelKeys.PARSE_SUGGEST_CHANNEL_KEY.toString(), ApartmentActivity.class);
    	PushService.subscribe(context,
                Constants.ParseChannelKeys.PARSE_SUGGEST_ACCEPTED_CHANNEL_KEY.toString(), ApartmentActivity.class);
        PushService.setDefaultPushCallback(context, ChoresMainActivity.class);

    	
    	ParseInstallation install = ParseInstallation.getCurrentInstallation();
    	install.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("setupPushNotifications", e.getMessage(), e);
                } else {
                    Log.i("setupPushNotifications", "saveInBackground succeeded");
                }

            }
        });
    }

    public void setupActionBar() {
        actionBar = ((Activity)context).getActionBar();
    	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    	String fragLabel;
    	
    	/** My chores tab handling **/
    	fragLabel = context.getResources().getString(R.string.action_bar_my_chores);
    	Tab myChoresTab = actionBar.newTab();
    	myChoresTab.setText(fragLabel);
    	myChoresTab.setTabListener(new ChoresTabListener());
    	actionBar.addTab(myChoresTab);
    	
    	/** Apartment tab handling **/
    	fragLabel = context.getResources().getString(R.string.action_bar_apartment);
    	Tab apartmentTab = actionBar.newTab();
    	apartmentTab.setText(fragLabel);
    	apartmentTab.setTabListener(new ChoresTabListener());
    	actionBar.addTab(apartmentTab);
    	
    	/** Statistics tab handling **/
    	fragLabel = context.getResources().getString(R.string.action_bar_statistics);
    	Tab statisticsTab = actionBar.newTab();
    	statisticsTab.setText(fragLabel);
    	statisticsTab.setTabListener(new ChoresTabListener());
    	actionBar.addTab(statisticsTab);
    	
    	/** Settings tab handling **/
    	fragLabel = context.getResources().getString(R.string.action_bar_settings);
    	Tab settingsTab = actionBar.newTab();
    	settingsTab.setText(fragLabel);
    	settingsTab.setTabListener(new ChoresTabListener());
    	actionBar.addTab(settingsTab);
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

//		Parse.initialize(context,
//                context.getResources().getString(R.string.parse_app_id),
//                context.getResources().getString(R.string.parse_client_key));
		ParseACL defaultACL = new ParseACL();
		defaultACL.setPublicReadAccess(true);
		defaultACL.setPublicWriteAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
    }
}
