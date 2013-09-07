package il.ac.huji.chores.server;

import il.ac.huji.chores.Chore;
import il.ac.huji.chores.Roommate;
import il.ac.huji.chores.server.parse.ParseRestClient;
import il.ac.huji.chores.server.parse.ParseRestClientImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.http.client.ClientProtocolException;


public class ChoresDivisionAlgorithms {
	
	/**
	 * Gets the scheduled chores list and assign them to the roommates
	 * according to the random algorithm.
	 * chores - list of scheduled, not assigned chores.
	 * aptId - the apartment Id.
	 * Notice that when this function returns, there's garbage inn coinsCollected field.
	 */
	public static void assignChores(List<Chore> chores, String aptId){
		
		//get roommates set
		ParseRestClient client = new ParseRestClientImpl();
		List<Roommate> roommates=null;
		try {
			roommates = client.getApartmentRoommates(aptId);
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
		for(int i=0; i<sortedChores.size();){
			//The roommates list is sorted according to coins collected.
			//If roommate_1.coins == roommate_2.coins == ... == roommate_n.coins 
			//then their order should be random.
			Iterator<Roommate> itr = roommates.iterator();
			int prevVal = -1;
			Roommate roommate = null;
			while(itr.hasNext()){
				roommate = (Roommate)itr.next();
				
				if((roommate.get_coinsCollected() - roommate.get_dept()) == prevVal){
					equalCollected.add(roommate);
				}
				else{//add chores to roommates in list randomly and save start a new equal list
					
					int divided = randomlyAssignAndStartNewList(equalCollected, roommates, roommate, sortedChores, i);
					i += divided;
					if(divided != 0){ // divided == 0 is in case of first roommate in iterator.
						break;
					}
					else{
						prevVal = roommate.get_coinsCollected() - roommate.get_dept();
						equalCollected.add(roommate); // Start a new list.

					}
				}
			}
			if(!itr.hasNext()){//all coins collected are equal. chores are divided randomly.

				int divided = randomlyAssignAndStartNewList(equalCollected, roommates, roommate, sortedChores, i);
				i += divided;
			}
		}
		
	}
	
	/*
	 * Randomly assign chores from sortedChores list(starts from 'cur' place) to roommates in equalCollected list (all the roommates in equalCollected list
	 * have the same coinsCollected field). Then, the function clears the equalCollected list and add 'roommate' to it.
	 * Return number of chores that were already divided.
	 */
	private static int randomlyAssignAndStartNewList(List<Roommate> equalCollected, List<Roommate> roommates, Roommate roommate, List<Chore> sortedChores, int cur){
		
		int dividedChoresNum = equalCollected.size();
		
		if(equalCollected.size() !=0 ){
			Collections.shuffle(equalCollected);
			for(int k=0; (k<equalCollected.size() && cur< sortedChores.size()); k++, cur++){//divide chores to all roommates in equal coins collected list
				sortedChores.get(cur).setAssignedTo(equalCollected.get(k).get_username()); // assign chore to roommate.
				equalCollected.get(k).set_coinsCollected(equalCollected.get(k).get_coinsCollected() + sortedChores.get(cur).getCoinsNum()); //update coinsCollected (for the algorithm)
				Collections.sort(roommates, new ValueComparator());//coinsCollected was changed. sortList.
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

	public static List<Chore> scheduleChores(String aptId) {
		// TODO Auto-generated method stub
		return new ArrayList<Chore>();
	}

}
