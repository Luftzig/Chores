package il.ac.huji.chores.dummy;

import java.io.IOException;
import java.util.List;

import il.ac.huji.chores.Roommate;
import il.ac.huji.chores.RoommatesApartment;
import il.ac.huji.chores.server.parse.*;
public class choresDevisionDummy {
	public static void main(String [] args){
		try {
			ParseRestClientImpl parse = new ParseRestClientImpl();
			
			//String chore = parse.getChore("ORWzwCZayM");
			//String choreInfo = parse.getChoreInfo("DSCHUiDguR");
			List<RoommatesApartment> aptList =parse.getApartmentList();
			System.out.println("aptList = "+aptList.toString());
			//List<String> ids =parse.getApartmentIds();
		//	System.out.println("ids = "+ids.toString());*/
			List<Roommate> roommates =  parse.getApartmentRoommates("qeXEIofK7q");
			System.out.println("roommates (qeXEIofK7q) = "+roommates.toString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
