package il.ac.huji.chores;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Comparator;
import java.util.List;

/**
 * @author Yoav Luft
 */
public class StatisticsListAdapter extends ArrayAdapter<ChoreApartmentStatistics> {

    private final List<ChoreApartmentStatistics> statistics;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChoreApartmentStatistics statistic = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.statistics_expanded, null);
        view.setClickable(true);
        String choreName = statistic.getChoreName();
        ((TextView) view.findViewById(R.id.statisticsName)).setText(choreName);
        try {
            ChoreImage image = ChoreImage.fromString(choreName);
            if (image != null) {
                ((ImageView) view.findViewById(R.id.staticsImageView)).setImageResource(image.getDrawableId());
            }
        } catch (IllegalArgumentException e) {
            Log.e("StatisticsListAdapter.getView", choreName + " is not a legal enum value");
        }
        ((TextView) view.findViewById(R.id.statisticsApartmentAssigned)).setText(String.valueOf(statistic.getApartmentAssigned()));
        ((TextView) view.findViewById(R.id.statisticsApartmentMissed)).setText(String.valueOf(statistic.getApartmentMissed()));
        ((TextView) view.findViewById(R.id.statisticsTotalCount)).setText(String.valueOf(statistic.getTotalCount()));
        ((TextView) view.findViewById(R.id.statisticsWorldMissed)).setText(String.valueOf(statistic.getTotalMissed()));
        ((TextView) view.findViewById(R.id.statisticsWorldAssigned)).setText(String.valueOf(statistic.getTotalAssigned()));
        return view;
    }

    public StatisticsListAdapter(Context context, List<ChoreApartmentStatistics> statistics) {
        super(context, R.layout.statistics_expanded, statistics);
        this.statistics = statistics;
    }
}
