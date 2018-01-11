package com.copticproject.api.copticcalendar;

import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Gregorian {
    private static final int GregorianYearForFirstCopticYear = 283;
    private static final long MillisecondsPerDay = 24 * 60 * 60 * 1000;
    private static final int epochYear = 1970;

    // The first day of the first Coptic year was on 29th of August 284
    private static final GregorianCalendar DateForFirstDayOfFirstCopticYear =
            new GregorianCalendar(284, 7, 29);

    public static Date ToCoptic(GregorianCalendar gregorianDate) {
        GregorianCalendar date = (GregorianCalendar)gregorianDate.clone();

        date.setTimeZone(DateForFirstDayOfFirstCopticYear.getTimeZone()); // Set same time zone as comparison date

        date.set(GregorianCalendar.HOUR, 0); // Ignore time of the day
        date.set(GregorianCalendar.MINUTE, 0);
        date.set(GregorianCalendar.SECOND, 0);
        date.set(GregorianCalendar.MILLISECOND, 0);

        int absoluteCopticDays = 1 + (int)Math.round((date.getTime().getTime() - DateForFirstDayOfFirstCopticYear.getTime().getTime()) /
                (double)MillisecondsPerDay);

        return Date.createFromAbsoluteDays(absoluteCopticDays);
    }

    public static GregorianCalendar FromCoptic(Date copticDate) {
        GregorianCalendar gregorian = (GregorianCalendar)DateForFirstDayOfFirstCopticYear.clone();
        gregorian.add(GregorianCalendar.DAY_OF_YEAR, copticDate.getAbsoluteDays() - 1);

        return gregorian;
    }
}
