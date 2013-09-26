package il.ac.huji.chores;

/**
 * @author Yoav Luft
 */
public class ChoreApartmentStatistics extends ChoreStatistics {

    private int apartmentAssigned;
    private int apartmentMissed;
    private int apartmentDone;

    public ChoreApartmentStatistics(ChoreStatistics other) {
        super(other.getChoreName(), other.getTotalCount(), other.getTotalMissed(), other.getTotalDone(),
                other.getTotalPoints(), other.getTotalAssigned(), other.getAverageValue());
    }

    public int getApartmentValue() {
        return apartmentValue;
    }

    public void setApartmentValue(int apartmentValue) {
        this.apartmentValue = apartmentValue;
    }

    public int getApartmentAssigned() {
        return apartmentAssigned;
    }

    public void setApartmentAssigned(int apartmentAssigned) {
        this.apartmentAssigned = apartmentAssigned;
    }

    public int getApartmentMissed() {
        return apartmentMissed;
    }

    public void setApartmentMissed(int apartmentMissed) {
        this.apartmentMissed = apartmentMissed;
    }

    public int getApartmentDone() {
        return apartmentDone;
    }

    public void setApartmentDone(int apartmentDone) {
        this.apartmentDone = apartmentDone;
    }

    private int apartmentValue;
}
