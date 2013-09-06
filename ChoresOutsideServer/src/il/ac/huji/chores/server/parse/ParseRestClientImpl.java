package il.ac.huji.chores.server.parse;

import il.ac.huji.chores.Apartment;
import il.ac.huji.chores.Chore;
import il.ac.huji.chores.ChoreInfo;
import il.ac.huji.chores.Roommate;

import java.util.List;

/**
 * User: Yoav
 * Email: yoav.luft@gmail.com
 * Date: 06/09/13
 */
public class ParseRestClientImpl implements ParseRestClient {
    @Override
    public List<Roommate> getApartmentRoommates(String apartmentId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Apartment> getApartmentList() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<String> getApartmentIds() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ChoreInfo> getApartmentChoreInfos(String apartmentId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sendChores(String apartmentId, List<Chore> assignedChores) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
