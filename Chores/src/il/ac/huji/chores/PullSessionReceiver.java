package il.ac.huji.chores;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import il.ac.huji.chores.dal.PullNotificationsDAL;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PullSessionReceiver extends BroadcastReceiver {

    private final ChoresMainActivity activityContext;
    private final ChoresBroadcastReceiver choresBroadcastReceiver;

    public PullSessionReceiver() {
        activityContext = (ChoresMainActivity) AppSetup.getMainActivityContext();
        choresBroadcastReceiver = activityContext.getReceiver();
        Log.d("PullSessionReceiver.constructor", "");
    }

    @Override
	public void onReceive(Context context, Intent intent) {
        List<JSONObject> notificationJson = PullNotificationsDAL.pullAllNotifications();
        for (JSONObject jsonObject : notificationJson) {
            intent.putExtra("com.parse.Data", jsonObject.toString());
            choresBroadcastReceiver.onReceive(context, intent);
        }
    }

    private void notifyMessage(String title, String message, Class targetActivity) {
        Notification.Builder builder = new Notification.Builder(activityContext)
                .setSmallIcon(R.drawable.icon48)
                .setContentTitle(title)
                .setContentText(message);
        Intent targetIntent = new Intent(activityContext, targetActivity);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(activityContext);
        stackBuilder.addParentStack(targetActivity);
        stackBuilder.addNextIntent(targetIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) activityContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
