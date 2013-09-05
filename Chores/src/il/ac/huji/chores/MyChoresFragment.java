package il.ac.huji.chores;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import il.ac.huji.chores.dal.ChoreDAL;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import java.util.List;

/**
 * MyChoresFragment contains a list of all chores assigned to the current user.
 */
public class MyChoresFragment extends Fragment {

    private MyChoresListAdapter adapter;
    private List<Chore> chores;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_chores_list, container, false);

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

    //this will be called if there's a new ASSIGNED actions - new chores were assigned
    protected void onNewIntent(Intent intent) {
    
    }

    @Override
    public void onResume() {
        Log.d("MyChoresFragment", "onResume");
        super.onResume();
        if (adapter != null) {
            try {
                chores = ChoreDAL.getRoommatesChores();
                Log.d("MyChoresFragment", "chores list size == " + chores.size());
                adapter.notifyDataSetChanged();
            } catch (UserNotLoggedInException e) {
                // Ignore
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("MyChoresFragment", "MyChores: onActivityCreated");
        // Set the adapter
        ListView listView = (ListView) getActivity().findViewById(R.id.myChoresFragmentListView);
        try {
            chores = ChoreDAL.getRoommatesChores();
            adapter = new MyChoresListAdapter(getActivity(), chores);
        } catch (UserNotLoggedInException e) {
            e.printStackTrace();
        }
        listView.setAdapter(adapter);
    }
}
