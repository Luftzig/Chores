package il.ac.huji.chores;

import android.app.Application;
import android.content.Context;

import com.parse.Parse; 

public class ChoresApp extends Application { 

    @Override 
    public void onCreate() { 
        super.onCreate();
        
        Context context = getApplicationContext();
        Parse.initialize(context,
                context.getResources().getString(R.string.parse_app_id),
                context.getResources().getString(R.string.parse_client_key));
    }
} 