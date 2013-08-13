package il.ac.huji.chores;

import java.util.Date;

/**
 * 
 */
public class ApartmentChore implements Chore {
        
	private static final long serialVersionUID = 1L; //implements Serializable
	private String name;
    private String assignedTo;
    private Date startsFrom;
    private Date deadline;
    private CHORE_STATUS status;
    private String type;
    private String funFact;
    private String statistics;
    private int coinsNum;
    private String id;
    
    public ApartmentChore(String id, String name, String assignedTo, Date startsFrom, Date deadline,
                         CHORE_STATUS status, String type, String funFact, String statistics, int coinsNum){
        
    	this.id = id;
        this.name = name;;
        this.assignedTo = assignedTo;
        this. startsFrom = startsFrom;
        this.deadline = deadline;
        this.status = status;
        this.type = type;
        this.funFact = funFact;
        this.statistics = statistics;
        this.coinsNum = coinsNum;
    }
    
    public String getName() {
        return name;
    }
    
    public String getAssignedTo() {
        return assignedTo;
    }

    public Date getStartsFrom() {
        return startsFrom;
    }

    public Date getDeadline() {
        return deadline;
    }


    public CHORE_STATUS getStatus() {
        return status;
    }

    @Override
    public String getFunFact() {
        return funFact;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getStatistics() {
        return statistics;
    }

    @Override
    public int getCoinsNum() {
        return coinsNum;
    }

    @Override
    public String getPrintableDate(Date date) {
        
//      Calendar cal = Calendar.getInstance();
//
//      cal.setTime(date);
//      int curDay = cal.get(Calendar.DAY_OF_MONTH);
//      int curMonth = cal.get(Calendar.MONTH);
//      int curYear = cal.get(Calendar.YEAR);
//      
//      String time = String.format(DATE_FORMAT, curDay, curMonth, curYear % 100); //get only 2 last digits
        
        return DateFormatter.getPrintableDate(date);
    }
    
    @Override
    public int getStyle() {
        throw new UnsupportedOperationException("Styles are not supported yet");
    }

	@Override
	public String getID() {
		return id;
	}
}
