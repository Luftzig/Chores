package il.ac.huji.chores;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import il.ac.huji.chores.dal.ApartmentDAL;
import il.ac.huji.chores.dal.NotificationsDAL;
import il.ac.huji.chores.dal.RoommateDAL;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;
import org.json.JSONException;
import org.json.JSONObject;
import il.ac.huji.chores.dal.ApartmentDAL;
import il.ac.huji.chores.dal.NotificationsDAL;
import il.ac.huji.chores.dal.RoommateDAL;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;
import org.json.JSONException;
import org.json.JSONObject;

public class ChoresMainActivity extends Activity {

    static public boolean mainActivityRunning = false;
    ActivityBroadcastReceiver receiver;
    ActivityBroadcastReceiver receiver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chores_main);
        mainActivityRunning = true;

        AppSetup.getInstance(this);

        receiver = new ActivityBroadcastReceiver();
        registerReceiver(receiver, new IntentFilter("il.ac.huji.chores.choresNotification"));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3) {
            //login/signup ok, create apartment
            String apartmentId = null;
            try {
                apartmentId = RoommateDAL.getApartmentID();
            } catch (UserNotLoggedInException e) {
                LoginActivity.OpenLoginScreen(this, false);
            }
            if (apartmentId == null) {
                Intent intent = new Intent(this, NewApartmentDialogActivity.class);
                startActivity(intent);
                // TODO should get the apartmentID from the returned activity
            }

            AppSetup.getInstance(this).setupActionBar();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void showNotificationDialog(final boolean onRightTab, int chosenTabLocation,
                                        final String type, final JSONObject jsonData) {
        final int chosen = chosenTabLocation;
        String dialogMsg;
        try {
            dialogMsg = jsonData.getString("msg");
        } catch (JSONException e) {
            Log.w("showNotificationDialog", "Failed to find message");
            return;
        }
        switch (Constants.ParseChannelKeys.valueOf(type)) {
            case PARSE_SUGGEST_CHANNEL_KEY:
                positiveButtonTxt = getResources().getString(R.string.suggest_positive_response);
                negativeButtonTxt = getResources().getString(R.string.suggest_negative_response);
                isNegButton = true;
                break;
            case PARSE_INVITATION_CHANNEL_KEY:
                positiveButtonTxt = getResources().getString(R.string.invitation_positive_response);
                negativeButtonTxt = getResources().getString(R.string.invitation_negative_response);
                isNegButton = true;
                break;
            default:
                Log.d("showNotificationDialog", "type " + type + " is not handled");
        }
        builder.setPositiveButton(positiveButtonTxt, new DialogInterface.OnClickListener() {

    enum ACTION_BAR_TABS_ORDER {
        MY_CHORES,
        APARTMENT,
        STATISTICS,
        SETTINGS
    }

    private class ActivityBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ActionBar bar = ChoresMainActivity.this.getActionBar();
            ActionBar.Tab curSelected = bar.getSelectedTab();
            JSONObject jsonData;
            try {
                jsonData = new JSONObject(intent.getExtras().getString("com.parse.Data"));

                String type = jsonData.getString("notificationType");
                boolean onRightTab = (curSelected.getText().equals(getResources().getString(R.string.action_bar_apartment)) == false);
                int nextTab = 0;

                switch (Constants.ParseChannelKeys.valueOf(type)) {
                    case PARSE_NEW_CHORES_CHANNEL_KEY:
                        nextTab = ACTION_BAR_TABS_ORDER.MY_CHORES.ordinal();
                        break;
                    case PARSE_INVITATION_CHANNEL_KEY:
                        nextTab = ACTION_BAR_TABS_ORDER.APARTMENT.ordinal();
                        break;
                    default:
                        nextTab = ACTION_BAR_TABS_ORDER.APARTMENT.ordinal();
                }
                showNotificationDialog(onRightTab, nextTab, type, jsonData);

            } catch (JSONException e) {
                return;
            }
        }
    }
}


