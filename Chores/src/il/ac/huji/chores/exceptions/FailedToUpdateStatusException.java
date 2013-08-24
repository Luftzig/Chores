package il.ac.huji.chores.exceptions;

public class FailedToUpdateStatusException extends Exception{

	private static final long serialVersionUID = 1L;

	public FailedToUpdateStatusException(String message){
		super(message);
	}
}
