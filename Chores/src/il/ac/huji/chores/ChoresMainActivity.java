package il.ac.huji.chores;

import il.ac.huji.chores.dal.RoommateDAL;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityTestCase;
import android.util.Log;

public class ChoresMainActivity extends Activity {

	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_chores_main);
			
	        AppSetup setup = AppSetup.getInstance((Context) this);

			
	        String apartmentId = null;
	        try {
	            apartmentId = RoommateDAL.getApartmentID();
	        } catch (UserNotLoggedInException e) {
	            // TODO login
	        }
	        if (apartmentId == null) {
	            Intent intent = new Intent(this, NewApartmentDialogActivity.class);
	            startActivity(intent);
	            // TODO should get the apartmentID from the returned activity
	        }
	        // TODO remove this static login
	        
	 }
	 
//	//this will be called if there's a new ASSIGNED actions - new chores were assigned
//	  	protected void onNewIntent(Intent intent) {
//
//	  		
//	  		boolean onRightTab = false;//indication whether we're on the tab we need to see the update.
//	  		String dialogMsg = getResources().getString(R.string.my_chores_new_assigned_chores_notification_dialog_txt);
//	  		String action = intent.getAction();
//			if(action.equals(Constants.PARSE_NEW_CHORES_CHANNEL_KEY))
//			{
//				//handle inside fragment
//		  		MyChoresFragment fragment = (MyChoresFragment) getFragmentManager().findFragmentByTag("MyChoresFragment_tag");
//		  		fragment.onNewIntent(intent); 
//		  		
//		  		//get current tab
//		  		ActionBar bar = getActionBar();
//		  		ActionBar.Tab curSelected = bar.getSelectedTab();
//		  		if(curSelected.getText().equals(getResources().getString(R.string.action_bar_my_chores)) == false){
//		  			// if selected tab is not my chores, ask whether to open it.
//		  			dialogMsg += "\n" + getResources().getString(R.string.my_chores_new_assigned_chores_notification_dialog_txt_question);
//		  			onRightTab = true;
//		  		}
//		  		showNotificationDialog(dialogMsg, onRightTab, 0);//TODO change 0 to a constant of my chores tab
//			}
//	  	}
//	  	
//	  	private void showNotificationDialog(String dialogMsg, boolean onRightTab, int chosenTabLocation){
//	  		
//	  		final int chosen = chosenTabLocation;
//	  		
//	  		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//	  		builder.setMessage(dialogMsg);
//	  		
//	  		String positiveButtonTxt = null;
//	  		if(onRightTab){
//	  			positiveButtonTxt = "OK";
//	  		}
//	  		else{
//	  			positiveButtonTxt = "YES";
//	  		}
//
//	  		builder.setPositiveButton(positiveButtonTxt, new DialogInterface.OnClickListener() {
//
//	  		    public void onClick(DialogInterface dialog, int which) {
//	  		        dialog.dismiss();
//	  		  		ActionBar bar = getActionBar();
//			  		bar.getTabAt(chosen).select();
//	  		    }
//
//	  		});
//
//	  		if(!onRightTab){
//	  		
//	  			builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//
//	  				@Override
//	  				public void onClick(DialogInterface dialog, int which) {
//	  					dialog.dismiss();
//	  				}
//	  			});
//	  		}
//
//	  		AlertDialog alert = builder.create();
//	  		alert.show();
//	  	}
//	 

}
