package il.ac.huji.chores.dal;

import il.ac.huji.chores.Chore;
import il.ac.huji.chores.Constants;

import java.util.List;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

public class NotificationsDAL {
	
	public static void notifyChoreDone(Chore chore,String sender,List<String> roommates){
		// Create our Installation query
		ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
		pushQuery.whereEqualTo("channels",Constants.PARSE_DONE_CHANNEL_KEY); // Set the channel
		pushQuery.whereContainedIn("username", roommates);
		 
		// Send push notification to query
		ParsePush push = new ParsePush();
		push.setQuery(pushQuery);
		push.setMessage(sender + " finished the chore "+ chore.getName());
		push.sendInBackground();
	}
	
	public static void notifyChoreMissed(Chore chore,String sender,List<String> roommates){
		// Create our Installation query
		ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
		pushQuery.whereEqualTo("channels",Constants.PARSE_MISSED_CHANNEL_KEY); // Set the channel
		pushQuery.whereContainedIn("username", roommates);
		 
		// Send push notification to query
		ParsePush push = new ParsePush();
		push.setQuery(pushQuery);
		push.setMessage(sender + " missed the chore "+ chore.getName());
		push.sendInBackground();
	}
	
	public static void notifySuggestStealChore(Chore chore,String sender,List<String> roommates){
		// Create our Installation query
		ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
		pushQuery.whereEqualTo("channels",Constants.PARSE_STEAL_CHANNEL_KEY); // Set the channel
		pushQuery.whereContainedIn("username", roommates);
		 
		// Send push notification to query
		ParsePush push = new ParsePush();
		push.setQuery(pushQuery);
		push.setMessage(sender + " suggest to steal your chore :"+ chore.getName());
		push.sendInBackground();
	}
	
	public static void notifySuggestChore(Chore chore,String sender,List<String> roommates){
		// Create our Installation query
		ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
		pushQuery.whereEqualTo("channels",Constants.PARSE_SUGGEST_CHANNEL_KEY); // Set the channel
		pushQuery.whereContainedIn("username", roommates);
		 
		// Send push notification to query
		ParsePush push = new ParsePush();
		push.setQuery(pushQuery);
		push.setMessage(sender + " suggest you to take the chore :"+ chore.getName());
		push.sendInBackground();
	}
	
	public static void notifySuggestChoreAccepted(Chore chore,String sender,List<String> roommates){
		// Create our Installation query
		ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
		pushQuery.whereContainedIn("username", roommates);
		 
		// Send push notification to query
		ParsePush push = new ParsePush();
		push.setQuery(pushQuery);
		push.setMessage(sender + " accepted to take your chore : "+ chore.getName());
		push.sendInBackground();
	}
	
	public static void notifyStelChoreAccepted(Chore chore,String sender,List<String> roommates){
		// Create our Installation query
		ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
		pushQuery.whereContainedIn("username", roommates);
		 
		// Send push notification to query
		ParsePush push = new ParsePush();
		push.setQuery(pushQuery);
		push.setMessage(sender + " accepted to pass you chore : "+ chore.getName());
		push.sendInBackground();
	}

}
