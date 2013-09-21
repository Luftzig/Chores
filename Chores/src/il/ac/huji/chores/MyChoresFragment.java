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
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.List;

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
        if (chart == null) {
            initChart();
            createDataSet();
            chart = ChartFactory.getBarChartView(getActivity(), dataSet, renderer, BarChart.Type.DEFAULT);
            ((ViewGroup) getActivity().findViewById(R.id.myChoresChartContainer)).addView(chart);
        }
    }

    private void createDataSet() {
        currentSeries.add(1, 100);
        currentSeries.add(1, 200);
        currentSeries.add(1, 250);
    }

    private void initChart() {
        currentSeries = new XYSeries("Sample Data");
        dataSet.addSeries(currentSeries);
        chartRenderer = new XYSeriesRenderer();
        renderer.addSeriesRenderer(chartRenderer);
        renderer.setBarSpacing(1);
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
