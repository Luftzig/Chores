package il.ac.huji.chores;

import com.parse.ParseException;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import il.ac.huji.chores.dal.ChoreDAL;
import il.ac.huji.chores.dal.RoommateDAL;
import il.ac.huji.chores.dummy.DummyContent;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the
 * ListView with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class MyChoresFragment extends Fragment {

	public static MyChoresFragment newInstance(String param1,
			String param2) {
		MyChoresFragment fragment = new MyChoresFragment();
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MyChoresFragment() {
        // Noop
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        Log.d("MyChoresFragment", "onCreateView");
		View view = inflater.inflate(R.layout.fragment_my_chores_list, container, false);

		// Set the adapter
		ListView listView = (ListView) view.findViewById(R.id.myChoresFragmentListView);
        ListAdapter adapter;
		try {
			adapter = new MyChoresListAdapter(getActivity(), ChoreDAL.getRoommatesChores());
		} catch (UserNotLoggedInException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((AdapterView<ListAdapter>) listView).setAdapter(adapter);

        Log.d("MyChoresFragment", "view created");
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

}
