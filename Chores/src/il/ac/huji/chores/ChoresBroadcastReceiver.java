package il.ac.huji.chores;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import com.parse.ParseException;
import il.ac.huji.chores.dal.ApartmentDAL;
import il.ac.huji.chores.dal.ApartmentSettingsDAL;
import il.ac.huji.chores.dal.NotificationsDAL;
import il.ac.huji.chores.dal.RoommateDAL;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Broadcast receiver that will handle broadcast both from PushService and internal pull service
* @author Yoav Luft
*/
public class ChoresBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ChoresMainActivity mainActivityContext = (ChoresMainActivity) AppSetup.getMainActivityContext();
        assert mainActivityContext != null;
        ActionBar bar = AppSetup.getActionBar();
        JSONObject jsonData;
        try {
            jsonData = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            boolean onRightTab;
            ActionBar.Tab curSelected = bar.getSelectedTab();
            if (curSelected != null) {
                CharSequence text = curSelected.getText();
                onRightTab = (!mainActivityContext.getResources().getString(R.string.action_bar_apartment)
                        .equals(text));
            } else {
                onRightTab = false;
            }
            int nextTab = 0;
            String type = jsonData.getString("notificationType");
            switch (Constants.ParseChannelKeys.valueOf(type)) {
            case PARSE_NEW_CHORES_CHANNEL_KEY:
                boolean showDialog = handleNewChoresMessage();
                Log.e("new chore divided val:", "" + showDialog);
                if (!showDialog) {
                    return;
                }
                nextTab = ChoresMainActivity.ACTION_BAR_TABS_ORDER.MY_CHORES.ordinal();
                setChoreAlarms(Long.parseLong(jsonData.get("updateTime").toString()),
                        mainActivityContext);
                break;
            case PARSE_INVITATION_CHANNEL_KEY:
                nextTab = ChoresMainActivity.ACTION_BAR_TABS_ORDER.APARTMENT.ordinal();
                break;
            default:
                nextTab = ChoresMainActivity.ACTION_BAR_TABS_ORDER.APARTMENT.ordinal();
            }
            showNotificationDialog(onRightTab, nextTab, type, jsonData);
        } catch (JSONException e1) {
        }catch(UserNotLoggedInException e1){	
        }
    }

    private boolean handleNewChoresMessage() throws UserNotLoggedInException {
        Settings settings = new Settings();
        Context mainActivityContext = AppSetup.getMainActivityContext();
        assert mainActivityContext != null;
        try {
            settings = ApartmentSettingsDAL.getSettings();
        } catch (ParseException e) {
            if (e.getCode() == ParseException.CONNECTION_FAILED) {
                ViewUtils.callToast(mainActivityContext, R.string.chores_connection_failed);
            } else {
                ViewUtils.callToast(mainActivityContext, R.string.general_error);
            }
            try {
                settings = ApartmentSettingsDAL.getDefaultSettings(false);
            } catch (ParseException e1) {
                //will not happen
            }
        }
        return settings.notifications.newChoresHasBeenDivided;
    }

    /**
     * Schedule reminders to new chores created at 'createTime' (in background)
     **/
    private void setChoreAlarms(long createTime, ChoresMainActivity choresMainActivity) {
        // use this to start and trigger a service
        Intent i = new Intent(choresMainActivity, AlarmService.class);
        // potentially add data to the intent
        i.putExtra("createTime", createTime);
        choresMainActivity.startService(i);
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

        final ChoresMainActivity mainActivityContext = (ChoresMainActivity) AppSetup.getMainActivityContext();
        assert mainActivityContext != null;
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivityContext);
        builder.setMessage(dialogMsg);
        String positiveButtonTxt = "OK";
        String negativeButtonTxt = null;
        boolean isNegButton = false;

        switch (Constants.ParseChannelKeys.valueOf(type)) {
            case PARSE_SUGGEST_CHANNEL_KEY:
                positiveButtonTxt = mainActivityContext.getResources().getString(R.string.suggest_positive_response);
                negativeButtonTxt = mainActivityContext.getResources().getString(R.string.suggest_negative_response);
                isNegButton = true;
                break;
            case PARSE_INVITATION_CHANNEL_KEY:
                try {
                    if (RoommateDAL.getApartmentID() == null) {
                        positiveButtonTxt = mainActivityContext.getResources()
                                .getString(R.string.invitation_positive_response);
                    } else {
                        positiveButtonTxt = mainActivityContext.getResources()
                                .getString(R.string.invitation_positive_response_warning);
                    }
                } catch (UserNotLoggedInException e) {
                    positiveButtonTxt = mainActivityContext.getResources()
                            .getString(R.string.invitation_positive_response);
                }
                negativeButtonTxt = mainActivityContext.getResources().getString(R.string.invitation_negative_response);
                isNegButton = true;
                break;
            default:
                Log.d("showNotificationDialog", "type " + type + " is not handled");
        }
        builder.setPositiveButton(positiveButtonTxt, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if (!onRightTab) {
                    ActionBar bar = AppSetup.getActionBar();
                    bar.getTabAt(chosen).select();
                }

                //If another things needs to be done, call function here
                switch (Constants.ParseChannelKeys.valueOf(type)) {
                    case PARSE_SUGGEST_CHANNEL_KEY:
                        try {
                            ApartmentChoresFragment.doSuggestionAccepted(jsonData.get("choreId").toString(),
                                    mainActivityContext.getApplicationContext());
                        } catch (JSONException e) {
                            return;
                        }
                        break;
                    case PARSE_INVITATION_CHANNEL_KEY:
                        acceptApartmentInvitation(jsonData, mainActivityContext);
                        break;
                    default:
                        Log.d("showNotificationDialog", "push of type " + type + " was not handled");
                }
            }
        });
        if (isNegButton) {
            builder.setNegativeButton(negativeButtonTxt, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }

            });
        }
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void acceptApartmentInvitation(JSONObject jsonData, ChoresMainActivity choresMainActivity) {
        Context mainActivityContext = AppSetup.getMainActivityContext();
        assert mainActivityContext != null;
        try {
            String apartmentId = jsonData.getString("apartmentId");
            ApartmentDAL.addRoommateToApartment(apartmentId);
            NotificationsDAL.notifyInvitationAccepted(RoommateDAL.getRoomateUsername(), apartmentId);
        } catch (UserNotLoggedInException e) {
            LoginActivity.OpenLoginScreen(choresMainActivity, false);
        } catch (JSONException e) {
            Log.w("acceptApartmentInvitation", "Error occured when trying to accept invitation", e);
            Log.d("acceptApartmentInvitation", "Offending JSON: " + jsonData.toString());
            choresMainActivity.receiver.showErrorDialog(mainActivityContext.getResources().getString(R.string.failed_to_join_apartment), e);
        } catch (ParseException e) {
            if (e.getCode() == ParseException.CONNECTION_FAILED) {
                ViewUtils.callToast(mainActivityContext, R.string.chores_connection_failed);
            } else {
                ViewUtils.callToast(mainActivityContext, R.string.general_error);
            }
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
}
