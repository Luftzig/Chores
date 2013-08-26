package il.ac.huji.chores.exceptions;

public class FailedToUpdateSettingsException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FailedToUpdateSettingsException(String message){
		super(message);
	}
}
