package il.ac.huji.chores;

import android.app.Fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ApartmentChoresFragment extends Fragment {
	
    private ArrayAdapter<ChoreInterface> _adapter; // temporary
	
	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 
	    View view = inflater.inflate(R.layout.fragment_apartment, container, false);
	    return view;
	  }

	 public void onActivityCreated (Bundle savedInstanceState)
	 {
		 
		 getActivity().setContentView(R.layout.fragment_apartment);
		 
	     List<ChoreInterface> chores = new ArrayList<ChoreInterface>();
	     
	     chores.add(new ApartmentChore("baba", new Date(2013, 9, 23), new Date(2013, 9, 30)));
	     chores.add(new ApartmentChore("blah",  new Date(2013, 6, 23),  new Date(2013, 9, 23)));
	        

	     ListView listCourses = (ListView)getActivity().findViewById(R.id.apartmentListChores);
	     _adapter = new ApartmentChoresDisplayAdapter(getActivity(), chores);
	     listCourses.setAdapter(_adapter);
	 }
}

