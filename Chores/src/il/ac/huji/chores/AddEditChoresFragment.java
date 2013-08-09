package il.ac.huji.chores;
import il.ac.huji.chores.DAL.ChoreDAL;
import il.ac.huji.chores.dummy.ChoreInfoDal;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class AddEditChoresFragment extends Fragment{
	
    private ArrayAdapter<ChoreInfo> _adapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       
      View view = inflater.inflate(R.layout.fragment_add_edit_chores, container, false);
      return view;
    }
	
	 public void onActivityCreated (Bundle savedInstanceState)
     {
		 super.onActivityCreated(savedInstanceState);

		 getActivity().setContentView(R.layout.fragment_add_edit_chores);

		 ListView listChores = (ListView)getActivity().findViewById(R.id.AddEditChoresFragment_choresList);

		 //set item click listener on list items   
		 listChores.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			 // if list item is clicked - open edit chore
			 @Override
			 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				 final ChoreInfo choreInfo = (ChoreInfo) parent.getItemAtPosition(position);
				 CallNewChoreDialog(choreInfo, 1111);
			 }
		 });

		 //add new chore button
		 Button addChore = (Button)getActivity().findViewById(R.id.AddEditChoresFragment_Addchore_button);
		 addChore.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View view) {

				 CallNewChoreDialog(null, 2222);
			 }

		 });
		 
		 List<ChoreInfo> chores = ChoreInfoDal.getChoreInfos();
		 
		  //set adapter
         _adapter = new ChoreInfosDisplayAdapter(getActivity(), chores);
         listChores.setAdapter(_adapter);
         
         if(!_adapter.isEmpty()){
        	 //open add new chore automatically
        	 CallNewChoreDialog(null, 2222);
         }
     }
	 
	 /*
	  * Call new chore dialog with null or a ChoreInfo instance
	  * 
	  */
	 private void CallNewChoreDialog(ChoreInfo choreInfo, int requestCode){
		 
		 Intent intent = new Intent(getActivity(), NewChoreDialogActivity.class);
		 intent.putExtra(getResources().getString(R.string.new_chore_extra1_name), choreInfo);
		 startActivityForResult(intent, requestCode);
	 }
	 
	 /*
	  * Gets results (new ChoreInfo) from new chore dialog //TODO check if it's should be here or in activity
	  */
	 protected void onAactivityResult(int requestCode, int resultCode, Intent data) {
		 
		 ChoreInfo chore = (ChoreInfo)data.getSerializableExtra(getResources().getString(R.string.new_chore_extra1_name));
	 
		 if (requestCode == 1111 && resultCode == Activity.RESULT_OK) {//TODO check fragment result codes
			 ChoreDAL.updateChoreInfo(chore);
		 }
		 else if (requestCode == 2222 && resultCode == Activity.RESULT_OK) {
			 ChoreDAL.addChoreInfo(chore);
		 }
		 _adapter.add(chore);
	 }
}
