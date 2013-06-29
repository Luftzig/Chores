package il.ac.huji.chores;

import java.util.Date;

public interface ChoreInterface {

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
	
	
	//Possible statuses a chore.
	enum CHORE_STATUS{
		STATUS_DONE, STATUS_MISS, STATUS_FUTURE
	}
}

