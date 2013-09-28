package il.ac.huji.chores;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseException;
import il.ac.huji.chores.dal.ApartmentDAL;
import il.ac.huji.chores.dal.ChoreStatisticsDAL;
import il.ac.huji.chores.dal.RoommateDAL;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static il.ac.huji.chores.StatisticsListAdapter.StatisticsRowWrapper;

public class StatisticsFragment extends Fragment {

    private ListView listView;
    private StatisticsListAdapter adapter;
    private List<StatisticsRowWrapper> statistics;
    private ProgressBar progressBar;
    private TextView messageBox;

    
    @Override 
    public void onResume(){
    	super.onResume();

        messageBox = (TextView) getActivity().findViewById(
				R.id.statisticsNoStatisticsTextView);
        
    	new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    String apartmentId = RoommateDAL.getApartmentID();
                    List<String> names = ChoreStatisticsDAL.getChoreStatisticsNames();
                    for (String name : names) {
                        ChoreApartmentStatistics choreApartmentStatistic = ChoreStatisticsDAL.getChoreApartmentStatistic(name, apartmentId);
                        StatisticsRowWrapper wrapper = new StatisticsRowWrapper(choreApartmentStatistic, StatisticsListAdapter.Display.COMPACT);
                        statistics.add(wrapper);
                        publishProgress();
                    }
                } catch (UserNotLoggedInException e) {
                    return null;
                } catch (ParseException e) {
                    return null;
                }
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
                statistics = new ArrayList<StatisticsRowWrapper>();
                adapter = new StatisticsListAdapter(getActivity(), statistics);
            	messageBox.setVisibility(View.GONE);
                listView.setAdapter(adapter);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);    //To change body of overridden methods use File | Settings | File Templates.
                progressBar.setVisibility(View.GONE);
                if(adapter==null ||adapter.getCount()==0){
                	adapter.notifyDataSetChanged();
                    messageBox
    				.setText(R.string.statistics_no_statistics);
    				messageBox.setVisibility(View.VISIBLE);
    				ViewUtils.hideLoadingView(listView, getActivity(), messageBox);
                } else {
					ViewUtils.hideLoadingView(messageBox, getActivity(), listView);
				
                }
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);    //To change body of overridden methods use File | Settings | File Templates.
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        listView = (ListView) view.findViewById(R.id.statisticsListView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.d("StatisticsFragment", "onClick called on item which is " + adapter.getItem(position).displayType);
                StatisticsRowWrapper row = adapter.getItem(position);
                if (row.displayType == StatisticsListAdapter.Display.EXPANDED) {
                    row.displayType = StatisticsListAdapter.Display.COMPACT;
                } else {
                    row.displayType = StatisticsListAdapter.Display.EXPANDED;
                }
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
