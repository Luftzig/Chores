package il.ac.huji.chores;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.parse.ParseUser;
import il.ac.huji.chores.dal.ChoreDAL;
import il.ac.huji.chores.exceptions.DataNotFoundException;
import il.ac.huji.chores.exceptions.FailedToRetrieveOldChoresException;
import il.ac.huji.chores.exceptions.FailedToRetriveAllChoresException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import java.util.Collections;
import java.util.List;

public class ApartmentChoresFragment extends Fragment {

    private ArrayAdapter<Chore> adapter;
    private String _oldestChoreDisplayed = null;
    private static final int HISTORY_FUNC_AMMOUNT = 5; // when the history button is pressed HISTORY_FUNC_AMMOUNT more chores are displayed
    private String _userName = null;
    private List<Chore> chores = null;
    private View progressBar;
    private ListView listChores;
    private List<Chore> histChores;
    private TextView titleText;
    private String apartmentID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_apartment, container, false);
        return view;
    }

    public void onResume() {
        super.onResume();
        ViewUtils.hideLoadingView(listChores, getActivity(), R.id.progressBar);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    chores = ChoreDAL.getAllChores();
                }  catch( UserNotLoggedInException e1 )  {
                    LoginActivity.OpenLoginScreen(getActivity(), false);
                } catch (FailedToRetriveAllChoresException e) {
                    showErrorMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);    //To change body of overridden methods use File | Settings | File Templates.

                Activity activity = getActivity();
                if (activity == null) {
                    Log.e("ApartmentChoresFragment$AsyncTask.onPostExecute", "activity was null when loading finished");
                    return;
                }
                if (chores.size() > 0) {
                    adapter.clear();
                    adapter.addAll(chores);
                    titleText.setText(getResources().getString(R.string.apartment_tableTitle));
                } else if (apartmentID == null) {
                    titleText.setText(getResources().getString(R.string.apartment_chores_no_apartment));
                } else {
                    titleText.setText(getResources().getString(R.string.apartment_chores_no_chores));
                }
                ViewUtils.replacePlaceholder(listChores, progressBar);
            }
        }.execute();
    }

    public String getCurUsername() {
        if (_userName != null) {
            return _userName;
        }
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            //user isn't logged in
            LoginActivity.OpenLoginScreen(getActivity(), false);
            user = ParseUser.getCurrentUser();
        }

        return user.getUsername();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void showErrorMessage() {
        //To change body of created methods use File | Settings | File Templates.
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = getActivity().findViewById(R.id.progressBar);
        listChores = (ListView) getActivity().findViewById(R.id.apartmentListChores);
        titleText = (TextView) getActivity().findViewById(R.id.myChoresRowTitle);
        apartmentID = (String) ParseUser.getCurrentUser().get("apartmentID");

        ViewUtils.hideLoadingView(listChores, getActivity(), R.id.progressBar);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    chores = ChoreDAL.getAllChores();
                }  catch( UserNotLoggedInException e1 )  {
                    LoginActivity.OpenLoginScreen(getActivity(), false);
                } catch (FailedToRetriveAllChoresException e) {
                    showErrorMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);    //To change body of overridden methods use File | Settings | File Templates.

                Activity activity = getActivity();
                if (activity == null) {
                    Log.e("ApartmentChoresFragment$AsyncTask.onPostExecute", "activity was null when loading finished");
                    return;
                }
                if (chores.size() > 0) {
                    adapter = new ApartmentChoresDisplayAdapter(activity, chores);
                    listChores.setAdapter(adapter);
                    titleText.setText(getResources().getString(R.string.apartment_tableTitle));
                } else if (apartmentID == null) {
                    titleText.setText(getResources().getString(R.string.apartment_chores_no_apartment));
                } else {
                    titleText.setText(getResources().getString(R.string.apartment_chores_no_chores));
                }
                ViewUtils.replacePlaceholder(listChores, progressBar);
            }
        }.execute();
        if (chores != null) {
            _oldestChoreDisplayed = checkOldestChoreInList(chores);
        }
        //set item click listener
        listChores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // if list chore item is clicked - open chore card
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Chore chore = (Chore) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), ChoreCardActivity.class);
                intent.putExtra(getResources().getString(R.string.card_activity_extra1_name), chore);
                _userName = getCurUsername();
                intent.putExtra(getResources().getString(R.string.card_activity_extra2_name), _userName);
                startActivity(intent);
            }
        });

        /* Buttons listeners */
        // add/edit chores button
        Button editChores = (Button) getActivity().findViewById(R.id.ApartmentChoresFragment_editChores_button);
        editChores.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddEditChoresActivity.class);
                startActivity(intent);
            }
        });

        // apartment settings button
        Button apartmentSettings = (Button) getActivity().findViewById(R.id.ApartmentChoresFragment_apartmentSettings_button);
        apartmentSettings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (apartmentID == null) {
                    intent = new Intent(getActivity(), NewApartmentDialogActivity.class);
                } else {
                    intent = new Intent(getActivity(), ApartmentSettingsActivity.class);
                }
                startActivity(intent);
            }
        });

        // apartment settings button
        final Button history = (Button) getActivity().findViewById(R.id.ApartmentChoresFragment_historyButton);
        history.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.hideLoadingView(listChores, getActivity(), R.id.progressBar);
                //chores history button
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);    //To change body of overridden methods use File | Settings | File Templates.
                        if (histChores != null) {
                            adapter.addAll(histChores);
                        } else {
                            showErrorMessage();
                        }
                        ViewUtils.replacePlaceholder(listChores, progressBar);
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            histChores = ChoreDAL.getUserOldChores(_oldestChoreDisplayed, HISTORY_FUNC_AMMOUNT);
                        } catch (UserNotLoggedInException e) {
                            // Ignore
                        } catch (FailedToRetrieveOldChoresException e) {
                            Log.w("ApartmentChoresFragment$AsyncTask.doInBackground", "Failed to retrieve");
                            Log.d("ApartmentChoresFragment$AsyncTask.doInBackground", "Failed to retrieve", e);
                            showErrorMessage();
                        }
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }
                }.execute();
            }
        });
    }

    //check what chore is the oldest (deadline-wise) from a given list.
    private String checkOldestChoreInList(List<Chore> histChores) {
        if (histChores.size() == 0) {
            return null;
        }

        Collections.sort(histChores, new DeadlineComparator());
        return histChores.get(0).getId();
    }

    /**
     * Call from a suggest notification
     * @param suggestedChoreId
     * @param context
     */
    public static void doSuggestionAccepted(String suggestedChoreId, Context context) {
        try {
            ChoreDAL.updateAssignedTo(suggestedChoreId, ParseUser.getCurrentUser().getUsername());
            //TODO(shani): add call to suggestion accepted.
        } catch (UserNotLoggedInException e) {
            LoginActivity.OpenLoginScreen(context, false);
            return;
        } catch (DataNotFoundException e) {
            Log.e("Exception", e.getMessage());
            return;
        }
    }
}

