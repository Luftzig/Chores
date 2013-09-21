package il.ac.huji.chores.dal;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import il.ac.huji.chores.Chore;
import il.ac.huji.chores.Constants;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationsDAL {
	
	public static void notifyChoreDone(Chore chore,String sender,List<String> roommates) throws JSONException{
		// Create our Installation query
		ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
		pushQuery.whereContainedIn("username", roommates);
		List<String> channels = new ArrayList<String>();
		channels.add(Constants.PARSE_DONE_CHANNEL_KEY);
		pushQuery.whereContainedIn("channels", channels);
		 
		// Send push notification to query
		String message = sender + " finished the chore "+ chore.getName();
		JSONObject data = new JSONObject();
		data.put("data",buildDataJson(message,Constants.PARSE_DONE_CHANNEL_KEY));
		System.out.println("data:"+data.toString());
		ParsePush push = new ParsePush();
		//push.setQuery(pushQuery);
		push.setData(data);
		push.setChannel(Constants.PARSE_DONE_CHANNEL_KEY);
		push.setMessage(message);
		push.sendInBackground();
	}
	
	public static void notifyChoreMissed(Chore chore,String sender,List<String> roommates) throws JSONException{
		// Create our Installation query
		ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
		pushQuery.whereContainedIn("username", roommates);
		List<String> channels = new ArrayList<String>();
		channels.add(Constants.PARSE_MISSED_CHANNEL_KEY);
		pushQuery.whereContainedIn("channels", channels);
		//pushQuery.whereContains("channels", Constants.PARSE_MISSED_CHANNEL_KEY);
		 String message = sender + " missed the chore "+ chore.getName();
		JSONObject data = new JSONObject();
		data.put("data",buildDataJson(message,Constants.PARSE_MISSED_CHANNEL_KEY));
		System.out.println("data:"+data.toString());
		
		// Send push notification to query
		ParsePush push = new ParsePush();
		push.setQuery(pushQuery);
		push.setMessage(message);
		push.setData(data);

		System.out.println("push:"+push.toString());
		push.sendInBackground();
	}
	

	
	public static void notifySuggestChore(Chore chore,String sender,List<String> roommates) throws JSONException{
		// Create our Installation query
		ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
		pushQuery.whereContainedIn("username", roommates);
		List<String> channels = new ArrayList<String>();
		channels.add(Constants.PARSE_SUGGEST_CHANNEL_KEY);
		pushQuery.whereContainedIn("channels", channels);
		String message = sender + " suggest you to take the chore :"+ chore.getName() + " with deadline : "+chore.getDeadline();
		JSONObject data = new JSONObject();
		JSONObject dataContent = buildDataJson(message,Constants.PARSE_SUGGEST_CHANNEL_KEY);
		dataContent.put("choreId",chore.getId());
		data.put("data",dataContent);
		// Send push notification to query
		ParsePush push = new ParsePush();
		push.setQuery(pushQuery);
		push.setMessage(message);
		push.setData(data);
		push.sendInBackground();
	}
	
	public static void notifySuggestChoreAccepted(Chore chore,String sender,List<String> roommates) throws JSONException{
		// Create our Installation query
		ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
		pushQuery.whereContainedIn("username", roommates);
		String message = sender + " accepted to take your chore : "+ chore.getName();
		JSONObject data = new JSONObject();
		JSONObject dataContent = buildDataJson(message,null);
		dataContent.put("choreId",chore.getId());
		data.put("data",dataContent);
		// Send push notification to query
		ParsePush push = new ParsePush();
		push.setQuery(pushQuery);
		push.setMessage(message);
		push.setData(data);
		push.sendInBackground();
	}
	public static void notifySuggestStealChore(Chore chore,String sender,List<String> roommates) throws JSONException{
		// Create our Installation query
		ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
		pushQuery.whereContainedIn("username", roommates);
		List<String> channels = new ArrayList<String>();
		channels.add(Constants.PARSE_STEAL_CHANNEL_KEY);
		pushQuery.whereContainedIn("channels", channels);
		String message=sender + " suggest to steal your chore :"+ chore.getName();
		JSONObject data = new JSONObject();
		JSONObject dataContent = buildDataJson(message,Constants.PARSE_STEAL_CHANNEL_KEY);
		dataContent.put("choreId",chore.getId());
		data.put("data",dataContent);
		// Send push notification to query
		ParsePush push = new ParsePush();
		push.setQuery(pushQuery);
		push.setMessage(message);
		push.sendInBackground();
	}

	
	
	public static JSONObject buildDataJson( String message, String notificationType) throws JSONException{
		JSONObject json = new JSONObject();

		json.put("action","il.ac.huji.chores.ChoresNotification");
		if(notificationType!=null)
			json.put("notificationType", notificationType);
		json.put(Constants.PARSE_NOTIFICATION_MESSAGE, message);
		return json;
		
	}

}