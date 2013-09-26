package il.ac.huji.chores;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class AddEditChoresActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_edit_chore);
	}

}
