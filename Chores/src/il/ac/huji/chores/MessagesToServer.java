package il.ac.huji.chores;

import android.util.Log;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;

import java.util.HashMap;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
