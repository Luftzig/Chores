package il.ac.huji.chores.dummy;

import java.io.IOException;

import il.ac.huji.chores.server.parse.*;
public class choresDevisionDummy {
	public static void main(String [] args){
		try {
			StringBuilder chore = ChoresDevisionRest.getChore("ORWzwCZayM");
			StringBuilder choreInfo = ChoresDevisionRest.getChoreInfo("DSCHUiDguR");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
