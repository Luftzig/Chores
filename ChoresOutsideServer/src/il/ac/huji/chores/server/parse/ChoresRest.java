package il.ac.huji.chores.server.parse;
import il.ac.huji.chores.ApartmentChore;
import il.ac.huji.chores.Chore;
import il.ac.huji.chores.Chore.CHORE_STATUS;
import il.ac.huji.chores.ChoreInfo;
import il.ac.huji.chores.ChoreInfo.CHORE_INFO_PERIOD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class ChoresRest {
	
	public static List<Chore> scheduleChores(String apartment){
		
		List<Chore> chores = new ArrayList<Chore>();
		ParseRestClientImpl parse = new ParseRestClientImpl();
		List<ChoreInfo> choreInfoList=null;
		try {
			choreInfoList = parse.getApartmentChoreInfos(apartment);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		Date currentDate = cal.getTime();
		
		for(ChoreInfo choreInfo :choreInfoList){
			System.out.println("Scheduling chore :"+choreInfo.getName());
			try {
				chores.add(scheduleChore(choreInfo,currentDate, apartment));
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			
		}
		
		return chores;
		
	}
	
	private static Chore scheduleChore(ChoreInfo choreInfo, Date currentDate,String apartment) throws ClientProtocolException, IOException{
		Chore chore = new ApartmentChore();
		chore.setChoreInfoId(choreInfo.getChoreInfoID());
		chore.setCoinsNum(choreInfo.getCoinsNum());
		chore.setName(choreInfo.getName());
		chore.setStartsFrom(currentDate);
		Date deadline = calculateDeadline(choreInfo,currentDate);
		chore.setDeadline(deadline);
		chore.setApartment(apartment);
		chore.setStatus(CHORE_STATUS.STATUS_FUTURE);
		
		return chore;
		
	}
	private static Date calculateDeadline(ChoreInfo choreInfo, Date currentDate){
		Calendar c = Calendar.getInstance();
		System.out.println("Current date : "+currentDate.toGMTString());
		c.setTime(currentDate); // Now use today date.
		int days=0;
		switch(choreInfo.getPriod()){
		
		case CHORE_INFO_DAY:
			break;	
		case CHORE_INFO_WEEK:
			days=7;
			break;
		case CHORE_INFO_MONTH:
			days = c.getActualMaximum(Calendar.DAY_OF_MONTH); 
			break;
		case CHORE_INFO_YEAR:
			days=365;
			break;		
		}
		c.add(Calendar.DATE, days);
		Date deadline = c.getTime();
		deadline.setHours(23);
		deadline.setMinutes(59);
		deadline.setSeconds(59);
		System.out.println("Calculated deadline : "+deadline.toGMTString());
		return deadline;
		
	}


}
