package il.ac.huji.chores.dal;

import il.ac.huji.chores.RoommatesApartment;

public class DALTest {
	public static void main(String[] args){
		//System.out.println("before adding new apartment");
		RoommatesApartment apt = new RoommatesApartment();
		String aptName = "Anna's apartment";
		apt.setName(aptName);
		ApartmentDAL.createApartment(apt);
		//System.out.println("after adding new apartment");
	}
}
