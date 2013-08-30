package il.ac.huji.chores;

import java.util.List;

import android.util.Log;

public class MessagesToServer {

	/*
	 * Notify a a list of roommates.
	 * Kind - the kind of the message (roommate done/ roommate steal, ChoreInfos were added, etc)
	 * objectId - the message refers to the object with the parse object id objectId.
	 */
	public static void notifyRoomates(List<String> roommatesNames, String kind, String objectId){
		
		//TODO
		/////dummy... erase/////
		for(int i=0; i< roommatesNames.size(); i++){
			Log.e("notify roommate " + roommatesNames.get(i), "kind: "+ kind );
		}
		/////////////////////
	}
}
