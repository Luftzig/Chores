package il.ac.huji.chores.dal;

import static il.ac.huji.chores.Constants.ParseChannelKeys.PARSE_DONE_CHANNEL_KEY;
import static il.ac.huji.chores.Constants.ParseChannelKeys.PARSE_JOINED_CHANNEL_KEY;
import static il.ac.huji.chores.Constants.ParseChannelKeys.PARSE_MISSED_CHANNEL_KEY;
import static il.ac.huji.chores.Constants.ParseChannelKeys.PARSE_STEAL_CHANNEL_KEY;
import static il.ac.huji.chores.Constants.ParseChannelKeys.PARSE_SUGGEST_ACCEPTED_CHANNEL_KEY;
import static il.ac.huji.chores.Constants.ParseChannelKeys.PARSE_SUGGEST_CHANNEL_KEY;
import il.ac.huji.chores.Chore;

import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;


public class PullNotificationsDAL extends NotificationsDAL {

	public static void notifyChoreDone(Chore chore, String sender,
			List<String> roommates) {

		ParseObject notification = new ParseObject("Notifications");
		notification.put("sender", sender);
		notification.put("info", chore.getName());
		notification.put("target", roommates);
		notification.put("type", PARSE_DONE_CHANNEL_KEY.toString());
		notification.saveInBackground(); 

	}

	public static void notifyChoreMissed(Chore chore, String sender,
			List<String> roommates) {

		ParseObject notification = new ParseObject("Notifications");
		notification.put("sender", sender);
		notification.put("info", chore.getName());
		notification.put("target", roommates);
		notification.put("type", PARSE_MISSED_CHANNEL_KEY.toString());
		notification.saveInBackground();
	}

	public static void notifySuggestStealChore(Chore chore, String sender,
			List<String> roommates) {

		ParseObject notification = new ParseObject("Notifications");
		notification.put("sender", sender);
		notification.put("info", chore.getName());
		notification.put("target", roommates);
		notification.put("type", PARSE_STEAL_CHANNEL_KEY.toString());
		notification.saveInBackground();

	}

	public static void notifySuggestChore(Chore chore, String sender,
			List<String> roommates) {
		ParseObject notification = new ParseObject("Notifications");
		notification.put("sender", sender);
		notification.put("info", chore.getId());
		notification.put("target", roommates);
		notification.put("type", PARSE_SUGGEST_CHANNEL_KEY.toString());
		notification.saveInBackground();
	}

	public static void notifySuggestChoreAccepted(Chore chore, String sender,
			List<String> roommates) {

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

}
