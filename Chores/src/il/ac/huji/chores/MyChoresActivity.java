package il.ac.huji.chores;

import il.ac.huji.chores.MyChoresFragment.OnFragmentInteractionListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
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
        AppSetup setup = AppSetup.getInstance((Context)this);
        // DialogFragment newApartmentDialog = new NewApartmentDialogFragment();
        // newApartmentDialog.show(getFragmentManager(), "new_apartment");
        Button createButton = (Button)findViewById(R.id.myChoresCreateButton);
        createButton.setOnClickListener(new OnClickListener() {
            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RoommatesApartment apartment = new RoommatesApartment();
                apartment.setName("Anna's place");
                apartment.createApartment();
			}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_chores, menu);
        return true;
    }

}
