package il.ac.huji.chores;

import android.util.Log;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;

import java.util.HashMap;
import java.util.List;

public class MessagesToServer {

	/*
	 * Notify a a list of roommates.
	 * Kind - the kind of the message (roommate done/ roommate steal, ChoreInfos were added, etc)
	 * objectId - the message refers to the object with the parse object id objectId.
	 */
	public static void notifyRoomates(List<String> roommatesNames, String kind, String objectId){
		
		//TODO
		/////dummy... erase/////
		for(int i=0; i< roommatesNames.size(); i++){
			Log.e("notify roommate " + roommatesNames.get(i), "kind: "+ kind );
		}
		/////////////////////
	}

    /**
     * Send invitation to requested phone numbers
     * @param callback
     * @param name
     * @param phoneNumbers semicolon separated list of phone numbers
     */
    public static void invite(FunctionCallback callback, String name, String phoneNumbers) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("phone", phoneNumbers);
        ParseCloud.callFunctionInBackground("invite", params, callback);
    }

}
