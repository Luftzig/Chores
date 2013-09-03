package il.ac.huji.chores.dummy;

import il.ac.huji.chores.server.Chore;
import il.ac.huji.chores.server.ChoreInstance;
import il.ac.huji.chores.server.ChoresDivisionAlgorithms;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DummyLoop {

	public static void test(){
		
		List<Chore> chores = new ArrayList<Chore>();
		
		chores.add(new ChoreInstance("washing dishes", new Date(2013-1900,10, 5), new Date(2013-1900,10, 10), 1));
		chores.add(new ChoreInstance("washing dishes", new Date(2013-1900,10, 12), new Date(2013-1900,10, 16), 3));
		chores.add(new ChoreInstance("washing dishes", new Date(2013-1900,11, 5), new Date(2013-1900,11, 11), 5));
		
		ChoresDivisionAlgorithms.assignChores(chores);
	}
}
