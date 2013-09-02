package il.ac.huji.chores.exceptions;

public class FailedToSaveOperationException extends Exception {

	private static final long serialVersionUID = 1L;

	public FailedToSaveOperationException(String message) {
		super(message);
	}
}
