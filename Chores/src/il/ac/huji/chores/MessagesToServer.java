package il.ac.huji.chores;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;

import java.util.HashMap;

public class MessagesToServer {
	
	

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
