package com.copticproject.api.copticcalendar;

public class Date {
    public static final int MinMonth = 1;
    public static final int MaxMonth = 13;
    public static final int MinDay = 1;
    public static final int MaxDayInDefaultMonths = 30;
    public static final int MaxDayInKogiMonthDefaultYear = 5;
    public static final int MaxDayInKogiMonthLeapYear = 6;
    public static final int MinYear = 1;
    public static final int LeapYearFrequency = 4;

    public static final int DaysInNonLeapYear = (MaxMonth - 1) * MaxDayInDefaultMonths + MaxDayInKogiMonthDefaultYear;
    public static final int DaysInLeapYear = (MaxMonth - 1) * MaxDayInDefaultMonths + MaxDayInKogiMonthLeapYear;

    private static final int DaysInFourConsecutiveYears = (LeapYearFrequency - 1) * DaysInNonLeapYear + DaysInLeapYear;

    private int day;
    private int month;
    private int year;

    public Date(int day, int month, int year) throws YearOutOfRangeException, MonthOutOfRangeException, DayOutOfRangeException, NonLeapYearDayOutOfRangeException, LastMonthDayOutOfRangeException {
        set(day, month, year);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!Date.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final Date other = (Date) obj;

        return this.day == other.day &&
                this.month == other.month &&
                this.year == other.year;
    }

    @Override
    public int hashCode() {
        return (this.day - 1) +
                (this.month - 1) * MaxDayInDefaultMonths +
                (this.year - 1) * MaxDayInDefaultMonths * MaxMonth;
    }

    @Override
    public String toString() {
        return String.format("%d/%d/%d", this.day, this.month, this.year);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) throws DayOutOfRangeException, NonLeapYearDayOutOfRangeException, LastMonthDayOutOfRangeException {
        ValidateDay(day);
        try {
            ValidateDayOfYear(day, this.month, this.year);
        } catch (YearOutOfRangeException e) {
            throw new IllegalStateException();
        }
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) throws MonthOutOfRangeException, NonLeapYearDayOutOfRangeException, LastMonthDayOutOfRangeException {
        ValidateMonth(month);
        try {
            ValidateDayOfYear(this.day, month, this.year);
        } catch (YearOutOfRangeException e) {
            throw new IllegalStateException();
        }
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) throws YearOutOfRangeException, NonLeapYearDayOutOfRangeException, LastMonthDayOutOfRangeException {
        ValidateYear(year);
        ValidateDayOfYear(this.day, this.month, year);
        this.year = year;
    }

    public void set(int day, int month, int year) throws DayOutOfRangeException, MonthOutOfRangeException, YearOutOfRangeException, NonLeapYearDayOutOfRangeException, LastMonthDayOutOfRangeException {
        ValidateDay(day);
        ValidateMonth(month);
        ValidateYear(year);
        ValidateDayOfYear(day, month, year);
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDayInYear() {
        return (this.month - 1) * MaxDayInDefaultMonths + this.day;
    }

    public static int getDaysOfYear(int year) throws YearOutOfRangeException {
        return IsLeapYear(year) ? DaysInLeapYear : DaysInNonLeapYear;
    }

    // Get days since 1 Toot year 1. The result is 1-based which means the for the day 1 Toot year 1 the return value will be 1
    public int getAbsoluteDays() {
        int days = ((this.year - 1) / LeapYearFrequency) * DaysInFourConsecutiveYears + this.getDayInYear();

        for (int year = this.year - 1; year >= this.year - ((this.year - 1) % LeapYearFrequency); year--)
            try {
                days += getDaysOfYear(year);
            } catch (YearOutOfRangeException e) {
                throw new IllegalStateException();
            }

        return days;
    }

    public static Date createFromAbsoluteDays(int absoluteDays) {
        absoluteDays--;
        int year = 1 + (absoluteDays / DaysInFourConsecutiveYears) * LeapYearFrequency;
        absoluteDays %= DaysInFourConsecutiveYears;

        try {
            for (int currentYearDays = getDaysOfYear(year); absoluteDays >= currentYearDays; currentYearDays = getDaysOfYear(year)) {
                year++;
                absoluteDays -= currentYearDays;
            }

            return new Date(absoluteDays % MaxDayInDefaultMonths + 1, absoluteDays / MaxDayInDefaultMonths + 1, year);
        } catch (YearOutOfRangeException e) {
            throw new IllegalStateException();
        } catch (MonthOutOfRangeException e) {
            throw new IllegalStateException();
        } catch (DayOutOfRangeException e) {
            throw new IllegalStateException();
        } catch (NonLeapYearDayOutOfRangeException e) {
            throw new IllegalStateException();
        } catch (LastMonthDayOutOfRangeException e) {
            throw new IllegalStateException();
        }
    }

    public boolean isLeapYear() {
        try {
            return IsLeapYear(this.year);
        } catch (YearOutOfRangeException e) {
            throw new IllegalStateException();
        }
    }

    public static boolean IsLeapYear(int year) throws YearOutOfRangeException {
        ValidateYear(year);

        return (year + 1) % LeapYearFrequency == 0;
    }

    private static void ValidateDay(int day) throws DayOutOfRangeException {
        if (day < MinDay || day > MaxDayInDefaultMonths)
            throw new DayOutOfRangeException();
    }

    private static void ValidateMonth(int month) throws MonthOutOfRangeException {
        if (month < MinMonth || month > MaxMonth)
            throw new MonthOutOfRangeException();
    }

    private static void ValidateYear(int year) throws YearOutOfRangeException {
        if (year < MinYear)
            throw new YearOutOfRangeException();
    }

    private static void ValidateDayOfYear(int day, int month, int year) throws LastMonthDayOutOfRangeException, NonLeapYearDayOutOfRangeException, YearOutOfRangeException {
        if (month == MaxMonth) {
            if (day > MaxDayInKogiMonthLeapYear)
                throw new LastMonthDayOutOfRangeException();
            if (day == MaxDayInKogiMonthLeapYear && !IsLeapYear(year))
                throw new NonLeapYearDayOutOfRangeException();
        }
    }
}