package il.ac.huji.chores.exceptions;

public class UnauthorizedAccessException extends Exception{

	private static final long serialVersionUID = 1L;

	public UnauthorizedAccessException(String message){
		super(message);
	}
}