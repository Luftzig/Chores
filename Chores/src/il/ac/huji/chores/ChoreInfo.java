package il.ac.huji.chores;

import java.io.Serializable;

public interface ChoreInfo extends Serializable{

	/*
	 * gets chore name
	 */
	public String getName();
	
	/*
	 * get the number of coins this chore worth
	 */
	public int getCoinsNum();
	
	/**
	 * Gets the number of times this chore should be distributed in the chosen period
	 */
	public int getHowManyInPeriod();
	
	/*
	 * Gets a time period. The chore should be distributed a number of times in this period
	 * (The number of times is returned from the getHowManyInPeriod function)
	 */
	public CHORE_INFO_PERIOD getPriod();
	
	
	/**
	 * A time period
	 *
	 */
	enum CHORE_INFO_PERIOD{
		CHORE_INFO_DAY, CHORE_INFO_WEEK, CHORE_INFO_MONTH, CHORE_INFO_YEAR
	}
}
