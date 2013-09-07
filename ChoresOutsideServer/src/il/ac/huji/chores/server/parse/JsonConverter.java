package il.ac.huji.chores.server.parse;

import il.ac.huji.chores.Apartment;
import il.ac.huji.chores.ChoreInfo;
import il.ac.huji.chores.ChoreInfoInstance;
import il.ac.huji.chores.Roommate;
import il.ac.huji.chores.RoommatesApartment;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonConverter {

	public static String whereConditionToJson(Map<String, String> whereParams) {

		JSONObject whereCond = new JSONObject(whereParams);
		StringBuilder json = new StringBuilder();
		json.append("where=");
		json.append(URLEncoder.encode(whereCond.toString()));
	/*	Set<Entry<String,String>> whereSet=whereParams.entrySet();
		for(Entry<String,String> entry: whereSet){
			json.append(entry.getKey(), entry.getValue());
		}*/
		return json.toString();
	}

	public static RoommatesApartment convertJsonToApartment(JSONObject obj) {
		RoommatesApartment apt = new RoommatesApartment();
		List<String> roommates = obj.getJSONArray("Roommates").getStringList();
		apt.setRoommates(roommates);
		try{
		apt.setDivisionDay(obj.getString("divisionDay"));
		}
		catch(JSONException e){
			apt.setDivisionDay(null);
		}
		try{
		apt.setDivisionFrequency(obj.getString("frequency"));
		}
		catch(JSONException e){
			apt.setDivisionFrequency(null);
		}
		apt.setName(obj.getString("apartmentName"));
		return apt;
	}

	public static List<RoommatesApartment> convertJsonArrayToApartmentList(
			JSONArray arr) {
		List<RoommatesApartment> aptList = new ArrayList<RoommatesApartment>();
		List<JSONObject> objList = arr.getObjectsList();
		for (JSONObject obj : objList) {
			aptList.add(convertJsonToApartment(obj));
		}
		return aptList;
	}

	public static List<ChoreInfo> convertJsonArrayToChoreInfoList(JSONArray arr) {
		List<ChoreInfo> aptList = new ArrayList<ChoreInfo>();
		List<JSONObject> objList = arr.getObjectsList();
		for (JSONObject obj : objList) {
			aptList.add(convertJsonToChoreInfo(obj));
		}
		return aptList;
	}

	public static List<Roommate> convertArrToRoommatesList(JSONArray arr) {
		List<Roommate> aptList = new ArrayList<Roommate>();
		List<JSONObject> objList = arr.getObjectsList();
		for (JSONObject obj : objList) {
			aptList.add(convertJsonToRoommate(obj));
		}
		return aptList;
	}

	public static Roommate convertJsonToRoommate(JSONObject obj) {
		Roommate roommate = new Roommate();
		roommate.set_coinsCollected(obj.getInt("coinsCollected"));
		roommate.set_dept(obj.getInt("coins"));
		roommate.set_id(obj.getString("objectId"));
		roommate.set_username(obj.getString("username"));
		return roommate;
	}

	public static ChoreInfo convertJsonToChoreInfo(JSONObject obj) {
		ChoreInfo choreInfo = new ChoreInfoInstance();
		choreInfo.setChoreInfoID(obj.getString("objectId"));
		choreInfo.setChoreInfoName(obj.getString("choreName"));
		choreInfo.setCoins(obj.getInt("coins"));
		choreInfo.setHowMany(obj.getInt("frequency"));
		choreInfo.setIsEveryone(obj.getBoolean("isEveryone"));
		choreInfo.setPeriod(obj.getString("period"));
		return choreInfo;

	}

}
