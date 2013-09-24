package il.ac.huji.chores.server;

import il.ac.huji.chores.Chore;
import il.ac.huji.chores.ChoreInfo;
import il.ac.huji.chores.Constants;
import il.ac.huji.chores.Roommate;
import il.ac.huji.chores.server.parse.ParseRestClient;
import il.ac.huji.chores.server.parse.ParseRestClientImpl;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.util.*;


public class ChoresDivisionAlgorithms {
	
	/**
	 * Gets the scheduled chores list and assign them to the roommates
	 * according to the random algorithm.
	 * chores - list of scheduled, not assigned chores.
	 * aptId - the apartment Id.
	 * Notice that when this function returns, there's garbage inn coinsCollected field.
	 */
	public static void assignChores(List<Chore> chores, String aptId){
		
		int everyoneDebt = 0; // sums the debt that should be added to everyone (in a group chore)
		
		//get roommates set
		ParseRestClient client = new ParseRestClientImpl();
		List<Roommate> roommates=null;
		try {
			roommates = client.getApartmentRoommates(aptId);
			if(roommates.size() == 0)
			{
				System.out.println("No roommates for apartment " + aptId);
				return;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(roommates, new ValueComparator());
		
		////TODO/////
		//DummyDal.printRoommates(roommates);
		/////////
				
		//Sort chores list, to start the division,
		List<Chore> sortedChores = sortChores(chores);
		
		List<Roommate> equalCollected = new ArrayList<Roommate>();
		
		//Divide chores
		Chore chore = null;
		for(int i=0; i<sortedChores.size();){
			
			chore = chores.get(i);
			if(chore.getAssignedTo().equals(Constants.CHORE_ASSIGNED_TO_EVERYONE)){
				everyoneDebt += chore.getCoinsNum();
				continue;
			}
						
			//The roommates list is sorted according to coins collected.
			//If roommate_1.coins == roommate_2.coins == ... == roommate_n.coins 
			//then their order should be random.
			Iterator<Roommate> itr = roommates.iterator();
			int prevVal = -1;
			Roommate roommate = null;
			while(itr.hasNext()){
				roommate = (Roommate)itr.next();
				
				if((roommate.getCoinsCollected()) == prevVal){
					equalCollected.add(roommate);
				}
				else{//add chores to roommates in list randomly and save start a new equal list
					
					int divided = randomlyAssignAndStartNewList(equalCollected, roommates, roommate, sortedChores, i);
					i += divided;
					if(divided != 0){ // divided == 0 is in case of first roommate in iterator.
						break;
					}
					else{
						prevVal = roommate.getCoinsCollected();
						equalCollected.add(roommate); // Start a new list.

					}
				}
			}
			if(!itr.hasNext()){//all coins collected are equal. chores are divided randomly.

				int divided = randomlyAssignAndStartNewList(equalCollected, roommates, roommate, sortedChores, i);
				i += divided;
			}
		}
		
		//update roommateDebts
		Roommate roommate = null;
		for(int i=0; i< roommates.size(); i++){
			roommate = roommates.get(i);
			client.setRommateDebt(roommate.getUsername(), roommate.getDebt() + everyoneDebt);
		}
		
	}
	
	/*
	 * Randomly assign chores from sortedChores list(starts from 'cur' place) to roommates in equalCollected list (all the roommates in equalCollected list
	 * have the same coinsCollected field). Then, the function clears the equalCollected list and add 'roommate' to it.
	 * Return number of chores that were already divided.
	 */
	private static int randomlyAssignAndStartNewList(List<Roommate> equalCollected, List<Roommate> roommates, Roommate roommate, List<Chore> sortedChores, int cur){
		
		int dividedChoresNum = equalCollected.size();
		Roommate curRoommate = null;
		Chore curChore = null;
		
		if(equalCollected.size() !=0){
			Collections.shuffle(equalCollected);
			for(int k=0; (k<equalCollected.size() && cur< sortedChores.size()); k++, cur++){//divide chores to all roommates in equal coins collected list
				curRoommate = equalCollected.get(k);
				curChore = sortedChores.get(cur);
				curChore.setAssignedTo(curRoommate.getUsername()); // assign chore to roommate.
				curRoommate.setCoinsCollected(curRoommate.getCoinsCollected() + curChore.getCoinsNum()); //update coinsCollected (for the algorithm)
				Collections.sort(roommates, new ValueComparator());//coinsCollected was changed. sortList.
				curRoommate.setDebt(curRoommate.getDebt() + curChore.getCoinsNum());// used to update roomate's debt in db later
			}
			equalCollected.clear(); // Clear to start a new list.
		}
				
		return dividedChoresNum;
	}

	/*
	 * Sort chores according to types. If types are A, B, C, D,
	 * create a random permutation, for example: B C A D, and the order will be: 
	 * B C A D B C A D, etc.
	 */
	private static List<Chore> sortChores(List<Chore> chores) {

		//Divide chores according to types
		Map<String, Stack<Chore>> sortedChores = new HashMap<String, Stack<Chore>>();
		Chore chore = null;
		for(int i=0; i < chores.size(); i++){
			
			chore = chores.get(i);
			Stack<Chore> stack = null;
			if(sortedChores.containsKey(chore.getName())){
				sortedChores.get(chore.getName()).push(chore);
			}
			else {
				
				stack = new Stack<Chore>();
				stack.push(chore);
				sortedChores.put(chore.getName(), stack);
			}
		}
		
		//Get a random permutation to the chore types
		List<String> choreTypes = new ArrayList<String>(sortedChores.keySet());
		Collections.shuffle(choreTypes);
		
		//Put chores back in the list according to a random order of the types
		List<Chore> orderedChores = new ArrayList<Chore>();
		int i = 0;
		String curChoreType = null;
		Stack<Chore> stack = null;
		
		while(i< chores.size()){
			for(int j=0; j<choreTypes.size(); j++){
				curChoreType = choreTypes.get(j);
				if(sortedChores.containsKey(curChoreType)){
					stack = sortedChores.get(curChoreType);
					if(stack.size() != 0){
						orderedChores.add(stack.pop());
						i++;
					}
				}
			}
		}
		
		return chores;
	}

}
