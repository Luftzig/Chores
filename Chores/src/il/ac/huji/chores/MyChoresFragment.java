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
import il.ac.huji.chores.dal.ApartmentDAL;
import il.ac.huji.chores.dal.ChoreDAL;
import il.ac.huji.chores.dal.RoommateDAL;
import il.ac.huji.chores.exceptions.FailedToGetRoommateException;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MyChoresFragment contains a list of all chores assigned to the current user.
 */
public class MyChoresFragment extends Fragment {

    private MyChoresListAdapter adapter;
    private List<Chore> chores;
    private XYSeries currentSeries;
    private XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
    private XYSeriesRenderer chartRenderer;
    private XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
    private GraphicalView chart;

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


    @Override
    public void onResume() {
        Log.d("MyChoresFragment", "onResume");
        super.onResume();
        if (adapter != null) {
            try {
                adapter.clear();
                adapter.addAll(ChoreDAL.getRoommatesChores());
                Log.d("MyChoresFragment", "chores list size == " + adapter.getCount());
                adapter.notifyDataSetChanged();
            } catch (UserNotLoggedInException e) {
                LoginActivity.OpenLoginScreen(getActivity(), false);
            }
        }

        // Coins Chart stuff
        try {
            if (chart == null) {
                initChart();
                    createDataSeries(getCoinsMap());
                chart = ChartFactory.getBarChartView(getActivity(), dataSet, renderer, BarChart.Type.DEFAULT);
                ((ViewGroup) getActivity().findViewById(R.id.myChoresChartContainer)).addView(chart);
            }
        } catch (UserNotLoggedInException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FailedToGetRoommateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private Map<String, Integer> getCoinsMap() throws UserNotLoggedInException, FailedToGetRoommateException {
        List<String> roommates = ApartmentDAL.getApartmentRoommates(RoommateDAL.getApartmentID());
        if (roommates == null || roommates.size() == 0) {
            throw new FailedToGetRoommateException("No roommates found for apartment " + RoommateDAL.getApartmentID());
        }
        Map<String, Integer> coinsMap = new HashMap<String, Integer>(roommates.size());
        for (String username : roommates) {
            Roommate roommate = RoommateDAL.getRoommateByName(username);
            coinsMap.put(roommate.getUsername(), roommate.getCoinsCollected());
        }
        return coinsMap;
    }

    private void createDataSeries(Map<String, Integer> coins) {
        int i = 1;
        List<String> orderedKeys = new ArrayList<String>(coins.size());
        String currentRoommate = RoommateDAL.getRoomateUsername();
        for (String key : coins.keySet()) {
            if (key.equals(currentRoommate)) {
                currentSeries.add(1, coins.get(key));
                orderedKeys.add(0, key);
            } else {
                currentSeries.add(i, coins.get(key));
            }
        }
    }

    private void initChart() {
        currentSeries = new XYSeries("Sample Data");
        dataSet.addSeries(currentSeries);
        chartRenderer = new XYSeriesRenderer();
        renderer.addSeriesRenderer(chartRenderer);
        renderer.setBarSpacing(0.5);
        renderer.setDisplayValues(true);
        renderer.setXAxisMin(0.0);
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
