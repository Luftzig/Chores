package il.ac.huji.chores;

import il.ac.huji.chores.ChoreInfo.CHORE_INFO_PERIOD;

public class ChoreInfoInstance implements ChoreInfo {

	private static final long serialVersionUID = 1L;
	private String _name;
	private int _coinsNum;
	private int _howMany;
	private CHORE_INFO_PERIOD _period;
	private boolean _isEveryone;
	private String _id;
	public ChoreInfoInstance(String name, int coinsNum, int howMany, CHORE_INFO_PERIOD period, boolean isEveryone){
		_name = name;
		_coinsNum = coinsNum;
		_howMany = howMany;
		_period = period;
		_isEveryone = isEveryone;
	}
	public ChoreInfoInstance(){
		
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

	@Override
	public String getChoreInfoID() {
		return _id;
	}

	@Override
	public void setChoreInfoID(String id) {
		_id=id;
		
	}
	@Override
	public void setChoreInfoName(String name) {
		this._name=name;
	}
	@Override
	public void setCoins(int coins) {
		this._coinsNum =coins;
		
	}
	@Override
	public void setIsEveryone(boolean isEveryone) {
		this._isEveryone = isEveryone;
		
	}
	@Override
	public void setHowMany(int amount) {
		this._howMany = amount;
		
	}
	@Override
	public void setPeriod(CHORE_INFO_PERIOD period) {
		this._period = period;
		
	}
	@Override
	public void setPeriod(String period) {
		this._period = CHORE_INFO_PERIOD.valueOf(period);
		
	}

	
}
