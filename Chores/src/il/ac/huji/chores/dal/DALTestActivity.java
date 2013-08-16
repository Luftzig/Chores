package il.ac.huji.chores.dal;

import il.ac.huji.chores.R;
import il.ac.huji.chores.R.layout;
import il.ac.huji.chores.R.menu;
import il.ac.huji.chores.RoommatesApartment;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import il.ac.huji.chores.RoommatesApartment;

public class DALTestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daltest);
		ApartmentDAL.Setup(this);
		RoommatesApartment apt = new RoommatesApartment();
		apt.setName("Anna's apartment");
		ApartmentDAL.createApartment(apt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.daltest, menu);
		return true;
	}

}
