package il.ac.huji.chores;

import java.util.Comparator;

public class ChoreDateComparator implements Comparator<Chore> {

	public int compare(Chore c1, Chore c2){
		if(c1.getDeadline().before(c2.getDeadline())){
			return -1;
		}
		else if(c1.getDeadline().after(c2.getDeadline())){
			return 1;
		}
		else{
		return 0;
		}
	}
}
