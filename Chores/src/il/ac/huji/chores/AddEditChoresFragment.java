package il.ac.huji.chores;

import il.ac.huji.chores.dal.ChoreDAL;
import il.ac.huji.chores.exceptions.FailedToAddChoreInfoException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import java.util.List;

import com.parse.ParseException;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class AddEditChoresFragment extends Fragment {

	private ArrayAdapter<ChoreInfo> _adapter;
	private ChoreInfo _editedChore = null;

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

		ListView listChores = (ListView) getActivity().findViewById(
				R.id.AddEditChoresFragment_choresList);

		// set item click listener on list items
		listChores
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					// if list item is clicked - open edit chore
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						_editedChore = (ChoreInfo) parent
								.getItemAtPosition(position);
						CallNewChoreDialog(_editedChore, 1111);
					}
				});

		// add new chore button
		Button addChore = (Button) getActivity().findViewById(
				R.id.AddEditChoresFragment_Addchore_button);
		addChore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				CallNewChoreDialog(null, 2222);
			}

		});

		List<ChoreInfo> chores;
		try {
			chores = ChoreDAL.getAllChoreInfo();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.e("Exception", e.getMessage());
			return;
		} catch (UserNotLoggedInException e) {
			LoginActivity.OpenLoginScreen(getActivity(), false);
			return;
		}

		// set adapter
		_adapter = new ChoreInfosDisplayAdapter(getActivity(), chores);
		listChores.setAdapter(_adapter);
		_adapter.notifyDataSetChanged();

		if (_adapter.isEmpty()) {
			// open add new chore automatically
			CallNewChoreDialog(null, 2222);
		}
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

		ChoreInfo newChore = (ChoreInfo) data
				.getSerializableExtra(getResources().getString(
						R.string.new_chore_extra1_name));

		if (requestCode == 1111 && resultCode == Activity.RESULT_OK) {
			try {

				ChoreDAL.addChoreInfo(newChore);
			} catch (FailedToAddChoreInfoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UserNotLoggedInException e) {
				LoginActivity.OpenLoginScreen(getActivity(), false);
			}
			//TODO:UPDATE SPECIFIC FIELDS
		} else if (requestCode == 2222 && resultCode == Activity.RESULT_OK) {
			try {
				ChoreDAL.addChoreInfo(newChore);
			} catch (UserNotLoggedInException e) {
				LoginActivity.OpenLoginScreen(getActivity(), false);
			} catch (FailedToAddChoreInfoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
