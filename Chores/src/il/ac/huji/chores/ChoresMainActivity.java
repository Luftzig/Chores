package il.ac.huji.chores;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import il.ac.huji.chores.dal.ApartmentDAL;
import il.ac.huji.chores.dal.ApartmentSettingsDAL;
import il.ac.huji.chores.dal.NotificationsDAL;
import il.ac.huji.chores.dal.RoommateDAL;
import il.ac.huji.chores.exceptions.FailedToGetApartmentSettings;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;
import org.json.JSONException;
import org.json.JSONObject;

public class ChoresMainActivity extends Activity {

    static public boolean mainActivityRunning = false;
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(dialogMsg);
        String positiveButtonTxt = "OK";
        String negativeButtonTxt = null;
        boolean isNegButton = false;

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

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if (!onRightTab) {
                    ActionBar bar = getActionBar();
                    bar.getTabAt(chosen).select();
                }

                //If another things needs to be done, call function here
                switch (Constants.ParseChannelKeys.valueOf(type)) {
                    case PARSE_SUGGEST_CHANNEL_KEY:
                        	try {
						ApartmentChoresFragment.doSuggestionAccepted(jsonData.get("choreId").toString(), getApplicationContext());
					} catch (JSONException e) {
						return;
					}
                        break;
                    case PARSE_INVITATION_CHANNEL_KEY:
                        acceptApartmentInvitation(jsonData);
                        break;
                    default:
                        Log.d("showNotificationDialog", "push of type " + type + " was not handled");
                }
            }
        });

        if (isNegButton) {
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }

            });
        }

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void acceptApartmentInvitation(JSONObject jsonData) {
        try {
            String apartmentId = jsonData.getString("apartmentId");
            ApartmentDAL.addRoommateToApartment(apartmentId);
            NotificationsDAL.notifyInvitationAccepted(RoommateDAL.getRoomateUsername(), apartmentId);
        } catch (UserNotLoggedInException e) {
            LoginActivity.OpenLoginScreen(this, false);
        } catch (JSONException e) {
            Log.w("acceptApartmentInvitation", "Error occured when trying to accept invitation", e);
            Log.d("acceptApartmentInvitation", "Offending JSON: " + jsonData.toString());
            showErrorDialog(getResources().getString(R.string.failed_to_join_apartment), e);
        }
    }

    /**
     * This should called to inform the user on errors, and allow reporting. Not implemented yet.
     * @param message error message to display.
     * @param cause error cause, if any
     */
    private void showErrorDialog(String message, Throwable cause) {
        // TODO [yl] create an error dialog
    }


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
                boolean onRightTab;
                if (curSelected != null) {
                    CharSequence text = curSelected.getText();
                    onRightTab = (!getResources().getString(R.string.action_bar_apartment)
                            .equals(text));
                } else {
                    onRightTab = false;
                }

                int nextTab = 0;

                switch (Constants.ParseChannelKeys.valueOf(type)) {
                    case PARSE_NEW_CHORES_CHANNEL_KEY:
                    	Settings settings = ApartmentSettingsDAL.getSettings();
                    	boolean showDialog = settings.notifications.newChoresHasBeenDivided;
                    	Log.e("new chore divided val:", ""+showDialog);
                    	if(!showDialog){
                    		return;
                    	}
                        nextTab = ACTION_BAR_TABS_ORDER.MY_CHORES.ordinal();
                        setChoreAlarms(Long.parseLong(jsonData.get("updateTime").toString()));
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
            } catch (UserNotLoggedInException e) {
				return;// no need to log in 
			} catch (FailedToGetApartmentSettings e) {
				return;
			}
        }
    }
    
    // Schedule reminders to new chores created at 'createTime' (in background)
    private void setChoreAlarms(long createTime) {
    
    	// use this to start and trigger a service
    	Intent i= new Intent(this, AlarmService.class);
    	// potentially add data to the intent
    	i.putExtra("createTime", createTime);
    	Log.e("^^^^^^^^^^", "start service was called");
    	this.startService(i); 
	}
}


