package il.ac.huji.chores.dummy;

import il.ac.huji.chores.ApartmentChore;
import il.ac.huji.chores.Chore;
import il.ac.huji.chores.server.ChoresDivisionAlgorithms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DummyLoop {

	public static void main(String[] args){
		
		List<Chore> chores = new ArrayList<Chore>();
		
		chores.add(new ApartmentChore("washing dishes", new Date(2013-1900,10, 5), new Date(2013-1900,10, 10), 1));
		chores.add(new ApartmentChore("washing car", new Date(2013-1900,10, 12), new Date(2013-1900,10, 16), 6));
		chores.add(new ApartmentChore("walking horse", new Date(2013-1900,11, 5), new Date(2013-1900,11, 11), 5));
		
		chores.add(new ApartmentChore("washing dishes", new Date(2013-1900,10, 5), new Date(2013-1900,10, 10), 7));
		chores.add(new ApartmentChore("washing car", new Date(2013-1900,10, 12), new Date(2013-1900,10, 16),  0));
		chores.add(new ApartmentChore("walking horse", new Date(2013-1900,11, 5), new Date(2013-1900,11, 11) , 10));

		
		ChoresDivisionAlgorithms.assignChores(chores, "1234s");
		
	}
}
