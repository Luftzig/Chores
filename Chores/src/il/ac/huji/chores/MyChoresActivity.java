package il.ac.huji.chores;

import il.ac.huji.chores.dal.ApartmentDAL;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyChoresActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chores);
        AppSetup setup = AppSetup.getInstance((Context) this);
        // Create new apartment dialog
       // Intent intent = new Intent(this, NewApartmentDialogActivity.class);
       // startActivity(intent);
        //ApartmentDAL.Setup(this);
		RoommatesApartment apt = new RoommatesApartment();
		apt.setName("Anna's apartment");
		//ApartmentDAL.createApartment(apt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_chores, menu);
        return true;
    }

}
