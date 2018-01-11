package com.copticproject.api.copticcalendar;

import java.util.GregorianCalendar;

public class Easter {

    private final static int yearsInLunarCycle = 19;
    private final static int yearsInCentury = 100;
    private final static int leapYearFrequency = 4;
    private final static int gregorianCenturyCycle = 4;
    private final static int maximumYearSupported = 4099;
    private final static int minimumYearSupported = 1583;

    private static GregorianCalendar getEasterDate(int year, Boolean isEastern) {
//        if (year < minimumYearSupported || year > maximumYearSupported)
//            throw new ArgumentException(string.Format("Gregorian calendar Easters apply for years {0} to {1} only", MinimumYearSupported, MaximumYearSupported), "year");

        // Calculate golden number, century number, and difference between the two calendars for this year
        int golderNumber = year % yearsInLunarCycle;
        int centuryNumber = year / yearsInCentury;
        int daysDifferenceBetweenJulianAndGregorianCalendar = centuryNumber - centuryNumber / gregorianCenturyCycle - 2;

        // Calculate days from March 21st till Pascha full moon
        int daysFromMarch21ToPaschalFullMoon;
        if (isEastern)
            // Julian calendar calculation
            daysFromMarch21ToPaschalFullMoon = (yearsInLunarCycle * golderNumber + 15) % 30;
        else {
            // Gregorian calendar calculation
            int h = (centuryNumber - centuryNumber / gregorianCenturyCycle -
                    (8 * centuryNumber + 13) / 25 + yearsInLunarCycle * golderNumber + 15) % 30;
            daysFromMarch21ToPaschalFullMoon = h - (h / 28) * (1 - (h / 28) * (29 / (h + 1)) * ((21 - golderNumber) / 11));
        }

        // Week day
        int weekDayForPaschalFullMoon = isEastern ?
                (year + year / leapYearFrequency + daysFromMarch21ToPaschalFullMoon) % 7 :
                (year + year / leapYearFrequency + daysFromMarch21ToPaschalFullMoon -
                        daysDifferenceBetweenJulianAndGregorianCalendar) % 7;

        // Number of days from March 21 to Sunday on or before PFM (-6 to 28 methods 1 & 3, to 56 for method 2)
        int daysFromMarch21ToEaster = daysFromMarch21ToPaschalFullMoon - weekDayForPaschalFullMoon +
                (isEastern ? daysDifferenceBetweenJulianAndGregorianCalendar : 0);

        GregorianCalendar date = new GregorianCalendar(year, 2, 21);
        date.add(GregorianCalendar.DAY_OF_YEAR, daysFromMarch21ToEaster);

        // p can be from -6 to 56 corresponding to dates 22 March to 23 May
        // (later dates apply to method 2, although 23 May never actually occurs)
//        int day = 1 + (daysFromMarch21ToEaster + 27 + (daysFromMarch21ToEaster + 6) / 40) % 31;
//        int month = 3 + (daysFromMarch21ToEaster + 26) / 30;

        return date;
    }

    public static GregorianCalendar getEasternEaster(int year) {
        return getEasterDate(year, true);
    }

    public static GregorianCalendar getWesternEaster(int year) {
        return getEasterDate(year, false);
    }
}
