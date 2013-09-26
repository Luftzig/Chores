package il.ac.huji.chores;

public class ChoreStatistics {
	
	private String choreName;
	private int totalCount;
	private int totalMissed;
	private int totalDone;
	private int totalPoints;

    public ChoreStatistics(String choreName, int totalCount, int totalMissed, int totalDone, int totalPoints,
                           int totalAssigned, int averageValue) {
        this.choreName = choreName;
        this.totalCount = totalCount;
        this.totalMissed = totalMissed;
        this.totalDone = totalDone;
        this.totalPoints = totalPoints;
        this.totalAssigned = totalAssigned;
        this.averageValue = averageValue;
    }

    public ChoreStatistics() {
    }

    public int getTotalAssigned() {
        return totalAssigned;
    }

    public void setTotalAssigned(int totalAssigned) {
        this.totalAssigned = totalAssigned;
    }

    private int totalAssigned;
	private int averageValue;
	
	public String getChoreName() {
		return choreName;
	}
	public void setChoreName(String choreName) {
		this.choreName = choreName;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getTotalMissed() {
		return totalMissed;
	}
	public void setTotalMissed(int totalMissed) {
		this.totalMissed = totalMissed;
	}
	public int getTotalDone() {
		return totalDone;
	}
	public void setTotalDone(int totalDone) {
		this.totalDone = totalDone;
	}
	public int getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}
	public int getAverageValue() {
		return averageValue;
	}
	public void setAverageValue(int averageValue) {
		this.averageValue = averageValue;
	}

	

}
