package il.ac.huji.chores;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.parse.*;
import il.ac.huji.chores.dal.RoommateDAL;

import javax.annotation.Nullable;

public class AppSetup {

    private static AppSetup instance;
    private Context context;

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
    	//PushService.setDefaultPushCallback(_ctx, ChoresMainActivity.class);
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
    	
    	ActionBar bar = ((Activity)context).getActionBar();
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

    public static ActionBar getActionBar() {
        if (instance != null && instance.context != null && instance.context instanceof ChoresMainActivity) {
            return ((ChoresMainActivity)instance.getContext()).getActionBar();
        } else {
            Log.e("AppSetup.getActionBar", "Not initialized or context is null or context is not ChoresMainActivity");
            return null;
        }
    }

    /**
     * @return context with which the AppSetup was created, should be {@link ChoresMainActivity}, or null if AppSetup
     * was not initialized.
     */
    @Nullable
    public static Context getMainActivityContext() {
        if (instance != null) {
            return instance.getContext();
        } else {
            return null;
        }
    }

    public static AppSetup getInstance(Context ctx) {
        if (instance == null) {
            instance = new AppSetup(ctx);
        }
        return instance;
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

    /**
     * @return context with which the AppSetup was created, should be {@link ChoresMainActivity}
     */
    public Context getContext() {
        return context;
    }
}
