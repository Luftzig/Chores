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
public class StatisticsListAdapter extends ArrayAdapter<StatisticsListAdapter.StatisticsRowWrapper> {

    public enum Display {
        EXPANDED,
        COMPACT
    }

    public static class StatisticsRowWrapper {
        public ChoreApartmentStatistics statistics;

        public StatisticsRowWrapper(ChoreApartmentStatistics statistics, Display displayType) {
            this.statistics = statistics;
            this.displayType = displayType;
        }

        public Display displayType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StatisticsListAdapter.StatisticsRowWrapper wrapper = getItem(position);
        ChoreApartmentStatistics statistic = wrapper.statistics;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view;
        String choreName = statistic.getChoreName();
        switch (wrapper.displayType) {
            case EXPANDED:
                view = inflater.inflate(R.layout.statistics_expanded, null);
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
            case COMPACT:
                view = inflater.inflate(R.layout.statistics_group, null);
                ((TextView) view.findViewById(R.id.statisticsName)).setText(choreName);
                try {
                    ChoreImage image = ChoreImage.fromString(choreName);
                    if (image != null) {
                        ((ImageView) view.findViewById(R.id.staticsImageView)).setImageResource(image.getDrawableId());
                    }
                } catch (IllegalArgumentException e) {
                    Log.e("StatisticsListAdapter.getView", choreName + " is not a legal enum value");
                }
                return view;
            default:
                return null;
        }
    }

    public StatisticsListAdapter(Context context, List<StatisticsRowWrapper> statistics) {
        super(context, R.layout.statistics_expanded, statistics);
    }
}
