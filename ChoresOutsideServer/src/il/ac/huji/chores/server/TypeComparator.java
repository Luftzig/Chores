package il.ac.huji.chores.server;

import il.ac.huji.chores.Chore;
import java.util.Comparator;

public class TypeComparator implements Comparator<Chore>{

	@Override
	public int compare(Chore chore1, Chore chore2) {

		return (chore1.getName()).compareTo(chore2.getName());
	}

}
