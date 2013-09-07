package il.ac.huji.chores;

public class Constants {

    // Constant strings
    public final static String CHORE_CARD_OPEN = "il.ac.huji.chores.CHORE_CARD_OPEN";
    public static final String PARSE_DONE_CHANNEL_KEY="DONE";
	public static final String PARSE_NEW_CHORES_CHANNEL_KEY="ASSIGNED";

    public static final String PARSE_FUNCTIONS_URL = "https://api.parse.com/1/functions/";

    public enum ChoreDivideDay { 
        SUNDAY, MONDAY, TUESDAY, WEDENSDAY,
        THURSDAY, FRIDAY, SATURDAY; 
    }

    public enum TimeScale {
        DAY, WEEK, MONTH, YEAR;
    }

    public enum ChoreDividePeriod {

        ONCE_A_WEEK(1, TimeScale.WEEK, 1),
        ONCE_2_WEEKS(1, TimeScale.WEEK, 2),
        ONCE_3_WEEKS(1, TimeScale.WEEK, 3),
        ONCE_4_WEEKS(1, TimeScale.WEEK, 4),
        ONCE_A_MONTH(1, TimeScale.MONTH, 1),
        ONCE_2_MONTH(1, TimeScale.MONTH, 2),
        ONCE_3_MONTH(1, TimeScale.MONTH, 3),
        ONCE_4_MONTH(1, TimeScale.MONTH, 4),
        ONCE_5_MONTH(1, TimeScale.MONTH, 5),
        ONCE_6_MONTH(1, TimeScale.MONTH, 6),
        TWICE_A_MONTH(2, TimeScale.MONTH, 1);

        private int howMany, inHowMuch;
        private TimeScale inTime;

        private ChoreDividePeriod(int howMany, TimeScale time, 
                int numPeriods) {
            this.howMany = howMany;
            inTime = time;
            inHowMuch = numPeriods;
        }
    }

}
