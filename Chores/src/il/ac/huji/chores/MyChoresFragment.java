package il.ac.huji.chores;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    private ListView listView;
    private View progressBar;
    private FrameLayout chartFrame;
    private TextView messageBox;
    private String yTitle;

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
        final List<Chore>[] roommatesChores = new List[1];
        if (adapter != null) {
            adapter.clear();
            final View placeholder = ViewUtils.hideLoadingView(listView, getActivity(), R.id.progressBar);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        roommatesChores[0] = ChoreDAL.getRoommatesChores();
                    } catch (UserNotLoggedInException e) {
                        LoginActivity.OpenLoginScreen(getActivity(), false);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);    //To change body of overridden methods use File | Settings | File Templates.
                    adapter.addAll(roommatesChores[0]);
                    Log.d("MyChoresFragment", "chores list size == " + adapter.getCount());
                    adapter.notifyDataSetChanged();
                    ViewUtils.replacePlaceholder(listView, placeholder);
                }
            }.execute();
        }

        // Coins Chart stuff
        final ProgressBar progressBar = new ProgressBar(getActivity());
        chartFrame.removeAllViews();
        chartFrame.addView(progressBar, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);    //To change body of overridden methods use File | Settings | File Templates.
                chart = ChartFactory.getBarChartView(getActivity(), dataSet, renderer, BarChart.Type.DEFAULT);
                chartFrame.removeAllViewsInLayout();
                chartFrame.addView(chart);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    initChart();
                } catch (UserNotLoggedInException e) {
                    LoginActivity.OpenLoginScreen(getActivity(), false);
                } catch (FailedToGetRoommateException e) {
                    Log.w("MyChoresFragment.onResume", "error getting roommates", e);
                } catch (Exception e) {
                    Log.e("MyChoresFragment.onResume", "unexpected error while creating graph", e);
                }
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }.execute();
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
        renderer.setYTitle(yTitle);
        int k = 1;
        for (String name : orderedNames) {
            renderer.addXTextLabel(k, name);
            k++;
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
        listView = (ListView) getActivity().findViewById(R.id.myChoresFragmentListView);
        progressBar = ViewUtils.hideLoadingView(listView, getActivity(), R.id.progressBar);
        chartFrame = (FrameLayout) getActivity().findViewById(R.id.myChoresChartContainer);
        messageBox = (TextView) getActivity().findViewById(R.id.myChoresMessageBox);
        yTitle = getResources().getString(R.string.coins_graph_y_label);
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    chores = ChoreDAL.getRoommatesChores();
                } catch (UserNotLoggedInException e) {
                    LoginActivity.OpenLoginScreen(getActivity(), false);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);    //To change body of overridden methods use File | Settings | File Templates.
                ViewUtils.replacePlaceholder(listView, progressBar);
                adapter = new MyChoresListAdapter(getActivity(), chores);
                listView.setAdapter(adapter);
                if (adapter == null || adapter.getCount() == 0) {
                    messageBox.setText(R.string.my_chores_no_chores_message);
                    messageBox.setVisibility(View.VISIBLE);
                }
            }
        }.execute();
    }
}
