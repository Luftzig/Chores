package il.ac.huji.chores.dummy;

import il.ac.huji.chores.ApartmentChore;
import il.ac.huji.chores.Chore;
import il.ac.huji.chores.dal.ChoreDAL;
import il.ac.huji.chores.exceptions.UserNotLoggedInException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;

import com.parse.ParseException;

public class DummyChoreDAL {

	public static List<Chore> getAllChores() {
    
		return createChores();
	}
	
	//add chores to real DB using the real DAL
	public static void addChores(){
		
		List<Chore> chores = createChores();
		
		  for(int i=0;i<chores.size();i++){
	    	  try {
	    		  Chore chore = chores.get(i);
	    		  //set choreInfo id
	    		  chore.setChoreInfoId(Integer.toString(i));
	    		  
	    		  ChoreDAL.addChore(chore);
				
				
			} catch (ParseException e) {
				Log.e("exception**********", "parse exception: " + e.getCode());
			} catch (UserNotLoggedInException e) {
				Log.e("exception**********", "not logged in exception");
			}
	      }
	}
	
	
	private static List<Chore> createChores(){
		 List<Chore> chores = new ArrayList<Chore>(); 
	      
	      chores.add(new ApartmentChore("1", "wash dishes", "baba", new Date(2013-1900, 4, 23), new Date(2013-1900, 9, 30), Chore.CHORE_STATUS.STATUS_FUTURE, "kitchen chores", "fun dishes", "stat dishes",1));
	      chores.add(new ApartmentChore("2", "wash room", "blah",  new Date(2013-1900, 6, 23),  new Date(2013-1900, 9, 23), Chore.CHORE_STATUS.STATUS_FUTURE, "kitchen chores", "fun dishes", "stat dishes", 2));
	      chores.add(new ApartmentChore("3", "task1", "baba", new Date(2013-1900, 9, 23), new Date(2013-1900, 9, 30),Chore.CHORE_STATUS.STATUS_FUTURE, "general chore", "task1 is fun", "stat task1", 4));
	      chores.add(new ApartmentChore("4", "walk dog", "bob",  new Date(2013-1900, 6, 23),  new Date(2013-1900, 9, 23), Chore.CHORE_STATUS.STATUS_FUTURE, "outside chore", "fun walk dog", "stat walk dog", 4));

	     
	
	      return chores;
	}

}
