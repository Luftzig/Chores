package il.ac.huji.chores;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import il.ac.huji.chores.dal.RoommateDAL;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

public class ChoresMainActivity extends Activity {

    ChoresBroadcastReceiver receiver;
    private static boolean isActionBarSetup = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chores_main);

        AppSetup.getInstance(this);

        receiver = new ChoresBroadcastReceiver();
        registerReceiver(receiver, new IntentFilter("il.ac.huji.chores.choresNotification"));
        
        if(RoommateDAL.isUserLoggedIn()){ // user must be logged in
        	AppSetup.getInstance(this).setupActionBar();
        	isActionBarSetup = true;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("ChoresMainActivity.onRestart", "");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("ChoresMainActivity.onStop", "");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3) {
            //login/signup ok, create apartment
            String apartmentId = null;
            try {
                apartmentId = RoommateDAL.getApartmentID();
            } catch (UserNotLoggedInException e) {
                LoginActivity.OpenLoginScreen(this, false);
            }
            if (apartmentId == null) {
                askForCreateNewApartment();
            }
            if(!isActionBarSetup){
            	AppSetup.getInstance(this).setupActionBar();
            }
        }
    }
    
    private void askForCreateNewApartment() {
        final Context thisContext = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.setMessage(R.string.ask_to_create_apartment)
                .setPositiveButton(R.string.ask_to_create_apartment_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(thisContext, NewApartmentDialogActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.ask_to_create_apartment_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d("ChoresMainActivity.onDestroy", "");
        unregisterReceiver(receiver);
        AppSetup.destroy();
    }

    enum ACTION_BAR_TABS_ORDER {
        MY_CHORES,
        APARTMENT,
        STATISTICS,
        SETTINGS
    }

}
