package il.ac.huji.chores;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Formats dates according to local information, including smart naming
 * ("today" or "tomorrow" instead of actual date).
 * This class should encapsulate all localization of dates stuff, like correct
 * formatting ('YYYY-MM-DD' for international, "DD/MM/YY" for israel, etc.)
 */
public class DateFormatter {

    // TODO Internationalize
    static public final String DATE_FORMAT = "dd/MM/yy";

    static public String getPrintableDate(Date date) {
        SimpleDateFormat dateForm = new SimpleDateFormat(DATE_FORMAT);
        return dateForm.format(date);
    }
}
