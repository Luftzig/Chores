package il.ac.huji.chores.server;

import il.ac.huji.chores.Chore;

import java.util.List;

public class ChoresServerMain {

	public static void main(String[] args){
		
		
	}
	
	
	/**
	 * Handle one apartment chores division.
	 * After Division, puts scheduled and assigned chores in the DB,
	 * and send notification.
	 */
	public static void DivideChoresForApartment(String apartmentId){
		
		//Schedule chores
		List<Chore> chores  = ChoresDivisionAlgorithms.scheduleChores(apartmentId);
		
		//Assign chores
		ChoresDivisionAlgorithms.assignChores(chores, apartmentId);
		
		//Notify roommates about the new chores
		NotificationsHandling.notifyNewChores();
		
	}
}
