package il.ac.huji.chores;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.google.common.collect.Lists;
import il.ac.huji.chores.dal.PullNotificationsDAL;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PullSessionReceiver extends BroadcastReceiver {

    private ChoresMainActivity activityContext;
    private ChoresBroadcastReceiver choresBroadcastReceiver;

    public PullSessionReceiver() {
        Log.d("PullSessionReceiver.constructor", "");
    }

    @Override
	public void onReceive(final Context context, final Intent intent) {
        activityContext = (ChoresMainActivity) AppSetup.getMainActivityContext();
        if(activityContext == null){
        	return;
        }
        choresBroadcastReceiver = activityContext.getReceiver();
        final List<JSONObject> notificationJson = Lists.newArrayList();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                for (JSONObject jsonObject : notificationJson) {
                    intent.putExtra("com.parse.Data", jsonObject.toString());
                    choresBroadcastReceiver.onReceive(context, intent);
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                notificationJson.addAll(PullNotificationsDAL.pullAllNotifications());
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }.execute();
    }

//    private void notifyMessage(String title, String message, Class targetActivity) {
//        Notification.Builder builder = new Notification.Builder(activityContext)
//                .setSmallIcon(R.drawable.icon48)
//                .setContentTitle(title)
//                .setContentText(message);
//        Intent targetIntent = new Intent(activityContext, targetActivity);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(activityContext);
//        stackBuilder.addParentStack(targetActivity);
//        stackBuilder.addNextIntent(targetIntent);
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);
//        NotificationManager notificationManager =
//                (NotificationManager) activityContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0, builder.build());
//    }
}
