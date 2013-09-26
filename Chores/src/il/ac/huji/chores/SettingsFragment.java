package il.ac.huji.chores;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.PushService;
import il.ac.huji.chores.dal.ApartmentSettingsDAL;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import static il.ac.huji.chores.Constants.ParseChannelKeys.*;

public class SettingsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings, container, false);
		return view;
	}

    @Override
    public void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        ParseUser user = ParseUser.getCurrentUser();
        Button logoutButton = (Button) getActivity().findViewById(R.id.logout_button);
        if (user != null) {
            String loggedInAs = new StringBuilder().append(getResources().getString(R.string.logged_in_prompt))
                    .append(" ").append(user.getUsername()).toString();
            ((TextView) getActivity().findViewById(R.id.settingsLoggedInAs)).setText(loggedInAs);
            logoutButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseUser.logOut();
                    LoginActivity.OpenLoginScreen(getActivity(), true);
                }
            });
        } else {
            logoutButton.setText(getResources().getString(R.string.please_log_in));
            logoutButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginActivity.OpenLoginScreen(getActivity(), true);
                }
            });
        }
        try {
            /** Read current settings and display them in the UI **/
            Settings settings = null;
            try {
                settings = ApartmentSettingsDAL.getSettings();
            } catch (ParseException e) {
                if (e.getCode() == ParseException.CONNECTION_FAILED) {
                    Toast.makeText(
                            getActivity(),
                            getActivity().getResources().getString(
                                    R.string.chores_connection_failed),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(
                            getActivity(),
                            getActivity().getResources().getString(
                                    R.string.general_error),
                            Toast.LENGTH_LONG).show();
                }
                settings = ApartmentSettingsDAL.getDefaultSettings(false);
            }
            final Settings defaultSettings = settings;
            // Notifications
            final CheckBox roommateMissed_checkBox = (CheckBox) (getActivity()
                    .findViewById(R.id.NotificationsSettingRoomateMissedChoreCheckbox));
            boolean check = defaultSettings.notifications.roommateMissedChore;
            roommateMissed_checkBox.setChecked(check);

            final CheckBox roommateDone_checkBox = (CheckBox) (getActivity()
                    .findViewById(R.id.NotificationsSettingsRoomateFinishedChoreCheckbox));
            check = defaultSettings.notifications.roommateFinishedChore;
            roommateDone_checkBox.setChecked(check);

            final CheckBox roommateStole_checkBox = (CheckBox) (getActivity()
                    .findViewById(R.id.NotificationsSettingsRoomateStoleChoreCheckbox));
            check = defaultSettings.notifications.roommateStoleMyChore;
            roommateStole_checkBox.setChecked(check);

            final CheckBox newChoresDivided_checkBox = (CheckBox) (getActivity()
                    .findViewById(R.id.NotificationsSettingsNewChoresCheckbox));
            check = defaultSettings.notifications.roommateStoleMyChore;
            newChoresDivided_checkBox.setChecked(check);

            // Chores
            final CheckBox choreReminder_checkBox = (CheckBox) (getActivity()
                    .findViewById(R.id.ChoresSettingsDisableRemindersCheckbox));
            check = defaultSettings.chores.disableRemindersAboutUpcomingChores;
            choreReminder_checkBox.setChecked(check);

            final CheckBox forbidStealing_checkBox = (CheckBox) (getActivity()
                    .findViewById(R.id.ChoresSettingsForbidCheckbox));
            check = defaultSettings.chores.forbidRoommatesFromTakingMyChores;
            forbidStealing_checkBox.setChecked(check);

            // Reminders

            final EditText reminderTime_editText = (EditText) (getActivity()
                    .findViewById(R.id.RemindersHoursText));
            reminderTime_editText.setText(Integer
                    .toString(defaultSettings.reminders.hours));

            /** Buttons handling and sending settings to database **/

            final Button saveButton = (Button) (getActivity()
                    .findViewById(R.id.SettingsFragment_saveButton));
            saveButton.setOnClickListener(new OnClickListener() {

                final Context context = getActivity();

                @Override
                public void onClick(View v) {
                    // Get new settings and save in database
                    Settings newSettings = new Settings();

                    // Notifications
                    newSettings.notifications = newSettings.new Notifications();
                    newSettings.notifications.roommateMissedChore = roommateMissed_checkBox
                            .isChecked();
                    newSettings.notifications.newChoresHasBeenDivided = newChoresDivided_checkBox
                            .isChecked();
                    newSettings.notifications.roommateFinishedChore = roommateDone_checkBox
                            .isChecked();
                    newSettings.notifications.roommateStoleMyChore = roommateStole_checkBox
                            .isChecked();

                    // Chores
                    newSettings.chores = newSettings.new Chores();
                    newSettings.chores.disableRemindersAboutUpcomingChores = choreReminder_checkBox
                            .isChecked();
                    newSettings.chores.forbidRoommatesFromTakingMyChores = forbidStealing_checkBox
                            .isChecked();

                    // Reminders
                    newSettings.reminders = newSettings.new Reminders();
                    newSettings.reminders.hours = Integer
                            .parseInt(reminderTime_editText.getText()
                                    .toString());

                    doSettings(newSettings, defaultSettings);

                    // Send to database
                    try {
                        ApartmentSettingsDAL.updateSettings(newSettings, false);
                        Toast.makeText(
                                context,
                                context.getResources().getString(
                                        R.string.setting_saved_toast_text),
                                Toast.LENGTH_SHORT).show();
                    } catch (UserNotLoggedInException e) {
                        LoginActivity.OpenLoginScreen(context, false);
                        return;
                    } catch (ParseException e) {
                        if (e.getCode() == ParseException.CONNECTION_FAILED) {
                            Toast.makeText(
                                    getActivity(),
                                    getActivity().getResources().getString(
                                            R.string.chores_connection_failed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                private void doSettings(Settings newSettings,
                                        Settings oldSettings) {
                    // unsubscribe / subscribe only if settings were change.

					/* Notifications */
                    // roommate missed a chore
                    if (newSettings.notifications.roommateMissedChore
                            ^ oldSettings.notifications.roommateMissedChore) {
                        if (newSettings.notifications.roommateMissedChore) {
                            PushService.subscribe(context,
                                    PARSE_MISSED_CHANNEL_KEY.toString(),
                                    ChoresMainActivity.class);
                        } else {
                            PushService.unsubscribe(context,
                                    PARSE_MISSED_CHANNEL_KEY.toString());
                        }
                    }
                    // roommate is done
                    if (newSettings.notifications.roommateFinishedChore
                            ^ oldSettings.notifications.roommateFinishedChore) {
                        if (newSettings.notifications.roommateFinishedChore) {
                            PushService.subscribe(context,
                                    PARSE_DONE_CHANNEL_KEY.toString(),
                                    ChoresMainActivity.class);
                        } else {
                            PushService.unsubscribe(context,
                                    PARSE_DONE_CHANNEL_KEY.toString());
                        }
                    }
                    // roommate stole a chore
                    if (newSettings.notifications.roommateStoleMyChore
                            ^ oldSettings.notifications.roommateStoleMyChore) {
                        if (newSettings.notifications.roommateStoleMyChore) {
                            PushService.subscribe(context,
                                    PARSE_STEAL_CHANNEL_KEY.toString(),
                                    ChoresMainActivity.class);
                        } else {
                            PushService.unsubscribe(context,
                                    PARSE_STEAL_CHANNEL_KEY.toString());
                        }
                    }

                    // new chores divided
                    if (newSettings.notifications.newChoresHasBeenDivided
                            ^ oldSettings.notifications.newChoresHasBeenDivided) {
                        if (newSettings.notifications.newChoresHasBeenDivided) {
                            PushService.subscribe(context,
                                    PARSE_NEW_CHORES_CHANNEL_KEY.toString(),
                                    ChoresMainActivity.class);
                        } else {
                            PushService.unsubscribe(context,
                                    PARSE_NEW_CHORES_CHANNEL_KEY.toString());
                        }
                    }
                    // chores
                    // TODO chores related stuff

                    // reminders
                    // TODO reminders related stuff

                }
            });

        } catch (UserNotLoggedInException e) {
            LoginActivity.OpenLoginScreen(getActivity(), false);
            return;
        } catch (ParseException e1) {
            if (e1.getCode() == ParseException.CONNECTION_FAILED) {
                Toast.makeText(
                        getActivity(),
                        getActivity().getResources().getString(
                                R.string.chores_connection_failed),
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(
                        getActivity(),
                        getActivity().getResources().getString(
                                R.string.general_error),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
}
