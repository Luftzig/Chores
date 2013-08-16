package il.ac.huji.chores.exceptions;

public class FailedToAddChoreInfoException extends Exception{

	private static final long serialVersionUID = 1L;

	public FailedToAddChoreInfoException(String message){
		super(message);
	}
}
