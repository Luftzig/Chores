package il.ac.huji.chores;

import java.util.Date;

public class ApartmentChore implements ChoreInterface{
	
	private String assignedTo;
	private Date startsFrom;
	private Date deadline;
	
	public ApartmentChore(String assignedTo, Date startsFrom, Date deadline)
	{
		this.assignedTo = assignedTo;
		this. startsFrom = startsFrom;
		this.deadline = deadline;
	}
	
	
	public String getAssignedTo() {
		return assignedTo;
	}

	public Date getStartsFrom() {
		return startsFrom;
	}

	public Date getDeadline() {
		return deadline;
	}
	


}
