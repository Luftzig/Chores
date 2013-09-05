package il.ac.huji.chores.server;

import java.util.Date;

public interface Chore {

	/** Getters **/
	public String getName();
	
	public Date getStartFrom();
	
	public Date getDeadline();
	
	public String getAssignedTo();
	
	public int getValue();
	
	/** Setters **/
	
	public void setName(String name);
	
	public void setStartFrom(Date start);
	
	public void setDeadline(Date deadline);
	
	public void setAssignedTo(String assignedTo);
	
	public void setValue(int value);
	
}
