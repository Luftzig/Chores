package il.ac.huji.chores.exceptions;

public class FailedToGetApartmentSettings extends Exception{

	private static final long serialVersionUID = 1L;

	public FailedToGetApartmentSettings(String message){
		super(message);
	}
}
