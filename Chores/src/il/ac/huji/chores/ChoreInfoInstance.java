package il.ac.huji.chores;

public class ChoreInfoInstance implements ChoreInfo {

	private static final long serialVersionUID = 1L;
	private String _name;
	private int _coinsNum;
	private int _howMany;
	private CHORE_INFO_PERIOD _period;
	private boolean _isEveryone;
	
	public ChoreInfoInstance(String name, int coinsNum, int howMany, CHORE_INFO_PERIOD period, boolean isEveryone){
		_name = name;
		_coinsNum = coinsNum;
		_howMany = howMany;
		_period = period;
		_isEveryone = isEveryone;
	}
	
	public String getName() {
		return _name;
	}

	@Override
	public int getCoinsNum() {
		return _coinsNum;
	}

	@Override
	public int getHowManyInPeriod() {
		return _howMany;
	}

	@Override
	public CHORE_INFO_PERIOD getPriod() {
		return _period;
	}

	@Override
	public boolean isEveryone() {
		return _isEveryone;
	}

	
}
