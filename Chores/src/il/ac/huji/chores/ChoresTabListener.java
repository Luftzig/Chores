package il.ac.huji.chores;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;

import java.util.Map;
import java.util.TreeMap;

public class ChoresTabListener implements TabListener {
	
	private static Map<Integer, Fragment> _fragList;
	private static final int MY_CHORE_CELL = 0;
	private static final int APARTMENT_CELL = 1;
	private static final int STATISTICS_CELL = 2;
	private static final int SETTINGS_CELL = 3;
	
	public ChoresTabListener(){
		super();
		if(_fragList == null){
			_fragList = new TreeMap<Integer, Fragment>();
		}
	}
	

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// Do nothing
	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
	
		Fragment frag = null;
		if(_fragList.containsKey(tab.getPosition())){
			frag = _fragList.get(tab.getPosition());
		}

        if (frag == null) {
            switch (tab.getPosition()) {
                case MY_CHORE_CELL:
                    _fragList.put(MY_CHORE_CELL, new MyChoresFragment());
                    break;
                case APARTMENT_CELL:
                    _fragList.put(APARTMENT_CELL, new ApartmentChoresFragment());
                    break;
                case STATISTICS_CELL:
                    _fragList.put(STATISTICS_CELL, new StatisticsFragment());
                    break;
                case SETTINGS_CELL:
                    _fragList.put(SETTINGS_CELL, new SettingsFragment());
                    break;
                default:
                    Log.e("error", "switch_default");
            }
        }
        ft.add(R.id.fragmentcontainer, _fragList.get(tab.getPosition()));
    }
 
	 @Override
	 public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (_fragList.get(tab.getPosition()) != null){
			   ft.remove(_fragList.get(tab.getPosition()));
		}
	 }
}
