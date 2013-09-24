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
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.*;

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

//        // Coins Chart stuff
//        try {
//            initChart();
//            chart = ChartFactory.getBarChartView(getActivity(), dataSet, renderer, BarChart.Type.DEFAULT);
//            ((ViewGroup) getActivity().findViewById(R.id.myChoresChartContainer)).addView(chart);
//        } catch (UserNotLoggedInException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (FailedToGetRoommateException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
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

    private List<String> createDataSeries(Map<String, Integer> coins) {
        int i = 2;
        List<String> orderedKeys = new ArrayList<String>(coins.size());
        String currentRoommate = RoommateDAL.getRoomateUsername();
        for (String key : coins.keySet()) {
            if (key.equals(currentRoommate)) {
                currentSeries.add(1, coins.get(key));
                orderedKeys.add(0, key);
            } else {
                currentSeries.add(i, coins.get(key));
                i++;
                orderedKeys.add(key);
            }
        }
        return orderedKeys;
    }

    private void initChart() throws UserNotLoggedInException, FailedToGetRoommateException {
        currentSeries = new XYSeries("Coins");
        Map<String, Integer> coinsMap = getCoinsMap();
        List<String> orderedNames = createDataSeries(coinsMap);
        dataSet.addSeries(currentSeries);
        chartRenderer = new XYSeriesRenderer();
        renderer.addSeriesRenderer(chartRenderer);
        renderer.setBarSpacing(0.2);
        renderer.setXAxisMin(0.0);
        renderer.setXAxisMax(orderedNames.size() + 1);
        int minCoins = findCoinsMin(coinsMap);
        int maxCoins = findCoinsMax(coinsMap);
        int padding = (maxCoins - minCoins) / 10;
        renderer.setYAxisMax(maxCoins + padding);
        renderer.setYAxisMin(minCoins - 2 * padding);
        renderer.clearXTextLabels();
        renderer.clearYTextLabels();
        renderer.setShowAxes(false);
        renderer.setShowGrid(false);
        renderer.setMarginsColor(DefaultRenderer.NO_COLOR);
        renderer.setBackgroundColor(DefaultRenderer.NO_COLOR); // TODO [yl] use color from theme
        renderer.setClickEnabled(false);
        renderer.setYTitle(getResources().getString(R.string.coins_graph_y_label));
        int k = 1;
        for (String name : orderedNames) {
            renderer.addXTextLabel(k, name);
            k++;
        }
    }

    private int findCoinsMax(Map<String, Integer> coinsMap) {
        return Collections.max(coinsMap.values());
    }

    private int findCoinsMin(Map<String, Integer> coinsMap) {
        return Collections.min(coinsMap.values());
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
            LoginActivity.OpenLoginScreen(getActivity(), false);
        }
        listView.setAdapter(adapter);
    }
}
