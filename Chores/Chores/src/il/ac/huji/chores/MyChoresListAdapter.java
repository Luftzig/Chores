package il.ac.huji.chores;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;


public class MyChoresListAdapter extends ArrayAdapter<Chore> {

    public MyChoresListAdapter(Context context, List<Chore> chores) {
        super(context, R.layout.my_chores_list_row);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Chore chore = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.my_chores_list_row, null);
        TextView choreTitle = (TextView) view.findViewById(R.id.myChoresRowTitle);
        TextView choreDueDate = (TextView)view.findViewById(R.id.myChoresRowDueDate);

        choreTitle.setText(chore.getName());
        return super.getView(position, convertView, parent);
    }

    @Override
    public void insert(Chore object, int index) {
        // TODO Auto-generated method stub
        super.insert(object, index);
    }

    @Override
    public void notifyDataSetChanged() {
        // TODO Auto-generated method stub
        super.notifyDataSetChanged();
    }

    @Override
    public void remove(Chore object) {
        // TODO Auto-generated method stub
        super.remove(object);
    }

    @Override
    public void setDropDownViewResource(int resource) {
        // TODO Auto-generated method stub
        super.setDropDownViewResource(resource);
    }

    @Override
    public void setNotifyOnChange(boolean notifyOnChange) {
        // TODO Auto-generated method stub
        super.setNotifyOnChange(notifyOnChange);
    }

    @Override
    public void sort(Comparator<? super Chore> comparator) {
        // TODO Auto-generated method stub
        super.sort(comparator);
    }

}
