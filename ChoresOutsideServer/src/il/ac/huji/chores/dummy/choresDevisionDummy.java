package il.ac.huji.chores.dummy;

import il.ac.huji.chores.ApartmentChore;
import il.ac.huji.chores.Chore;
import il.ac.huji.chores.server.NotificationsHandling;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;

public class choresDevisionDummy {

    public static void main(String[] args) throws ClientProtocolException, IOException {
        NotificationsHandling notifications = new NotificationsHandling();
        Chore chore = new ApartmentChore();
        chore.setName("wash dishes");
        chore.setAssignedTo("Yoav");
        chore.setApartment("Tl0A2RZUh8");
        NotificationsHandling.notifyMissedChore(chore);
    }

}
