package il.ac.huji.chores.exceptions;

public class UserNotLoggedInException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotLoggedInException(String message){
		super(message);
	}
}
