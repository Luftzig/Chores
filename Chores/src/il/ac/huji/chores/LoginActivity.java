package il.ac.huji.chores;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
	

	public static void OpenLoginScreen(Context context, boolean appSetup){
		Intent intent = new Intent(context, LoginActivity.class);
		if(appSetup)
		{
			((Activity) context).startActivityForResult(intent, 3);
		}
		else{
			((Activity) context).startActivityForResult(intent, -3); // the result won't be used
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        moveTaskToBack(true);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
