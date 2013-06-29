package il.ac.huji.chores;


import il.ac.huji.chores.R;

import java.text.SimpleDateFormat;
import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ApartmentChoresDisplayAdapter extends ArrayAdapter<ChoreInterface>{
	
	static public final String dataPattern = "dd/MM/yyyy";

	public ApartmentChoresDisplayAdapter(Context context, List<ChoreInterface> courses) {
		super(context, R.layout.apartment_chores_table_row, courses);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ChoreInterface chore = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.apartment_chores_table_row, null);
			
		TextView assignedTo = (TextView)view.findViewById(R.id.assignedTo);
		TextView start = (TextView)view.findViewById(R.id.startsFrom);
		TextView deadline = (TextView)view.findViewById(R.id.deadline);
		
		assignedTo.setText("Assigned to:\n" + chore.getAssignedTo());
		
		//dates
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat(dataPattern);
		start.setText("Starts from:\n" + dateFormatGmt.format(chore.getStartsFrom()));
		deadline.setText("Deadline:\n" + dateFormatGmt.format(chore.getDeadline()));
		
		return view;
	}

}
