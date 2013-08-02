package il.ac.huji.chores;

import android.app.Fragment;

import il.ac.huji.chores.Chore.CHORE_STATUS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ApartmentChoresFragment extends Fragment {
    
    private ArrayAdapter<Chore> _adapter; // temporary
    
     @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         
        View view = inflater.inflate(R.layout.fragment_apartment, container, false);
        return view;
      }

     public void onActivityCreated (Bundle savedInstanceState)
     {
         super.onActivityCreated(savedInstanceState);
         
         getActivity().setContentView(R.layout.fragment_apartment);
         
         List<Chore> chores = new ArrayList<Chore>();
         
         chores.add(new ApartmentChore("wash dishes", "baba", new Date(2013, 4, 23), new Date(2013, 9, 30), CHORE_STATUS.STATUS_FUTURE, "kitchen chores", "fun dishes", "stat dishes",1));
         chores.add(new ApartmentChore("wash dishes", "blah",  new Date(2013, 6, 23),  new Date(2013, 9, 23), CHORE_STATUS.STATUS_DONE, "kitchen chores", "fun dishes", "stat dishes", 2));
         chores.add(new ApartmentChore("task1", "baba", new Date(2013, 9, 23), new Date(2013, 9, 30),CHORE_STATUS.STATUS_FUTURE, "general chore", "task1 is fun", "stat task1", 4));
         chores.add(new ApartmentChore("walk dog", "bob",  new Date(2013, 6, 23),  new Date(2013, 9, 23), CHORE_STATUS.STATUS_FUTURE, "outside chore", "fun walk dog", "stat walk dog", 4));
         chores.add(new ApartmentChore("task1", "baba", new Date(2013, 9, 23), new Date(2013, 9, 30), CHORE_STATUS.STATUS_FUTURE, "general chore", "task1 is fun", "stat task1", 4));
         chores.add(new ApartmentChore("task2","blah",  new Date(2013, 6, 23),  new Date(2013, 9, 23), CHORE_STATUS.STATUS_FUTURE, "general chore", "task2 is fun", "stat task2", 5));
         chores.add(new ApartmentChore("task2","baba", new Date(2013, 9, 23), new Date(2013, 9, 30), CHORE_STATUS.STATUS_FUTURE, "general chore", "task2 is fun", "stat task2", 5));
         chores.add(new ApartmentChore("task1","blah",  new Date(2013, 6, 23),  new Date(2013, 6, 23), CHORE_STATUS.STATUS_MISS, "general chore", "task1 is fun", "stat task1", 4));
            

         ListView listChores = (ListView)getActivity().findViewById(R.id.apartmentListChores);
         
         //set item click listener       
         listChores.setOnItemClickListener(new OnItemClickListener() {

            // if list chore item is clicked - open chore card
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
                //TODO
            }
        });
         
         //set adapter
         _adapter = new ApartmentChoresDisplayAdapter(getActivity(), chores);
         listChores.setAdapter(_adapter);
     }
}

