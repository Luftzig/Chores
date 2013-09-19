package il.ac.huji.chores;

import org.json.JSONException;
import org.json.JSONObject;
import il.ac.huji.chores.dal.RoommateDAL;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.test.ActivityTestCase;
import android.util.Log;

public class ChoresMainActivity extends Activity {

	ActivityBroadcastReceiver receiver;
	static public boolean mainActivityRunning = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chores_main);
		mainActivityRunning = true;

		AppSetup setup = AppSetup.getInstance((Context) this);

		receiver = new ActivityBroadcastReceiver();
		registerReceiver(receiver, new IntentFilter("il.ac.huji.chores.choresNotification"));
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == 3){

			//login/signup ok, create apartment 
			String apartmentId = null;
			try {
				apartmentId = RoommateDAL.getApartmentID();
			} catch (UserNotLoggedInException e) {
				LoginActivity.OpenLoginScreen(this, false);

			 }

			if (apartmentId == null) {
				Intent intent = new Intent(this, NewApartmentDialogActivity.class);
				startActivity(intent);
				// TODO should get the apartmentID from the returned activity
			}

			AppSetup.setupActionBar(this);


		}
	}

	private class ActivityBroadcastReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent){
			
			ActionBar bar = ChoresMainActivity.this.getActionBar();
			ActionBar.Tab curSelected = bar.getSelectedTab();

			JSONObject jsonData;
			try {
				jsonData = new JSONObject(intent.getExtras().getString("com.parse.Data"));

				String type = jsonData.getString("notificationType");


				boolean onRightTab = (curSelected.getText().equals(getResources().getString(R.string.action_bar_apartment)) == false);
				int nextTab = 0;

				if(type.equals(Constants.PARSE_NEW_CHORES_CHANNEL_KEY)){
					nextTab = ACTION_BAR_TABS_ORDER.MY_CHORES.ordinal();
				}
				else{
					nextTab = ACTION_BAR_TABS_ORDER.APARTMET.ordinal();
				}

				showNotificationDialog(onRightTab, nextTab, type, jsonData);

			} catch (JSONException e) {
				return;
			}
		}
	}



	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}


	private void showNotificationDialog(final boolean onRightTab, int chosenTabLocation, final String type, final JSONObject jsonData){

		final int chosen = chosenTabLocation;
		String dialogMsg;
		try {
			dialogMsg = jsonData.getString("msg");
		} catch (JSONException e) {
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(dialogMsg);

		String positiveButtonTxt = "OK";
		boolean isNegButton = false;

		if(type.equals(Constants.PARSE_SUGGEST_CHANNEL_KEY) || false /**TODO: Yoav, add here the invite constant instead of false*/){
			positiveButtonTxt = "Yes";
			isNegButton = true;
		}
		
		if(isNegButton){
			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}

			});
		}

		builder.setPositiveButton(positiveButtonTxt, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

				if(!onRightTab){
					ActionBar bar = getActionBar();
					bar.getTabAt(chosen).select();
				}

				//If other things needs to be done, call function here 
				if(type.equals(Constants.PARSE_SUGGEST_CHANNEL_KEY)){
					try {
						ApartmentChoresFragment.doSuggestionAccepted(jsonData.get("choreId").toString(), getApplicationContext());
					} catch (JSONException e) {
						return;
					}
				}
				//TODO: Yoav, add condition add check and function call here.
			}
		});


		AlertDialog alert = builder.create();
		alert.show();
	}	

	enum ACTION_BAR_TABS_ORDER{
		MY_CHORES,
		APARTMET,
		STATISTICS,
		SETTINGS
	}
}


