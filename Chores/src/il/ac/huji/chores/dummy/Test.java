package il.ac.huji.chores.dummy;

import java.util.Calendar;

public class Test {

    public static void main(String[] args) {
        System.out.println("Testing calendar objects");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        System.out.println("Calendar now shows " + c.toString());
    }
}
