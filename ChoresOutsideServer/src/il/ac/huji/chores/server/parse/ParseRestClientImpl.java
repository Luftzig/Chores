package il.ac.huji.chores.server.parse;

import il.ac.huji.chores.Apartment;
import il.ac.huji.chores.Chore;
import il.ac.huji.chores.ChoreInfo;
import il.ac.huji.chores.Roommate;
import il.ac.huji.chores.RoommatesApartment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * User: Yoav
 * Email: yoav.luft@gmail.com
 * Date: 06/09/13
 */
public class ParseRestClientImpl implements ParseRestClient {
	private static String BASE_URL = "https://api.parse.com/1/classes/";
    @Override
    public List<Roommate> getApartmentRoommates(String apartmentId) throws ClientProtocolException, IOException {
    	Map<String,String> whereCond = new HashMap<String,String>();
    	whereCond.put("apartmentID", apartmentId);
    	String result =  QueryWhere("_User",whereCond);
    	System.out.println("result = "+result);
    	JSONObject resultJson = new JSONObject(result);
    	JSONArray jsonArr = resultJson.getJSONArray("results");
    	List<Roommate> roommates = JsonConverter.convertArrToRoommatesList(jsonArr);
    	return roommates;
    }

    @Override
    public List<RoommatesApartment> getApartmentList() throws ClientProtocolException, IOException {
    	String result =  Query("Apartment");
    	System.out.println("result = "+result);
    	JSONObject resultJson = new JSONObject(result);
    	JSONArray jsonArr = resultJson.getJSONArray("results");
    	List<RoommatesApartment> apartmentList =JsonConverter.convertJsonArrayToApartmentList(jsonArr);
    	return apartmentList;
    	
    }

    @Override
    public List<String> getApartmentIds() throws ClientProtocolException, IOException {
    	String result =  Query("Apartment");
    	System.out.println("result = "+result);
    	JSONObject resultJson = new JSONObject(result);
    	JSONArray jsonArr = resultJson.getJSONArray("results");
    	List<String> ids = new ArrayList<String>();
    	for(int i = 0;i<jsonArr.length();i++){
    		ids.add(jsonArr.getJSONObject(i).getString("objectId"));
    	}
    	return ids;
    }

    @Override
    public List<ChoreInfo> getApartmentChoreInfos(String apartmentId) throws ClientProtocolException, IOException {
        Map<String,String> whereConditionsMap = new HashMap<String, String>();
        whereConditionsMap.put("apartment", apartmentId);
        String result = QueryWhere("ChoresInfo",whereConditionsMap);
        System.out.println("result = "+result);
        JSONObject resultJson = new JSONObject(result);
    	JSONArray jsonArr = resultJson.getJSONArray("results");
        List<ChoreInfo> choreInfoList = JsonConverter.convertJsonArrayToChoreInfoList(jsonArr);
        return choreInfoList;
    }

    @Override
    public void sendChores(String apartmentId, List<Chore> assignedChores) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    
	public String addChore(String choreJson) throws ClientProtocolException, IOException{
		return  createObject("Chores",choreJson);
	}
	
	public String getChoreInfo(String choreInfoId) throws IOException{
		return getObject("ChoresInfo",choreInfoId);
	}
	
	public String getChore(String choreId) throws IOException{
		return getObject("Chores",choreId);
	}
	
	public StringBuilder updateObject(String className,String id,String jsonObject) throws ClientProtocolException, IOException{
		HttpClient client = new DefaultHttpClient();
		HttpPut post = new HttpPut("https://api.parse.com/1/classes/"+className);
		post.setHeader("X-Parse-Application-Id", "oNViNVhyxp6dS0VXvucqgtaGmBMFIGWww0sHuPGG");
		post.setHeader("X-Parse-REST-API-Key", "Tu5aHmbnn2Bz7AXVfSb2CPOng7LaoGkJHH0YbVXr");
		post.setHeader("Content-Type", "application/json");
		 StringEntity input = new StringEntity(jsonObject);
	        post.setEntity(input);
	        HttpResponse response = client.execute(post);
	        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        String line = "";
	        StringBuilder result = new StringBuilder();
	        while ((line = rd.readLine()) != null) {
	            System.out.println(line);
	            result.append(line);
	        }
			return result;
	}

	
	public String createObject(String className, String jsonObject) throws IllegalStateException, IOException{
		
		return postRequest("https://api.parse.com/1/classes/"+className,jsonObject);
		
	}
	public String getObject(String className,String id) throws ClientProtocolException, IOException{
			return getRequest("https://api.parse.com/1/classes/"+className+"/"+id);
	}
	public String getRequest(String url) throws ClientProtocolException, IOException{
		HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        request.setHeader("X-Parse-Application-Id", "oNViNVhyxp6dS0VXvucqgtaGmBMFIGWww0sHuPGG");
        request.setHeader("X-Parse-REST-API-Key","Tu5aHmbnn2Bz7AXVfSb2CPOng7LaoGkJHH0YbVXr");

        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        StringBuilder buffer = new StringBuilder();
        while ((line = rd.readLine()) != null) {
          System.out.println(line);
          buffer.append(line);
        }
        return buffer.toString();
	}
	public String getApartment(String id) throws ClientProtocolException, IOException{
		return getObject("Apartment", id);
	}
	public String Query(String className) throws ClientProtocolException, IOException{
		return getRequest("https://api.parse.com/1/classes/"+className);
	}
	
	public String postRequest(String url,String body) throws ClientProtocolException, IOException{
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		post.setHeader("X-Parse-Application-Id", "oNViNVhyxp6dS0VXvucqgtaGmBMFIGWww0sHuPGG");
		post.setHeader("X-Parse-REST-API-Key", "Tu5aHmbnn2Bz7AXVfSb2CPOng7LaoGkJHH0YbVXr");
		post.setHeader("Content-Type", "application/json");
		
        StringEntity input = new StringEntity(body);
        post.setEntity(input);
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        StringBuilder result = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
            result.append(line);
        }
		return result.toString();
	}
	public String QueryWhere(String className, Map<String,String> keyValue) throws ClientProtocolException, IOException{
		String where = buildWhereStatement(keyValue);
		return getRequest(BASE_URL+className+"?"+where);
	}
	public String buildWhereStatement( Map<String,String> keyValue){

		return JsonConverter.whereConditionToJson(keyValue);

	}

	@Override
	public List<RoommatesApartment> getTodaysApartmentList(String day) {
		// TODO Auto-generated method stub
		return null;
	}
}
