package com.copticproject.api.copticcalendar;

public class NonLeapYearDayOutOfRangeException extends Throwable {
    @Override
    public String getMessage() {
        return "Day must be between 1 and 5 for the last month of a non-leap year";
    }
}
