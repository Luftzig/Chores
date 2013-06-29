package il.ac.huji.chores;


import il.ac.huji.chores.ChoreInterface.CHORE_STATUS;
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

public class ApartmentChoresDisplayAdapter extends ArrayAdapter<ChoreInterface>{
	
	static public final String DATE_FORMAT = "Starts from:\n%02d/%02d/%02d";

	public ApartmentChoresDisplayAdapter(Context context, List<ChoreInterface> courses) {
		super(context, R.layout.apartment_chores_table_row, courses);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ChoreInterface chore = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.apartment_chores_table_row, null);
		
		//decide row color
		if(chore.getStatus() == CHORE_STATUS.STATUS_DONE){
			view.setBackgroundColor(Color.GREEN);
		}else if(chore.getStatus() == CHORE_STATUS.STATUS_FUTURE){
			view.setBackgroundColor(Color.LTGRAY);
		}else{
			view.setBackgroundColor(Color.RED);
		}
		
		TextView assignedTo = (TextView)view.findViewById(R.id.assignedTo);
		TextView start = (TextView)view.findViewById(R.id.startsFrom);
		TextView deadline = (TextView)view.findViewById(R.id.deadline);
		
		assignedTo.setText("Assigned to:\n" + chore.getAssignedTo());
		
		
		Calendar cal = Calendar.getInstance();
		//start time
		cal.setTime(chore.getStartsFrom());
		int curDay = cal.get(Calendar.DAY_OF_MONTH);
		int curMonth = cal.get(Calendar.MONTH);
		int curYear = cal.get(Calendar.YEAR);
		
		String time = String.format(DATE_FORMAT, curDay, curMonth, curYear % 100); //get only 2 last digits
		start.setText(time);
		
		//deadline time
		cal.setTime(chore.getDeadline());
		curDay = cal.get(Calendar.DAY_OF_MONTH);
		curMonth = cal.get(Calendar.MONTH);
		cal.get(Calendar.YEAR);
		
		time = String.format(DATE_FORMAT, curDay, curMonth, curYear % 100); //get only 2 last digits
		deadline.setText(time);
		
		return view;
	}

}
