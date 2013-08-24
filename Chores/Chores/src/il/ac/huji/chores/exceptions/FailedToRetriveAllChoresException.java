package il.ac.huji.chores.exceptions;

public class FailedToRetriveAllChoresException extends Exception{

	private static final long serialVersionUID = 1L;

	public FailedToRetriveAllChoresException(String message){
		super(message);
	}
}

