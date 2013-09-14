package il.ac.huji.chores;

import java.io.Serializable;
import java.util.Date;

public interface Chore extends Serializable{

	//Possible statuses a chore.
	enum CHORE_STATUS{
		STATUS_DONE, STATUS_MISS, STATUS_FUTURE
	}
	public void setChoreInfoId(String id);
	
	public void setApartment(String apartmentId);
	public String getChoreInfoId();
	
	/*
	 * get the id of this chore in the DB.
	 */
	public String getId();

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
    
	public void setId(String id);

	public void setName(String name);

	public void setAssignedTo(String assignedTo);
	public void setStartsFrom(Date startsFrom);
	public void setDeadline(Date deadline);

	public void setStatus(CHORE_STATUS status);

	public void setType(String type);

	public void setFunFact(String funFact);

	public void setStatistics(String statistics);

	public void setCoinsNum(int coinsNum);

	public String getApartment();
}

