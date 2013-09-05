package il.ac.huji.chores;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MyChoresActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //AppSetup setup = AppSetup.getInstance((Context) this);
        setContentView(R.layout.activity_my_chores);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_chores, menu);
        return true;
    }

}
