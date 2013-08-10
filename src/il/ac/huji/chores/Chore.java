package il.ac.huji.chores;

import java.io.Serializable;
import java.util.Date;

public interface Chore extends Serializable{

	//Possible statuses a chore.
	enum CHORE_STATUS{
		STATUS_DONE, STATUS_MISS, STATUS_FUTURE
	}

	/*
	 * gets chore name
	 */
	public String getName();
	
	/*
	 * gets chore's owner
	 */
	public String getAssignedTo();

	/*
	 * get chore's start
	 */
	public Date getStartsFrom();

	/*
	 * gets chore's deadline
	 */
	public Date getDeadline();
	
	/*
	 * gets chore's status
	 * 
	 */
	public CHORE_STATUS getStatus();
	
	/*
	 * gets a fun fact about the chore
	 */
	public String getFunFact();

	/*
	 * gets the chore type (family)
	 */
	// TODO: move to local (in chore card)
	public String getType(); 

	/*
	 * gets the current statistics about the chore
	 */
	public String getStatistics();
	
	/*
	 * get the number of coins this chore worth
	 */
	public int getCoinsNum();
	
	/**
	 * Given a Date object, returns it as a string
	 */
	public String getPrintableDate(Date date);

    /**
     * Return a style resource id for the chore item
     */
    public int getStyle();
}

