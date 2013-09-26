package il.ac.huji.chores;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;

public class NewChoreDialogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_new_chore_dialog);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_chore_dialog, menu);
		return true;
	}

}
