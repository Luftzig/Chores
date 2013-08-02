package il.ac.huji.chores;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ChoreCardActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chore_card);
		
		//get arguments to the card
		Intent intent= getIntent();
		Chore choreInterface = (Chore)intent.getSerializableExtra("choreData");
		boolean ownerOpen = intent.getBooleanExtra("ownerOpen", false);
		
		//set argiments in card fragments
		ChoreCardFragment cardFrag = (ChoreCardFragment)getFragmentManager().findFragmentById(R.id.card_fragment);

		if (cardFrag != null && choreInterface != null) {
		 
			cardFrag.OrganizeUIParts(choreInterface, ownerOpen);
		}
		
	}
}
