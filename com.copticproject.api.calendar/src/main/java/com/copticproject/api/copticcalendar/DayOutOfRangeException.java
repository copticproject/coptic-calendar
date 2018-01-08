package com.copticproject.api.copticcalendar;

public class DayOutOfRangeException extends Throwable {
    @Override
    public String getMessage() {
        return "Day must be between 1 and 30";
    }
}
