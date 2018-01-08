package com.copticproject.api.copticcalendar;

public class LastMonthDayOutOfRangeException extends Throwable {
    @Override
    public String getMessage() {
        return "Day must be between 1 and 6 for the last month";
    }
}
