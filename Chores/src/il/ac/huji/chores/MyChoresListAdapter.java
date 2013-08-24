package il.ac.huji.chores;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;


public class MyChoresListAdapter extends ArrayAdapter<Chore> {

    private List<Chore> chores;

    public MyChoresListAdapter(Context context, List<Chore> chores) {
        super(context, R.layout.my_chores_list_row);
        this.chores = chores;
        Log.d("MyChoresListAdapter", "Created with " + chores.size() + " chores");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Chore chore = getItem(position);
        Log.d("MyChoresListAdapter", "chore " + chore);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.my_chores_list_row, null);
        TextView choreTitle = (TextView) view.findViewById(R.id.myChoresRowTitle);
        TextView choreDueDate = (TextView)view.findViewById(R.id.myChoresRowDueDate);

        choreTitle.setText(chore.getName());
        String choreDueString = DateFormat.getDateInstance(DateFormat.SHORT).format(chore.getDeadline());
        choreDueDate.setText(choreDueString);
        ImageButton editButton = (ImageButton)view.findViewById(R.id.myChoresRowEditButton);
        editButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChoreCardActivity.class);
                intent.putExtra(Constants.CHORE_CARD_OPEN, chore);
                v.getContext().startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public Chore getItem(int position) {
        return chores.get(position);
    }
}
