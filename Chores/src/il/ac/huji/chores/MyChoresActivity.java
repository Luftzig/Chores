package il.ac.huji.chores;

import il.ac.huji.chores.dal.ApartmentDAL;
import il.ac.huji.chores.dal.RoommateDAL;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;

public class MyChoresActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chores);
        AppSetup setup = AppSetup.getInstance((Context) this);

        String apartmentId = null;
        try {
            apartmentId = RoommateDAL.getApartmentID();
            Log.d("MyChoresActivity", "ApartmentId = " + apartmentId);
        } catch (UserNotLoggedInException e) {
            Log.d("MyChoresActivity", "User not logged in");
            // TODO login
        }
        if (apartmentId == null) {
            Log.d("MyChoresActivity", "No apartment");
            Intent intent = new Intent(this, NewApartmentDialogActivity.class);
            startActivity(intent);
            // TODO should get the apartmentID from the returned activity
        }
        // TODO remove this static login
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_chores, menu);
        return true;
    }

}
