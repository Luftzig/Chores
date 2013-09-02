package il.ac.huji.chores.exceptions;


public class FailedToGetChoreException extends Exception{

	private static final long serialVersionUID = 1L;

	public FailedToGetChoreException(String message){
		super(message);
	}
}

