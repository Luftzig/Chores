package il.ac.huji.chores.dal;
import com.parse.ParseException;

import android.util.Log;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import il.ac.huji.chores.Chore;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


import static il.ac.huji.chores.Constants.ParseChannelKeys.*;

public class NotificationsDAL {

    public static void notifyChoreDone(Chore chore, String sender, List<String> roommates) {
        // Create our Installation query
        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("channels", PARSE_DONE_CHANNEL_KEY.toString()); // Set the channel
        pushQuery.whereContainedIn("username", roommates);
        String msg = sender + " finished the chore " + chore.getName();
        JSONObject data = createDataJson(msg,PARSE_DONE_CHANNEL_KEY.toString());
      
        System.out.println("data:"+data.toString());
        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);
        push.setData(data);
      //  push.setMessage(sender + " finished the chore " + chore.getName());
        push.sendInBackground();
    }

    public static void notifyChoreMissed(Chore chore, String sender, List<String> roommates) {
        // Create our Installation query
        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("channels", PARSE_MISSED_CHANNEL_KEY.toString()); // Set the channel
        pushQuery.whereContainedIn("username", roommates);
        String msg=sender + " missed the chore " + chore.getName();
        JSONObject data = createDataJson(msg,PARSE_MISSED_CHANNEL_KEY.toString());
        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);
        push.setData(data);
        push.sendInBackground();
    }

    public static void notifySuggestStealChore(Chore chore, String sender, List<String> roommates) {
        // Create our Installation query
        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("channels", PARSE_STEAL_CHANNEL_KEY.toString()); // Set the channel
        pushQuery.whereContainedIn("username", roommates);
        String msg=sender + " missed the chore " + chore.getName();
        JSONObject data = createDataJson(msg,PARSE_STEAL_CHANNEL_KEY.toString());
        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);
        push.setData(data);
        push.sendInBackground();
    }

    public static void notifySuggestChore(Chore chore, String sender, List<String> roommates) {
        // Create our Installation query
        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("channels", PARSE_SUGGEST_CHANNEL_KEY.toString()); // Set the channel
        pushQuery.whereContainedIn("username", roommates);
        String msg=sender + " suggest you to take the chore :" + chore.getName();
        JSONObject data = createDataJson(msg,PARSE_SUGGEST_CHANNEL_KEY.toString());
        try {
			data.put("choreId", chore.getId());
			data.put("sender",sender);
			data.put("deadline",chore.getDeadline().getTime());
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);
        push.setData(data);
        push.sendInBackground();
    }

    public static void notifySuggestChoreAccepted(Chore chore, String sender, List<String> roommates) {
        // Create our Installation query
        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
        pushQuery.whereContainedIn("username", roommates);
        String msg=sender + " accepted to take your chore : " + chore.getName();
        JSONObject data = createDataJson(msg,PARSE_SUGGEST_ACCEPTED_CHANNEL_KEY.toString());
        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);
        push.setData(data);
        push.sendInBackground();
    }

    public static void notifyInvitationAccepted(String accepter, String apartmentId) {
        List<String> roommates = ApartmentDAL.getApartmentRoommates(apartmentId);
        Log.d("NotificationsDAL.notifyInvitationAccepted", "Accepted message will be sent to " + roommates.toString());
        ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
        query.whereEqualTo("channels", PARSE_JOINED_CHANNEL_KEY.toString()); // Set the channel
        query.whereContainedIn("username", roommates);
        ParsePush push = new ParsePush();
        push.setQuery(query);
        push.setMessage(accepter + " has joined the apartment!");
        push.sendInBackground();
    }
    
    public static JSONObject createDataJson(String msg,String notificationtype){
    	JSONObject data = new JSONObject();
    	try {
			data.put("action",  "il.ac.huji.chores.choresNotification");
			data.put("alert", msg);
			data.put("msg",msg);
			data.put("notificationType", notificationtype);
		} catch (JSONException e) {}
			
		
    	return data;
    }
}

