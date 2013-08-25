package il.ac.huji.chores;

import il.ac.huji.chores.ChoreInfo.CHORE_INFO_PERIOD;

import java.io.Serializable;

public interface ChoreInfo extends Serializable{

	/*
	 * gets chore name
	 */
	public String getName();
	
	public String getChoreInfoID();
	
	public void setChoreInfoID(String id);
	
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
	
	/*
	 * Is the chore for everyone to do or just one roomate
	 */
	public boolean isEveryone();
	
	
	public void setChoreInfoName(String name);
	
	public void setCoins(int coins);
	
	public void setIsEveryone(boolean isEveryone);
	
	public void setHowMany(int amount);
	
	public void setPeriod(String period);
	
	
	/**
	 * A time period
	 *
	 */
	enum CHORE_INFO_PERIOD{
		CHORE_INFO_DAY, CHORE_INFO_WEEK, CHORE_INFO_MONTH, CHORE_INFO_YEAR, CHORE_INFO_NOT_REPEATED
	}


	void setPeriod(CHORE_INFO_PERIOD period);
}
