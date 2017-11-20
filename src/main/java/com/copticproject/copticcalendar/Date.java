package com.copticproject.copticcalendar;

import org.omg.CORBA.portable.ApplicationException;

import javax.management.InvalidApplicationException;

public class Date {
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
                (this.month - 1) * 30 +
                (this.year - 1) * 30 * 13;
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

    public boolean isLeapYear() {
        try {
            return IsLeapYear(this.year);
        } catch (YearOutOfRangeException e) {
            throw new IllegalStateException();
        }
    }

    public static boolean IsLeapYear(int year) throws YearOutOfRangeException {
        ValidateYear(year);


    }

    private static void ValidateDay(int day) throws DayOutOfRangeException {
        if (day < 1 || day > 30)
            throw new DayOutOfRangeException();
    }

    private static void ValidateMonth(int month) throws MonthOutOfRangeException {
        if (month < 1 || month > 13)
            throw new MonthOutOfRangeException();
    }

    private static void ValidateYear(int year) throws YearOutOfRangeException {
        if (year < 1)
            throw new YearOutOfRangeException();
    }

    private static void ValidateDayOfYear(int day, int month, int year) throws LastMonthDayOutOfRangeException, NonLeapYearDayOutOfRangeException, YearOutOfRangeException {
        if (month == 13) {
            if (day > 6)
                throw new LastMonthDayOutOfRangeException();
            if (day == 6 && !IsLeapYear(year))
                throw new NonLeapYearDayOutOfRangeException();
        }
    }
}
