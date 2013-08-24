package il.ac.huji.chores.exceptions;

public class FailedToRetrieveOldChoresException extends Exception{

	private static final long serialVersionUID = 1L;

	public FailedToRetrieveOldChoresException(String message){
		super(message);
	}
}

