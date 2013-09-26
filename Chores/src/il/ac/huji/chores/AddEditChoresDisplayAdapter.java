package il.ac.huji.chores;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AddEditChoresDisplayAdapter extends ArrayAdapter<ChoreInfo>{


    public AddEditChoresDisplayAdapter(Context context, List<ChoreInfo> infos) {
        super(context, R.layout.add_edit_chores_list_row, infos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.add_edit_chores_list_row, null);
        
        ChoreInfo info = getItem(position);
        
        TextView choreName = (TextView)view.findViewById(R.id.add_edit_row_title);
        choreName.setText(info.getName());

        return view;
    }

}
