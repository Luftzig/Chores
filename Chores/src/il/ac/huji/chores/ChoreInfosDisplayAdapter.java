package il.ac.huji.chores;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ChoreInfosDisplayAdapter extends ArrayAdapter<ChoreInfo> {

	public ChoreInfosDisplayAdapter(Context context, List<ChoreInfo> chores) {
        super(context, R.layout.chore_infos_table_row, chores);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        ChoreInfo chore = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.chore_infos_table_row, null);
       
        TextView choreName = (TextView)view.findViewById(R.id.AddEditChores_tableRow_choreName);
        choreName.setText(chore.getName());
        
        return view;
    }
}
