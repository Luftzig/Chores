package il.ac.huji.chores.dal;
import com.parse.ParseException;

import android.util.Log;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import il.ac.huji.chores.Chore;

import java.util.ArrayList;
import java.util.List;


import static il.ac.huji.chores.Constants.ParseChannelKeys.*;

public class NotificationsDAL {

    public static void notifyChoreDone(Chore chore, String sender, List<String> roommates) {
        // Create our Installation query
        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
      //  pushQuery.whereEqualTo("channels", PARSE_DONE_CHANNEL_KEY.toString()); // Set the channel
        pushQuery.whereEqualTo("username", roommates.get(0));

        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);
        push.setMessage(sender + " finished the chore " + chore.getName());
        push.sendInBackground();
    }

    public static void notifyChoreMissed(Chore chore, String sender, List<String> roommates) {
        // Create our Installation query
        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
      //  pushQuery.whereEqualTo("channels", PARSE_MISSED_CHANNEL_KEY.toString()); // Set the channel
        pushQuery.whereEqualTo("username", roommates.get(0));

        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);
        push.setMessage(sender + " missed the chore " + chore.getName());
        push.sendInBackground();
    }

    public static void notifySuggestStealChore(Chore chore, String sender, List<String> roommates) {
        // Create our Installation query
        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("channels", PARSE_STEAL_CHANNEL_KEY.toString()); // Set the channel
        pushQuery.whereContainedIn("username", roommates);

        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);
        push.setMessage(sender + " suggest to steal your chore :" + chore.getName());
        push.sendInBackground();
    }

    public static void notifySuggestChore(Chore chore, String sender, List<String> roommates) {
        // Create our Installation query
        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("channels", PARSE_SUGGEST_CHANNEL_KEY.toString()); // Set the channel
        pushQuery.whereContainedIn("username", roommates);

        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);
        push.setMessage(sender + " suggest you to take the chore :" + chore.getName());
        push.sendInBackground();
    }

    public static void notifySuggestChoreAccepted(Chore chore, String sender, List<String> roommates) {
        // Create our Installation query
        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
        pushQuery.whereContainedIn("username", roommates);

        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);
        push.setMessage(sender + " accepted to take your chore : " + chore.getName());
        push.sendInBackground();
    }

    public static void notifyStealChoreAccepted(Chore chore, String sender, List<String> roommates) {
        // Create our Installation query
        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
        pushQuery.whereContainedIn("username", roommates);

        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);
        push.setMessage(sender + " accepted to pass you chore : " + chore.getName());
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
}

