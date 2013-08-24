package il.ac.huji.chores.exceptions;

public class ApartmentAlreadyExistsException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ApartmentAlreadyExistsException(String message){
		super(message);
	}
}
