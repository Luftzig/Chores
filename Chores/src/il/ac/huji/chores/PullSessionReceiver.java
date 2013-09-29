package il.ac.huji.chores;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import il.ac.huji.chores.dal.PullNotificationsDAL;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PullSessionReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
        List<JSONObject> notificationJson = PullNotificationsDAL.pullAllNotifications();
        for (JSONObject jsonObject : notificationJson) {
            handleNotificationJson(jsonObject);
        }
    }

    private void handleNotificationJson(JSONObject jsonObject) {
        String sender;
        JSONObject info;
        Constants.ParseChannelKeys type;
        try {
            sender = jsonObject.getString("sender");
            info = jsonObject.getJSONObject("info");
            type = Constants.ParseChannelKeys.valueOf(jsonObject.getString("type"));
        } catch (JSONException e) {
            Log.w("PullSessionReceiver.handleNotificationJson", "Failed to extract fields", e);
            return;
        }
        switch (type) {

            case PARSE_DONE_CHANNEL_KEY:
                handleDone(sender, info);
                break;
            case PARSE_MISSED_CHANNEL_KEY:
                handleMissed(sender, info);
                break;
            case PARSE_STEAL_CHANNEL_KEY:
                handleSteal(sender, info);
                break;
            case PARSE_SUGGEST_CHANNEL_KEY:
                handleSuggest(sender, info);
                break;
            case PARSE_NEW_CHORES_CHANNEL_KEY:
                handleNewChore(sender, info);
                break;
            case PARSE_SUGGEST_ACCEPTED_CHANNEL_KEY:
                handleAcceptSuggest(sender, info);
                break;
            case PARSE_INVITATION_CHANNEL_KEY:
                handleInvitation(sender, info);
                break;
            case PARSE_JOINED_CHANNEL_KEY:
                handleJoined(sender, info);
                break;
        }
    }

    private void handleJoined(String sender, JSONObject info) {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void handleInvitation(String sender, JSONObject info) {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void handleAcceptSuggest(String sender, JSONObject info) {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void handleNewChore(String sender, JSONObject info) {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void handleSuggest(String sender, JSONObject info) {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void handleSteal(String sender, JSONObject info) {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void handleMissed(String sender, JSONObject info) {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void handleDone(String sender, JSONObject info) {
        //To change body of created methods use File | Settings | File Templates.
    }

}
