package il.ac.huji.chores.dal;

import static il.ac.huji.chores.Constants.ParseChannelKeys.PARSE_DONE_CHANNEL_KEY;
import static il.ac.huji.chores.Constants.ParseChannelKeys.PARSE_JOINED_CHANNEL_KEY;
import static il.ac.huji.chores.Constants.ParseChannelKeys.PARSE_MISSED_CHANNEL_KEY;
import static il.ac.huji.chores.Constants.ParseChannelKeys.PARSE_STEAL_CHANNEL_KEY;
import static il.ac.huji.chores.Constants.ParseChannelKeys.PARSE_SUGGEST_ACCEPTED_CHANNEL_KEY;
import static il.ac.huji.chores.Constants.ParseChannelKeys.PARSE_SUGGEST_CHANNEL_KEY;
import il.ac.huji.chores.Chore;
import il.ac.huji.chores.Constants.ParseChannelKeys;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import javax.annotation.Nullable;

public class PullNotificationsDAL {

	public static void notifyChoreDone(Chore chore, String sender,
			List<String> roommates) {
		NotificationsDAL.notifyChoreDone(chore,sender,roommates);
		ParseObject notification = new ParseObject("Notifications");
		notification.put("sender", sender);
		notification.put("info", chore.getName());
		notification.put("target", roommates);
		notification.put("type", PARSE_DONE_CHANNEL_KEY.toString());
		notification.saveInBackground();

	}

	public static void notifyChoreMissed(Chore chore, String sender,
			List<String> roommates) {
		NotificationsDAL.notifyChoreMissed(chore,sender,roommates);
		ParseObject notification = new ParseObject("Notifications");
		notification.put("sender", sender);
		notification.put("info", chore.getName());
		notification.put("target", roommates);
		notification.put("type", PARSE_MISSED_CHANNEL_KEY.toString());
		notification.saveInBackground();
	}

	public static void notifySuggestStealChore(Chore chore, String sender,
			List<String> roommates) {
		NotificationsDAL.notifySuggestStealChore(chore,sender,roommates);
		ParseObject notification = new ParseObject("Notifications");
		notification.put("sender", sender);
		notification.put("info", chore.getName());
		notification.put("target", roommates);
		notification.put("type", PARSE_STEAL_CHANNEL_KEY.toString());
		notification.saveInBackground();

	}

	public static void notifySuggestChore(Chore chore, String sender,
			List<String> roommates) {
		NotificationsDAL.notifySuggestChore(chore,sender,roommates);
		ParseObject notification = new ParseObject("Notifications");
		notification.put("sender", sender);
		JSONObject json = new JSONObject();
		try {
			json.put("name", chore.getName());
			json.put("deadline", chore.getDeadline().getTime());
			json.put("choreId", chore.getId());
		} catch (JSONException ignored) {

		}
		notification.put("info", json.toString());
		notification.put("target", roommates);
		notification.put("type", PARSE_SUGGEST_CHANNEL_KEY.toString());
		try {
			notification.save();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void notifySuggestChoreAccepted(Chore chore, String sender,
			List<String> roommates) {
		NotificationsDAL.notifySuggestChoreAccepted(chore,sender,roommates);
		ParseObject notification = new ParseObject("Notifications");
		notification.put("sender", sender);
		notification.put("info", chore.getName());
		notification.put("target", roommates);
		notification.put("type", PARSE_SUGGEST_ACCEPTED_CHANNEL_KEY.toString());
		notification.saveInBackground();
	}

	public static void notifyInvitationAccepted(String accepter,
			String apartmentId) throws ParseException {
		List<String> roommates = ApartmentDAL
				.getApartmentRoommates(apartmentId);

		ParseObject notification = new ParseObject("Notifications");
		notification.put("sender", accepter);
		notification.put("info", "");
		notification.put("target", roommates);
		notification.put("type", PARSE_JOINED_CHANNEL_KEY.toString());
		notification.saveInBackground();

	}

    /**
     * @return a list of JSON objects representing queued message, or null if Parse is not ready yet
     */
    @Nullable
	public static List<JSONObject> pullAllNotifications() {
		ParseUser currUser = ParseUser.getCurrentUser();
        if (currUser == null) {
            return null;
        }
        List<String> target = new ArrayList<String>();
		target.add(currUser.getUsername());
		List<JSONObject> notifications = new ArrayList<JSONObject>();
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
				"Notifications");
		Date lastUpdate =  currUser.getDate("lastUpdate");
		query.whereContainedIn("target", target).whereGreaterThan("createdAt",lastUpdate);

		List<ParseObject> results;
		try {
			results = query.find();
		} catch (ParseException e) {
			return notifications;
		}
		for (ParseObject result : results) {
			JSONObject jsonRes = handleParseObject(result);
			if (jsonRes != null){
				notifications.add(jsonRes);
				System.out.println("json:"+jsonRes.toString());
			}
		}
		currUser.put("lastUpdate", new Date());
		currUser.saveInBackground();
		return notifications;
	}

	private static JSONObject handleParseObject(ParseObject obj) {

		JSONObject notification = new JSONObject();

		String type = obj.getString("type");
		try {
			notification.put("notificationType", type);
			JSONObject info;
			String msg = "";
			switch (ParseChannelKeys.valueOf(type)) {
			case PARSE_DONE_CHANNEL_KEY:
				msg = obj.getString("sender") + " finished the chore "
						+ obj.getString("info");
				break;
				
			case PARSE_NEW_CHORES_CHANNEL_KEY:
				msg = "new chores has been divided";
				info = new JSONObject(obj.getString("info"));
	            notification.put("updateTime", info.getString("updateTime"));
	            break;

			case PARSE_MISSED_CHANNEL_KEY:
				msg = obj.getString("sender") + " missed the chore "
						+ obj.getString("info");
				break;

			case PARSE_STEAL_CHANNEL_KEY:
				msg = obj.getString("sender") + " stole the chore "
						+ obj.getString("info");
				break;

			case PARSE_SUGGEST_CHANNEL_KEY:
				JSONObject json = new JSONObject(obj.getString("info"));
				msg = obj.getString("sender")
						+ " suggest you to take the chore :"
						+ json.getString("name");

				notification.put("choreId", json.getString("choreId"));
				notification.put("sender", obj.getString("sender"));
				notification.put("deadline", json.getLong("deadline"));
				break;

			case PARSE_SUGGEST_ACCEPTED_CHANNEL_KEY:
				msg = obj.getString("sender")
						+ " accepted to take your chore : "
						+ obj.getString("info");
				break;

			case PARSE_JOINED_CHANNEL_KEY:
				msg = obj.getString("sender") + " has joined the apartment!";
				break;

			case PARSE_INVITATION_CHANNEL_KEY:
				if(RoommateDAL.getApartmentID() != null)
				{
					return null;
				}
				msg = "You were invited to join " + obj.getString("sender") + "'s apartment.";
                info = new JSONObject(obj.getString("info"));
                notification.put("apartmentId", info.getString("apartmentId"));
				break;
			}
			notification.put("msg", msg);
		} catch (JSONException e1) {
			return null;
		} catch (UserNotLoggedInException e) {
			return null;
		}
		return notification;

	}
}
