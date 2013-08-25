package il.ac.huji.chores;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NewChoreDialogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_chore_dialog);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_chore_dialog, menu);
		return true;
	}

}
