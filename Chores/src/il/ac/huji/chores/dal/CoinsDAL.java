package il.ac.huji.chores.dal;

import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import il.ac.huji.chores.Coins;
import il.ac.huji.chores.exceptions.FailedToGetChoreException;
import il.ac.huji.chores.exceptions.FailedToGetRoommateException;
import il.ac.huji.chores.exceptions.FailedToSaveOperationException;

public class CoinsDAL {

	public static ParseObject createDefaultCoinsForRoommate()
			throws ParseException {
		String username = RoommateDAL.getRoomateUsername();
		ParseObject coinsObj = new ParseObject("Coins");
		coinsObj.put("username", username);
		coinsObj.put("dept", 0);
		coinsObj.put("coinsCollected", 0);
		coinsObj.save();
		return coinsObj;
	}

	public static void updateCoinsToRoommates(Coins coins)
			throws ParseException {
		ParseObject coinsObj = getRoommageCoinsObj(coins.getUsername());
		if (coinsObj == null)
			coinsObj = createDefaultCoinsForRoommate();
		coinsObj.put("dept", coins.getDept());
		coinsObj.put("coinsCollected", coins.getCoinsCollected());
		coinsObj.save();
	}
	public static int getRoommateDebt(String username) throws ParseException{
		return getRoommageCoinsObj(username).getInt("dept");
	}
	
	public static void increaseCoinsCollectedDecreaseDebt(int coins) throws FailedToSaveOperationException, ParseException{
		String username = RoommateDAL.getRoomateUsername();
		increaseCoinsCollectedDecreaseDebt(username,coins);
	}
	
	public static void increaseCoinsCollectedDecreaseDebt(String roommate,int coins) throws FailedToSaveOperationException, ParseException{
		ParseObject coinsObj =getRoommageCoinsObj(roommate);
		coinsObj.increment("coinsCollected", coins);
		coinsObj.increment("dept",coins*(-1));
		coinsObj.save();
	}
	
	public static ParseObject getRoommageCoinsObj(String username)
			throws ParseException {
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Coins");
		query.whereEqualTo("username", username);
		List<ParseObject> results;
		results = query.find();
		if (results.size() > 0) {
			return results.get(0);
		} else {
			return null;
		}
	}
	
	public static Coins getRoommageCoins(String username)
			throws ParseException {
		Coins coins = new Coins();
		ParseObject coinsObj =getRoommageCoinsObj(username);
		coins.setCoinsCollected(coinsObj.getInt("coinsCollected"));
		coins.setDept(coinsObj.getInt("dept"));
		coins.setUsername("username");
		return coins;
		
	}

	public static void increaseCoinsCollectedDecreaseDebtAllRoommates(int coins, List<String> roommates) throws FailedToSaveOperationException, FailedToGetRoommateException, ParseException{
		for(String roommate : roommates){
			increaseCoinsCollectedDecreaseDebt(roommate,coins);
		}
	}

	public static int getRoommateCollectedCoins(String roommate) throws ParseException {
		return getRoommageCoinsObj(roommate).getInt("coinsCollected");
	}

}
