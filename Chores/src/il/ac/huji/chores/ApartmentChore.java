package il.ac.huji.chores;

import java.util.Date;

public class ApartmentChore implements ChoreInterface{
	
	private String name;
	private String assignedTo;
	private Date startsFrom;
	private Date deadline;
	private CHORE_STATUS status;
	
	public ApartmentChore(String name, String assignedTo, Date startsFrom, Date deadline, CHORE_STATUS status)
	{
		this.name = name;;
		this.assignedTo = assignedTo;
		this. startsFrom = startsFrom;
		this.deadline = deadline;
		this.status = status;
	}
	
	public String getName() {
		return name;
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


	public CHORE_STATUS getStatus() {
		
		return status;
	}
	


}
