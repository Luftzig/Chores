package il.ac.huji.chores.server;

import java.util.HashMap;
import java.util.Map;

public class ChoresFunFacts {
	
	static private Map<String, String> funFacts = new HashMap<String, String>();
	
	private static void fillFactsMap(){
		
		funFacts.put("Cook dinner", "Peanuts are one of the ingredients in dynamite");
		funFacts.put("Dish washing", "The Mythbusters found that the dirtiest object in any house is usually the kitchen sponge");
		funFacts.put("Do the laundry", "The folks at Disney world do as much laundry daily as you would in 44 years");
		funFacts.put("Dusting", "Dust\'s components vary greatly from household to household");
		funFacts.put("Floor sweeping", "A witch\'s broomstick is in fact the most aerodynamic of all broomsticks");
		funFacts.put("Floor washing", "The original squeegee was originally used by fisherman to scrape fish scales");
		funFacts.put("Grocery shopping", "The best day of the week to hit the food store is Wednesday");
		funFacts.put("Take out the trash", "Landfills produce so much methane gas, making them a prime source of energy for ethanol plants");
		funFacts.put("Toilet cleaning", "The Mythbusters found that the surface of the toilet is usually the cleanest in the room");
		funFacts.put("Walk the dog", "Dogs are capable of understanding up to 250 words and gestures");
	}
	
	public static String getFactForChore(String choreName){
		
		if(funFacts.size() == 0){
			fillFactsMap();
		}
		return funFacts.get(choreName);
	}

}

