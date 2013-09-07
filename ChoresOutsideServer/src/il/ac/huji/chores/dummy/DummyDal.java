package il.ac.huji.chores.dummy;

import il.ac.huji.chores.Roommate;
import java.util.ArrayList;
import java.util.List;


public class DummyDal {

	public static List<Roommate> getRoommates(String aptId){
		
		ArrayList<Roommate> roommates = new ArrayList<Roommate>();
		
		Roommate a = new Roommate();
		a.set_username("Alf"); a.set_dept(2); a.set_coinsCollected(14);
		Roommate b = new Roommate();
		b.set_username("Bob"); b.set_dept(1); b.set_coinsCollected(6);
		Roommate c = new Roommate();
		c.set_username("Guy"); c.set_dept(0); c.set_coinsCollected(6);
		Roommate d = new Roommate();
		d.set_username("Dan"); d.set_dept(0); d.set_coinsCollected(6);
		Roommate e = new Roommate();
		e.set_username("Ester"); e.set_dept(0); e.set_coinsCollected(6);
		
		roommates.add(a); 
		roommates.add(b); 
		roommates.add(c); 
		roommates.add(d); 
		roommates.add(e); 

		
		return roommates;
	}
	
	public static void printRoommates(List<Roommate> roommates){
		
		for(int i=0; i < roommates.size(); i++){
			System.out.println("roommate name: " + roommates.get(i).get_username() + ", coinsCollected+debt: " + (roommates.get(i).get_coinsCollected() - roommates.get(i).get_dept()));
		}
	}
}
