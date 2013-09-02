package il.ac.huji.chores.exceptions;


public class FailedToGetRoommateException extends Exception{

	private static final long serialVersionUID = 1L;

	public FailedToGetRoommateException(String message){
		super(message);
	}
}

