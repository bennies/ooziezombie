package com.booking.ooziezombie;

import java.util.Calendar;

public class Tools {
    // Disgusting hack to compare dates without the time. Not needed in java 8 or when using jodatime
    public static final Calendar removeTime(Calendar cal) {
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }
}
