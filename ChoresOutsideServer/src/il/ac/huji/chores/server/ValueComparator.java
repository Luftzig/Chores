package il.ac.huji.chores.server;

import il.ac.huji.chores.Roommate;

import java.util.Comparator;

public class ValueComparator implements Comparator<Roommate> {

	@Override
	public int compare(Roommate roommate1, Roommate roommate2) {

		return (roommate1.get_coinsCollected()) - (roommate2.get_coinsCollected());

	}

}
