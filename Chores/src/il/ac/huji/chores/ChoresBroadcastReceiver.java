package il.ac.huji.chores;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.parse.ParseException;
import il.ac.huji.chores.dal.ApartmentDAL;
import il.ac.huji.chores.dal.ApartmentSettingsDAL;
import il.ac.huji.chores.dal.NotificationsDAL;
import il.ac.huji.chores.dal.RoommateDAL;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;
import org.json.JSONException;
import org.json.JSONObject;

/**
* @author Yoav Luft
*/
public class ChoresBroadcastReceiver extends BroadcastReceiver {

    private final Context context;

    public ChoresBroadcastReceiver() {
        this.context = AppSetup.getContext();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ActionBar bar = AppSetup.getInstance(context).getActionBar();
        ActionBar.Tab curSelected = bar.getSelectedTab();
        JSONObject jsonData;
        try {
            jsonData = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            String type = jsonData.getString("notificationType");
            boolean onRightTab;
            if (curSelected != null) {
                CharSequence text = curSelected.getText();
                onRightTab = (!context.getResources().getString(R.string.action_bar_apartment).equals(text));
            } else {
                onRightTab = false;
            }

            int nextTab = 0;

            switch (Constants.ParseChannelKeys.valueOf(type)) {
            case PARSE_NEW_CHORES_CHANNEL_KEY:
                Settings settings=new Settings();
                try {
                    settings = ApartmentSettingsDAL.getSettings();
                } catch (ParseException e) {

                    if (e.getCode() == ParseException.CONNECTION_FAILED) {
                        Toast.makeText(
                                context,
                                context.getResources().getString(
                                        R.string.chores_connection_failed),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(
                                context,
                                context.getResources().getString(
                                        R.string.general_error),
                                Toast.LENGTH_LONG).show();
                    }
                    try {
                        settings = ApartmentSettingsDAL.getDefaultSettings(false);
                    } catch (ParseException e1) {
                        //will not happen
                    }
                }
                boolean showDialog = settings.notifications.newChoresHasBeenDivided;
                Log.e("new chore divided val:", "" + showDialog);
                if (!showDialog) {
                    return;
                }
                nextTab = ChoresMainActivity.ACTION_BAR_TABS_ORDER.MY_CHORES.ordinal();
                setChoreAlarms(Long.parseLong(jsonData.get("updateTime")
                        .toString()));
                break;
            case PARSE_INVITATION_CHANNEL_KEY:
                nextTab = ChoresMainActivity.ACTION_BAR_TABS_ORDER.APARTMENT.ordinal();
                break;
            default:
                nextTab = ChoresMainActivity.ACTION_BAR_TABS_ORDER.APARTMENT.ordinal();
            }
            showNotificationDialog(onRightTab, nextTab, type, jsonData);

        } catch (JSONException e) {
            return;
        } catch (UserNotLoggedInException e) {
            return;// no need to log in
        }
    }

    /**
     * Schedule reminders to new chores created at 'createTime' (in background)
     */
    private void setChoreAlarms(long createTime) {

        // use this to start and trigger a service
        Intent i = new Intent(context, AlarmService.class);
        // potentially add data to the intent
        i.putExtra("createTime", createTime);
        context.startService(i);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(dialogMsg);
        String positiveButtonTxt = "OK";
        String negativeButtonTxt = null;
        boolean isNegButton = false;

        switch (Constants.ParseChannelKeys.valueOf(type)) {
            case PARSE_SUGGEST_CHANNEL_KEY:
                positiveButtonTxt = context.getResources().getString(R.string.suggest_positive_response);
                negativeButtonTxt = context.getResources().getString(R.string.suggest_negative_response);
                isNegButton = true;
                break;
            case PARSE_INVITATION_CHANNEL_KEY:
                try {
                    if (RoommateDAL.getApartmentID() == null) {
                        positiveButtonTxt = context.getResources().getString(R.string.invitation_positive_response);
                    } else {
                        positiveButtonTxt = context.getResources().getString(R.string.invitation_positive_response_warning);
                    }
                } catch (UserNotLoggedInException e) {
                    positiveButtonTxt = context.getResources().getString(R.string.invitation_positive_response);
                }
                negativeButtonTxt = context.getResources().getString(R.string.invitation_negative_response);
                isNegButton = true;
                break;
            default:
                Log.d("showNotificationDialog", "type " + type + " is not handled");
        }
        builder.setPositiveButton(positiveButtonTxt, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if (!onRightTab) {
                    ActionBar bar = AppSetup.getInstance(context).getActionBar();
                    bar.getTabAt(chosen).select();
                }

                //If another things needs to be done, call function here
                switch (Constants.ParseChannelKeys.valueOf(type)) {
                    case PARSE_SUGGEST_CHANNEL_KEY:
                        try {
                            ApartmentChoresFragment.doSuggestionAccepted(jsonData.get("choreId").toString(),
                                    context.getApplicationContext());
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
            builder.setNegativeButton(negativeButtonTxt, new DialogInterface.OnClickListener() {

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
            LoginActivity.OpenLoginScreen(context, false);
        } catch (JSONException e) {
            Log.w("acceptApartmentInvitation", "Error occured when trying to accept invitation", e);
            Log.d("acceptApartmentInvitation", "Offending JSON: " + jsonData.toString());
            showErrorDialog(context.getResources().getString(R.string.failed_to_join_apartment), e);
        } catch (ParseException e) {
            if (e.getCode() == ParseException.CONNECTION_FAILED) {
                Toast.makeText(
                        context,
                        context.getResources().getString(
                                R.string.chores_connection_failed),
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context,
                        context.getResources().getString(R.string.general_error),
                        Toast.LENGTH_LONG).show();
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
