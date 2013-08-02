package il.ac.huji.chores;


import il.ac.huji.chores.Chore.CHORE_STATUS;
import il.ac.huji.chores.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ApartmentChoresDisplayAdapter extends ArrayAdapter<Chore>{
    
    static public final String DATE__START = "Starts from:\n";
    static public final String DATE__END = "Deadline:\n";

    public ApartmentChoresDisplayAdapter(Context context, List<Chore> chores) {
        super(context, R.layout.apartment_chores_table_row, chores);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        Chore chore = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.apartment_chores_table_row, null);
        
        //decide row color
        // TODO use styles instead
        if (chore.getStatus() == CHORE_STATUS.STATUS_DONE){
            view.setBackgroundColor(Color.GREEN);
        }else if(chore.getStatus() == CHORE_STATUS.STATUS_FUTURE){
            view.setBackgroundColor(Color.WHITE);
        }else{
            view.setBackgroundColor(Color.RED);
        }
        
        //set chore info
        TextView choreName = (TextView)view.findViewById(R.id.choreName);
        TextView assignedTo = (TextView)view.findViewById(R.id.assignedTo);
        TextView start = (TextView)view.findViewById(R.id.startsFrom);
        TextView deadline = (TextView)view.findViewById(R.id.deadline);
        
        choreName.setText(chore.getName());
        assignedTo.setText("Assigned to:\n" + chore.getAssignedTo());
        
        //start time
        String time = chore.getPrintableDate(chore.getStartsFrom());
        start.setText(DATE__START + time);
        
        //deadline
        time = chore.getPrintableDate(chore.getDeadline());
        deadline.setText(DATE__END + time);
        
        return view;
    }

}
