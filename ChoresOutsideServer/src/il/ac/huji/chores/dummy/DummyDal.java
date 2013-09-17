package il.ac.huji.chores.dummy;

import il.ac.huji.chores.Roommate;

import java.util.ArrayList;
import java.util.List;


public class DummyDal {

	public static List<Roommate> getRoommates(String aptId){
		
		ArrayList<Roommate> roommates = new ArrayList<Roommate>();
		
		Roommate a = new Roommate();
		a.setUsername("Alf"); a.setDebt(2); a.setCoinsCollected(14);
		Roommate b = new Roommate();
		b.setUsername("Bob"); b.setDebt(1); b.setCoinsCollected(6);
		Roommate c = new Roommate();
		c.setUsername("Guy"); c.setDebt(0); c.setCoinsCollected(6);
		Roommate d = new Roommate();
		d.setUsername("Dan"); d.setDebt(0); d.setCoinsCollected(6);
		Roommate e = new Roommate();
		e.setUsername("Ester"); e.setDebt(0); e.setCoinsCollected(6);
		
		roommates.add(a); 
		roommates.add(b); 
		roommates.add(c); 
		roommates.add(d); 
		roommates.add(e); 

		
		return roommates;
	}
	
	public static void printRoommates(List<Roommate> roommates){
		
		for(int i=0; i < roommates.size(); i++){
			System.out.println("roommate name: " + roommates.get(i).getUsername() + ", coinsCollected+debt: " + (roommates.get(i).getCoinsCollected() - roommates.get(i).getDebt()));
		}
	}
}
