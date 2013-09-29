package il.ac.huji.chores;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;

public class GcmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.
System.out.println("ON RECIEVE");
        context.startService(intent);

        setResultCode(Activity.RESULT_OK);
    }
}