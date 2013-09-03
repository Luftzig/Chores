package il.ac.huji.chores.server;

import java.util.Date;

public class ChoreInstance implements Chore {

	String _name; 
	Date _startsFrom;
	Date _deadline;
	String _assignedTo;
	int _value;
	
	public ChoreInstance(){
		_name = null;
		_startsFrom = null;
		_deadline = null;
		_assignedTo = null;
		_value = 0;
	}
	
	//assignedTo should be set later. Not in the constructor.
	public ChoreInstance(String name, Date startsFrom, Date dealine, int value){
		_name = name;
		_startsFrom = startsFrom;
		_deadline = dealine;
		_assignedTo = null;
		_value = value;
	}
	
	@Override
	public String getName() {
		return _name;
	}

	@Override
	public Date getStartFrom() {
		return _startsFrom;
	}

	@Override
	public Date getDeadline() {
		return _deadline;
	}

	@Override
	public String getAssignedTo() {
		return _assignedTo;
	}

	@Override
	public void setName(String name) {
		_name = name;
	}

	@Override
	public void setStartFrom(Date start) {
		_startsFrom = start;
	}

	@Override
	public void setDeadline(Date deadline) {
		_deadline = deadline;
	}

	@Override
	public void setAssignedTo(String assignedTo) {
		_assignedTo = assignedTo;
		
	}

	@Override
	public int getValue() {
		return _value;
	}

	@Override
	public void setValue(int value) {
		_value = value;
		
	}



}
