package il.ac.huji.chores.dummy;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import il.ac.huji.chores.Roommate;
import il.ac.huji.chores.RoommatesApartment;
import il.ac.huji.chores.server.parse.*;
public class choresDevisionDummy {
	public static void main(String [] args){
	//	ParseRestClientImpl parse = new ParseRestClientImpl();
		
		//String chore = parse.getChore("ORWzwCZayM");
		//String choreInfo = parse.getChoreInfo("DSCHUiDguR");
//	List<RoommatesApartment> aptList =parse.getApartmentList();
//	System.out.println("aptList = "+aptList.toString());
	/*	List<String> ids=null;
		try {
			ids = parse.getApartmentIds();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	System.out.println("ids = "+ids.toString());
	List<Roommate> roommates;
	try {
		
		roommates = parse.getApartmentRoommates("qeXEIofK7q");
		System.out.println("roommates (qeXEIofK7q) = "+roommates.toString());
	} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
*/
		ChoresRest.scheduleChores("qeXEIofK7q");
	}
	
}
