package com.copticproject.copticcalendar;

public class YearOutOfRangeException extends Throwable {
    @Override
    public String getMessage() {
        return "Year cannot be less than 1";
    }
}
