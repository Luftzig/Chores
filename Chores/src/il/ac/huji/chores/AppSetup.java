package il.ac.huji.chores;

import il.ac.huji.chores.dal.RoommateDAL;
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
        loginParse();
        //  PushService.subscribe(_ctx, "", AppSetup.class);
        //  PushService.setDefaultPushCallback(_ctx, AppSetup.class);
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
		//push notifications 
		PushService.setDefaultPushCallback(_ctx, PushNotificationsHandlerActivity.class);
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
		
    	
    }
}
