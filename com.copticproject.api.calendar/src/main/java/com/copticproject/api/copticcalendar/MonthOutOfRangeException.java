package com.copticproject.api.copticcalendar;

public class MonthOutOfRangeException extends Throwable {
    @Override
    public String getMessage() {
        return "Month must be between 1 and 13";
    }
}
