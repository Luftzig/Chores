package il.ac.huji.chores.dal;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import il.ac.huji.chores.ApartmentChore;
import il.ac.huji.chores.Chore;
import il.ac.huji.chores.Chore.CHORE_STATUS;
import il.ac.huji.chores.R;
import il.ac.huji.chores.Roommate;
import il.ac.huji.chores.exceptions.*;

import java.util.Date;
import java.util.List;

public class DALTestActivity extends Activity {

    @SuppressWarnings("deprecation")
	@Override
	protected void onStart() {
		super.onStart();
		setContentView(R.layout.activity_daltest);
		Parse.initialize(this,
				this.getResources().getString(R.string.parse_app_id), this
						.getResources().getString(R.string.parse_client_key));
		ParseACL defaultACL = new ParseACL();
		defaultACL.setPublicReadAccess(true);
		defaultACL.setPublicWriteAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
		
		try {
			Chore chore = new ApartmentChore(null, "chore", ParseUser.getCurrentUser().getUsername(), new Date(2012,1,1), new Date(2012,2,2), CHORE_STATUS.STATUS_DONE, null, null, null, 3);
			chore.setChoreInfoId("8O1y3GOmK6");
			ChoreDAL.addChore(chore);
			chore = new ApartmentChore(null, "chore", ParseUser.getCurrentUser().getUsername(), new Date(2012,1,1), new Date(2012,2,2), CHORE_STATUS.STATUS_FUTURE, null, null, null, 5);
			chore.setChoreInfoId("8O1y3GOmK6");
			ChoreDAL.addChore(chore);
			RoommateDAL.initRoommateProperties("037777777");
			Roommate roommate = RoommateDAL.getRoommateByName("anna");
			int dept=RoommateDAL.getRoommateDebt("anna");
			int coins =RoommateDAL.getRoommateCollectedCoins("anna");
			RoommateDAL.increaseCoinsCollected(5);
			roommate = RoommateDAL.getRoommateByName("anna");
		} catch (UserNotLoggedInException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (FailedToGetRoommateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FailedToGetChoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FailedToSaveOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//RoommateDAL.Login("anna", "anna123");
		/*RoommatesApartment apt = new RoommatesApartment();
		apt.setName("new apartment");
		apt.setDivisionDay("Sunday");
		apt.setDivisionFrequency("Twice a Week");
		
		String aptId=null ;
		try {
			aptId = ApartmentDAL.createApartment(apt);
			ApartmentDAL.addRoommateToApartment(aptId);
		} catch (ApartmentAlreadyExistsException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (UserNotLoggedInException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}*/
		/*ChoreInfo choreInfo = new ChoreInfoInstance();
		choreInfo.setChoreInfoName("chore");
		choreInfo.setPeriod(CHORE_INFO_PERIOD.CHORE_INFO_WEEK);
		choreInfo.setCoins(3);
		choreInfo.setHowMany(2);
		String choreInfoId=null;
	try {
		choreInfoId=ChoreDAL.addChoreInfo(choreInfo);
	} catch (UserNotLoggedInException e4) {
		// TODO Auto-generated catch block
		e4.printStackTrace();
	} catch (FailedToAddChoreInfoException e4) {
		// TODO Auto-generated catch block
		e4.printStackTrace();
	}
	*/
		/*
	Chore chore5 = new ApartmentChore(null, "chore", ParseUser.getCurrentUser().getObjectId(), new Date(2012,1,1), new Date(2012,2,2), CHORE_STATUS.STATUS_DONE, null, null, null, 3);
	chore5.setChoreInfoId("8O1y3GOmK6");
	try {
		ChoreDAL.addChore(chore5);
	} catch (ParseException e3) {
		// TODO Auto-generated catch block
		e3.printStackTrace();
	} catch (UserNotLoggedInException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	*/
	//ParseUser.logOut();
		//String roommateID =RoommateDAL.createRoommateUser("newRoommate",
		// "123123","roommate@gmail.com");
		try {
			RoommateDAL.Login("newRoommate", "123123");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	/*	try {
			ApartmentDAL.addRoommateToApartment(aptId);
		} catch (UserNotLoggedInException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}*/
		
		/*ChoreInfo choreInfo2 = new ChoreInfoInstance();
		choreInfo2.setChoreInfoName("chore2");
		choreInfo2.setPeriod(CHORE_INFO_PERIOD.CHORE_INFO_MONTH);
		choreInfo2.setCoins(5);
		choreInfo2.setHowMany(1);
		String choreInfoId2=null;*/
		//String choreInfoId2="Kfw0Sgo1Aa";
/*
		Chore chore = new ApartmentChore(null, "chore", ParseUser.getCurrentUser().getObjectId(), new Date(2012,1,1), new Date(2012,2,2), CHORE_STATUS.STATUS_DONE, null, null, null, 3);
		
		Chore chore2 = new ApartmentChore(null, "chore", ParseUser.getCurrentUser().getObjectId(), new Date(2013,1,1), new Date(2013,2,2), CHORE_STATUS.STATUS_MISS, null, null, null, 3);
		Chore chore3 = new ApartmentChore(null, "chore", ParseUser.getCurrentUser().getObjectId(), new Date(), new Date(), CHORE_STATUS.STATUS_FUTURE, null, null, null, 3);
		Chore chore4 = new ApartmentChore(null, "chore2", ParseUser.getCurrentUser().getObjectId(), new Date(), new Date(2014,1,1), CHORE_STATUS.STATUS_FUTURE, null, null, null, 3);
		chore.setChoreInfoId("8O1y3GOmK6");
		chore2.setChoreInfoId("8O1y3GOmK6");
		chore3.setChoreInfoId("8O1y3GOmK6");
		chore4.setChoreInfoId("8O1y3GOmK6");*/
		//chore5.setChoreInfoId(choreInfoId2);
		try {
			/*ChoreDAL.addChore(chore);
		ChoreDAL.addChore(chore2);
		ChoreDAL.addChore(chore3);
		ChoreDAL.addChore(chore4);
		*/
		//List<Chore> allChores = ChoreDAL.getAllChores();
		//List<Chore> roommateChores = ChoreDAL.getRoommatesChores();
		List<Chore> roommateOldChores = ChoreDAL.getUserAllOldChores();
		List<Chore> allOldUserChores = ChoreDAL.getUserOldChores("chore", 1);
		allOldUserChores = ChoreDAL.getUserOldChores("chore2", 5);
		ChoreDAL.updateCoins("8O1y3GOmK6", 4);
		RoommateDAL.Login("anna", "anna123");
		roommateOldChores = ChoreDAL.getUserAllOldChores();
		allOldUserChores = ChoreDAL.getUserOldChores("chore", 1);
		allOldUserChores = ChoreDAL.getUserOldChores("chore2", 5);
		
		/*} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();*/
		} catch (UserNotLoggedInException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		//}/* catch (FailedToRetriveAllChoresException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FailedToRetrieveOldChoresException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String choreID;
		
			//choreID = ChoreDAL.addChore(chore);
			//Chore choreResult  = ChoreDAL.getChore(choreID);
			try {
				List<Chore> allChores = ChoreDAL.getRoommatesChores();
			} catch (UserNotLoggedInException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		/*
		 * String apartmentID; try { apartmentID = RoommateDAL.getApartmentID();
		 * } catch (UserNotLoggedInException e1) { apartmentID = null; }
		 */
	/*	ChoreInfoInstance chore = new ChoreInfoInstance();
		chore.setChoreInfoName("Anna's chore");
		chore.setCoins(3);
		chore.setIsEveryone(true);
		chore.setHowMany(2);
		chore.setPeriod(ChoreInfo.CHORE_INFO_PERIOD.CHORE_INFO_MONTH);
		String choreId;*/
		/*try {
			// choreId = ChoreDAL.addChoreInfo(chore);
			/*
			 * ChoreDAL.updateChoreInfoName("jQY3c2QQ8o", "YOAV'S CHORE");
			 * ChoreDAL.updateCoins("jQY3c2QQ8o", 4);
			 * ChoreDAL.updateFrequency("jQY3c2QQ8o",1);
			 * ChoreDAL.updatePeriod("jQY3c2QQ8o",
			 * CHORE_INFO_PERIOD.CHORE_INFO_WEEK);
			 *
			List<ChoreInfo> list = ChoreDAL.getAllChoreInfo();

		} catch (UserNotLoggedInException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
		/*
		 * RoommatesApartment apt = new RoommatesApartment();
		 * apt.setName("apt");
		 */
		/*
		 * List<String> roommates = new ArrayList<String>(); //try {
		 * //apartmentID = ApartmentDAL.createApartment(apt); try {
		 * ApartmentDAL.addRoommateToApartment(apartmentID); } catch
		 * (UserNotLoggedInException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } //roommates =
		 * ApartmentDAL.getApartmentRoommates(apartmentID); //}// catch
		 * (ApartmentAlreadyExistsException e) { //
		 * Log.e("ApartmentAlreadyExistsException", e.toString()); //} catch
		 * (UserNotLoggedInException e) { // TODO Auto-generated catch block
		 * //e.printStackTrace(); //}
		 *catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.daltest, menu);
		return true;
	}

}
