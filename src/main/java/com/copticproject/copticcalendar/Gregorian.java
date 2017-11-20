package com.copticproject.copticcalendar;

public class Gregorian {
    private static final int GregorianYearForFirstCopticYear = 283;
    private static final long MillisecondsPerDay = 24 * 60 * 60 * 1000;
    private static final int epochYear = 1970;

    public static Date ToCoptic(java.util.Date date) {
    }

    public static java.util.Date FromCoptic(Date date) {
    }

    private static java.util.Date FormDayOfYear(int day) {

    }

    private static int GetDayOfYear(java.util.Date date) {
        int daySinceEpoch = (int)(date.getTime() / MillisecondsPerDay);
        int year = epochYear
    }
}
