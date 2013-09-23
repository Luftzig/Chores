package il.ac.huji.chores.dummy;

import il.ac.huji.chores.ApartmentChore;
import il.ac.huji.chores.Chore;
import il.ac.huji.chores.server.ChoresDivisionAlgorithms;
import il.ac.huji.chores.server.RoutineJob;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DummyLoop {

    @SuppressWarnings("deprecation")
	public static void main(String[] args){
		
		List<Chore> chores = new ArrayList<Chore>();
		
		chores.add(new ApartmentChore("washing dishes", new Date(2013-1900,10, 5), new Date(2013-1900,10, 10), 1));
		chores.add(new ApartmentChore("washing car", new Date(2013-1900,10, 12), new Date(2013-1900,10, 16), 6));
		chores.add(new ApartmentChore("walking horse", new Date(2013-1900,11, 5), new Date(2013-1900,11, 11), 5));
		
		chores.add(new ApartmentChore("washing dishes", new Date(2013-1900,10, 5), new Date(2013-1900,10, 10), 7));
		chores.add(new ApartmentChore("washing car", new Date(2013-1900,10, 12), new Date(2013-1900,10, 16),  0));
		chores.add(new ApartmentChore("walking horse", new Date(2013-1900,11, 5), new Date(2013-1900,11, 11) , 10));


        String apartmentId = "Tl0A2RZUh8";
        ChoresDivisionAlgorithms.assignChores(chores, apartmentId);
        try {
            RoutineJob.DivideChoresForApartment(apartmentId, Calendar.getInstance().getTime());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
