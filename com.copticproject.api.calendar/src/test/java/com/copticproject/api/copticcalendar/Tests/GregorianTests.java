package com.copticproject.api.copticcalendar.Tests;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.stream.Stream;

import com.copticproject.api.copticcalendar.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

public class GregorianTests {

//    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");

    private static Stream<Arguments> getGregorianToCopticCases() throws LastMonthDayOutOfRangeException, MonthOutOfRangeException, YearOutOfRangeException, NonLeapYearDayOutOfRangeException, DayOutOfRangeException {
        return Stream.of(
                Arguments.of(new GregorianCalendar(2018, 0, 7),
                        new Date(29, 4, 1734))
        );
    }

    private static Stream<Arguments> getCopticToGregorianCases() throws LastMonthDayOutOfRangeException, MonthOutOfRangeException, YearOutOfRangeException, NonLeapYearDayOutOfRangeException, DayOutOfRangeException {
        return Stream.of(
                Arguments.of(new Date(29, 4, 1734),
                        new GregorianCalendar(2018, 0, 7))
        );
    }

    @DisplayName("Convert Gregorian date to Coptic")
    @ParameterizedTest(name = "run #{index} with [{arguments}]")
    @MethodSource(value = "getGregorianToCopticCases")
    public void TestConvertionToCoptic(GregorianCalendar date, Date expected) {
        assertEquals(expected, Gregorian.ToCoptic(date));
    }

    @DisplayName("Convert Coptic date to Gregorian")
    @ParameterizedTest(name = "run #{index} with [{arguments}]")
    @MethodSource(value = "getCopticToGregorianCases")
    public void TestConvertionFromCoptic(Date date, GregorianCalendar expected) {
        assertEquals(expected, Gregorian.FromCoptic(date));
    }
}
