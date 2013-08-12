package il.ac.huji.chores.dummy;

import il.ac.huji.chores.ApartmentChore;
import il.ac.huji.chores.Chore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DummyChoreDAL {

	public static List<Chore> getAllChores() {

      List<Chore> chores = new ArrayList<Chore>(); 
      
      chores.add(new ApartmentChore("wash dishes", "baba", new Date(2013-1900, 4, 23), new Date(2013-1900, 9, 30), Chore.CHORE_STATUS.STATUS_FUTURE, "kitchen chores", "fun dishes", "stat dishes",1));
      chores.add(new ApartmentChore("wash dishes", "blah",  new Date(2013-1900, 6, 23),  new Date(2013-1900, 9, 23), Chore.CHORE_STATUS.STATUS_DONE, "kitchen chores", "fun dishes", "stat dishes", 2));
      chores.add(new ApartmentChore("task1", "baba", new Date(2013-1900, 9, 23), new Date(2013-1900, 9, 30),Chore.CHORE_STATUS.STATUS_FUTURE, "general chore", "task1 is fun", "stat task1", 4));
      chores.add(new ApartmentChore("walk dog", "bob",  new Date(2013-1900, 6, 23),  new Date(2013-1900, 9, 23), Chore.CHORE_STATUS.STATUS_FUTURE, "outside chore", "fun walk dog", "stat walk dog", 4));
      chores.add(new ApartmentChore("task1", "baba", new Date(2013-1900, 9, 23), new Date(2013-1900, 9, 30), Chore.CHORE_STATUS.STATUS_FUTURE, "general chore", "task1 is fun", "stat task1", 4));
      chores.add(new ApartmentChore("task2","blah",  new Date(2013-1900, 6, 23),  new Date(2013-1900, 9, 23), Chore.CHORE_STATUS.STATUS_FUTURE, "general chore", "task2 is fun", "stat task2", 5));
      chores.add(new ApartmentChore("task2","baba", new Date(2013-1900, 9, 23), new Date(2013-1900, 9, 30), Chore.CHORE_STATUS.STATUS_FUTURE, "general chore", "task2 is fun", "stat task2", 5));
      chores.add(new ApartmentChore("task1","blah",  new Date(2013-1900, 6, 23),  new Date(2013-1900, 6, 23), Chore.CHORE_STATUS.STATUS_MISS, "general chore", "task1 is fun", "stat task1", 4));
         
      return chores;
	}

}
