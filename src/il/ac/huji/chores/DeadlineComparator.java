package il.ac.huji.chores;

import java.util.Comparator;
import java.util.Date;

public class DeadlineComparator implements Comparator<Chore> {

	@Override
	public int compare(Chore chore1, Chore chore2) {
		
		Date deadline1 = chore1.getDeadline();
		Date deadline2 = chore2.getDeadline();
		
		if(deadline1.before(deadline2)){
			return -1;
		}
		else if(deadline1.equals(deadline2)){
			return 0;
		}
		else{
			return 1;
		}
	}

}
