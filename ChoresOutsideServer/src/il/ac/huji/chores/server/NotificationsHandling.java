package il.ac.huji.chores.server;

import com.parse.ParseObject;
import il.ac.huji.chores.Chore;
import il.ac.huji.chores.Constants;
import il.ac.huji.chores.Roommate;
import il.ac.huji.chores.server.parse.ParseRestClientImpl;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static il.ac.huji.chores.Constants.ParseChannelKeys.PARSE_MISSED_CHANNEL_KEY;
import static il.ac.huji.chores.Constants.ParseChannelKeys.PARSE_NEW_CHORES_CHANNEL_KEY;

public class NotificationsHandling {

    private static final String CHORES_SERVER = "CHORES_SERVER";
    private static String PUSH_URL = "https://api.parse.com/1/push";

    public static void notifyNewChores(String apartmentId, Calendar earliest) throws ClientProtocolException, IOException {
        ParseRestClientImpl parse = new ParseRestClientImpl();
        List<Roommate> roommates = parse.getApartmentRoommates(apartmentId);
        String message = "New chores has been divided";
        JSONObject data = buildDataJson(message, PARSE_NEW_CHORES_CHANNEL_KEY.toString());
        List<String> channelsList = new ArrayList<String>();
        channelsList.add(PARSE_NEW_CHORES_CHANNEL_KEY.toString());
        JSONObject usersStatement = buildWhereRoommateStatement(roommates, channelsList);
        TimeZone timeZone = earliest.getTimeZone();
        Calendar roundedCalendar = Calendar.getInstance();
        roundedCalendar.set(earliest.get(Calendar.YEAR),
                            earliest.get(Calendar.MONTH),
                            earliest.get(Calendar.DAY_OF_MONTH));
        Date updateDayRounded = roundedCalendar.getTime();
        data.put("updateTime", updateDayRounded.getTime());
        data.put("timezone", timeZone.getID());

        //JSONArray channels = new JSONArray(channelsList);
        sendNotification(usersStatement, data);
        JSONObject timeJson = new JSONObject().put("updateTime", updateDayRounded.getTime()).put("timezone", timeZone.getID());
        updateTableHelper(PARSE_NEW_CHORES_CHANNEL_KEY, CHORES_SERVER, getRoommatesNames(roommates), timeJson);
    }

    //notify parse chore chore was missed (parse then need to notify the roommates)
    //This method is called after the chore status was updated to missed,
    public static void notifyMissedChore(Chore chore) throws ClientProtocolException, IOException {
        //get users list
        ParseRestClientImpl parse = new ParseRestClientImpl();
        List<Roommate> roommates = parse.getApartmentRoommates(chore.getApartment());
        String message = chore.getAssignedTo() + " has missed the chore '" + chore.getName() + "'";
        JSONObject data = buildDataJson(message, PARSE_MISSED_CHANNEL_KEY.toString());
        List<String> channelsList = new ArrayList<String>();
        channelsList.add(PARSE_MISSED_CHANNEL_KEY.toString());
        JSONObject usersStatement = buildWhereRoommateStatement(roommates, channelsList);

        //JSONArray channels = new JSONArray(channelsList);
        sendNotification(usersStatement, data);
        updateTableHelper(PARSE_MISSED_CHANNEL_KEY, chore.getAssignedTo(), getRoommatesNames(roommates), chore.getName());
    }

    private static void updateTableHelper(Constants.ParseChannelKeys type, String sender, List<String> target, Object info)
            throws IOException {
        ParseRestClientImpl parse = new ParseRestClientImpl();
        JSONObject notification = new JSONObject();
        notification.put("sender", sender);
        notification.put("info", info.toString());
        notification.put("target", target);
        notification.put("type", type.toString());
        parse.createObject("Notifications", notification.toString());
    }

    private static void sendNotification(JSONObject where, JSONObject data) throws ClientProtocolException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost put = new HttpPost(PUSH_URL);
        put.setHeader("X-Parse-Application-Id", "oNViNVhyxp6dS0VXvucqgtaGmBMFIGWww0sHuPGG");
        put.setHeader("X-Parse-REST-API-Key", "Tu5aHmbnn2Bz7AXVfSb2CPOng7LaoGkJHH0YbVXr");
        put.setHeader("Content-Type", "application/json");
        JSONObject json = new JSONObject();

        json.put("where", where);
        json.put("data", data);
        //json.put("username", users);
        //json.put("owner", "aTvZBFcxmh");
        System.out.println("FINAL JSON:" + json);
        StringEntity input = new StringEntity(json.toString());
        put.setEntity(input);
        HttpResponse response = client.execute(put);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        StringBuilder result = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
            result.append(line);
        }
    }

    public static JSONObject buildWhereRoommateStatement(List<Roommate> roommates, List<String> channels) {
        JSONArray jsonRoommates = convertRoommatesToJSonArray(roommates);
        JSONObject where = new JSONObject();
        JSONObject inChannels = new JSONObject();
        inChannels.put("$in", channels);
        where.put("channels", inChannels);
        where.put("username", (new JSONObject()).put("$in", jsonRoommates));
        System.out.println(where.toString());
        return where;
    }

    public static JSONArray convertRoommatesToJSonArray(List<Roommate> roommates) {
        List<String> usersList = getRoommatesNames(roommates);
        JSONArray jsonArr = new JSONArray(usersList);
        System.out.println(jsonArr.toString());
        return jsonArr;
    }

    private static List<String> getRoommatesNames(List<Roommate> roommates) {
        List<String> usersList = new ArrayList<String>();
        for (Roommate roommate : roommates) {
            usersList.add(roommate.getUsername());
        }
        return usersList;
    }

    public static JSONObject buildDataJson(String message, String notificationType) {
        JSONObject json = new JSONObject();
        json.put("msg", message);
        json.put("alert",message);
        //json.put("title",title);
        json.put("action", "il.ac.huji.chores.ChoresNotification");
        json.put("notificationType", notificationType);
        return json;

    }


}
