package il.ac.huji.chores;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.parse.ParseException;
import il.ac.huji.chores.dal.ChoreDAL;
import il.ac.huji.chores.exceptions.FailedToAddChoreInfoException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import java.util.List;

public class AddEditChoresFragment extends Fragment {

    private final int CREATE_NEW_CHORE = 2222;
    private final int UPDATE_CHORE_INFO = 1111;
    private ArrayAdapter<ChoreInfo> _adapter;
    private ChoreInfo _editedChore = null;
    private List<ChoreInfo> choreInfos;
    private ListView listChores;
    private View progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_edit_chores,
                container, false);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setContentView(R.layout.fragment_add_edit_chores);
        progressBar = getActivity().findViewById(R.id.progressBar);
        listChores = (ListView) getActivity().findViewById(
                R.id.AddEditChoresFragment_choresList);

        // set item click listener on list items
        listChores.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // if list item is clicked - open edit chore
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                _editedChore = (ChoreInfo) parent
                        .getItemAtPosition(position);
                CallNewChoreDialog(_editedChore, UPDATE_CHORE_INFO);
            }
        });

        // add new chore button
        Button addChore = (Button) getActivity().findViewById(R.id.AddEditChoresFragment_Addchore_button);
        addChore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CallNewChoreDialog(null, CREATE_NEW_CHORE);
            }

        });

        ViewUtils.hideLoadingView(listChores, getActivity(), R.id.progressBar);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);    //To change body of overridden methods use File | Settings | File Templates.
                // set adapter
                Activity activity = getActivity();
                if (activity == null) {
                    return;
                }
                ViewUtils.replacePlaceholder(listChores, progressBar);
                _adapter = new AddEditChoresDisplayAdapter(activity, choreInfos);
                listChores.setAdapter(_adapter);
                _adapter.notifyDataSetChanged();
                if (_adapter.isEmpty()) {
                    // open add new chore automatically
                    CallNewChoreDialog(null, CREATE_NEW_CHORE);
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    choreInfos = ChoreDAL.getAllChoreInfo();
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    Log.e("Exception", e.getMessage());
                } catch (UserNotLoggedInException e) {
//                    LoginActivity.OpenLoginScreen(getActivity(), false);
                }
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }.execute();
    }

    /*
     * Call new chore dialog with null or a ChoreInfo instance
     */
    private void CallNewChoreDialog(ChoreInfo choreInfo, int requestCode) {
        Intent intent = new Intent(getActivity(), NewChoreDialogActivity.class);
        intent.putExtra(getResources()
                .getString(R.string.new_chore_extra1_name), choreInfo);
        startActivityForResult(intent, requestCode);
    }

    /*
     * Gets results (new ChoreInfo) from new chore dialog //TODO check if it's
     * should be here or in activity
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null) {// back button
            return;
        }

        ViewUtils.hideLoadingView(listChores, getActivity(), R.id.progressBar);
        final ChoreInfo newChore = (ChoreInfo) data
                .getSerializableExtra(getResources().getString(
                        R.string.new_chore_extra1_name));

        if (requestCode == UPDATE_CHORE_INFO && resultCode == Activity.RESULT_OK) {
            new AsyncTask<Void, Void, ParseException>() {

                @Override
                protected void onPostExecute(ParseException e) {
                    super.onPostExecute(e);    //To change body of overridden methods use File | Settings | File Templates.
                    Activity activity = getActivity();
                    if (activity == null) {
                        return;
                    }
                    ViewUtils.replacePlaceholder(listChores, progressBar);
                    if (e == null) {
                        return;
                    }
                    if (e.getCode() == ParseException.CONNECTION_FAILED) {
                        Toast.makeText(activity, activity.getResources().getString(R.string.chores_connection_failed),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity, activity.getResources().getString(R.string.general_error),
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                protected ParseException doInBackground(Void... params) {
                    try {
                        ChoreDAL.updateChoreInfo(newChore, newChore.getChoreInfoID());
                    } catch (ParseException e) {
                        return e;
                    }
                    return null;
                }
            }.execute();
            //TODO:UPDATE SPECIFIC FIELDS
        } else if (requestCode == CREATE_NEW_CHORE && resultCode == Activity.RESULT_OK) {
            new AsyncTask<Void, Void, Exception>() {

                @Override
                protected void onPostExecute(Exception e) {
                    super.onPostExecute(e);    //To change body of overridden methods use File | Settings | File Templates.
                    Activity activity = getActivity();
                    if (activity == null) {
                        return;
                    }
                    ViewUtils.replacePlaceholder(listChores, progressBar);
                    if (e == null) {
                        return;
                    } else if (e instanceof UserNotLoggedInException) {
                        LoginActivity.OpenLoginScreen(getActivity(), false);
                    } else {
                        Log.e("AddEditChoresFragment.onActivityResult", "adding chore info failed.", e);
                    }
                }

                @Override
                protected Exception doInBackground(Void... params) {
                    try {
                        ChoreDAL.addChoreInfo(newChore);
                    } catch (UserNotLoggedInException e) {
                        return e;
                    } catch (ParseException e) {
						
					}
                    return null;
                }
            }.execute();
        }
        if (resultCode == Activity.RESULT_OK) {
            if (_editedChore != null) { // add chore
                _adapter.remove(_editedChore);
                _adapter.notifyDataSetChanged();
            }
            _adapter.add(newChore);
        }

    }

}
