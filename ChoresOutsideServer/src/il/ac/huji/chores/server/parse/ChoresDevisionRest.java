package il.ac.huji.chores.server.parse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class ChoresDevisionRest {
	
	public static StringBuilder addChore(String choreJson) throws ClientProtocolException, IOException{
		return  createObject("Chores",choreJson);
	}
	
	public static StringBuilder getChoreInfo(String choreInfoId) throws IOException{
		return getObject("ChoresInfo",choreInfoId);
	}
	
	public static StringBuilder getChore(String choreId) throws IOException{
		return getObject("Chores",choreId);
	}
	
	public static StringBuilder updateObject(String className,String id,String jsonObject) throws ClientProtocolException, IOException{
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

	
	public static StringBuilder createObject(String className, String jsonObject) throws IllegalStateException, IOException{
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("https://api.parse.com/1/classes/"+className);
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
	public static StringBuilder getObject(String className,String id) throws ClientProtocolException, IOException{
			HttpClient client = new DefaultHttpClient();
	        HttpGet request = new HttpGet("https://api.parse.com/1/classes/"+className+"/"+id);
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
	        return buffer;
	}

}
