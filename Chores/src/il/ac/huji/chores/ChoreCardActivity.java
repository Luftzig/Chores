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
		Chore chore = (Chore)intent.getSerializableExtra(getResources().getString(R.string.card_activity_extra1_name));
		String curUser = intent.getStringExtra(getResources().getString(R.string.card_activity_extra2_name));
		
		//set argiments in card fragments
		ChoreCardFragment cardFrag = (ChoreCardFragment)getFragmentManager().findFragmentById(R.id.card_fragment);

		if (cardFrag != null && chore != null) {
		 
			cardFrag.OrganizeUIParts(chore, curUser);
		}
		
	}
}
