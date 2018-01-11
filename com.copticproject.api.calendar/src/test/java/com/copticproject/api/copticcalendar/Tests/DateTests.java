package com.copticproject.api.copticcalendar.Tests;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.copticproject.api.copticcalendar.*;
import com.copticproject.api.copticcalendar.Date;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DateTests {
    private static Arguments[] validationCases;
    private static Arguments[] leapYearCases;
    private static Arguments[] absoluteDaysCases;

    public static void readFile() throws LastMonthDayOutOfRangeException, MonthOutOfRangeException, YearOutOfRangeException, NonLeapYearDayOutOfRangeException, DayOutOfRangeException, IOException {
        if (validationCases == null || leapYearCases == null || absoluteDaysCases == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(GregorianTests.class.getClassLoader().getResource("coptic-date-class-test-cases.json").getFile());

            JsonNode root = objectMapper.readTree(file);

            ArrayList<Arguments> results = new ArrayList<>();
            Iterator<JsonNode> validationElements = root.path("validation-cases").elements();
            while (validationElements.hasNext()) {
                JsonNode item = validationElements.next();
                JsonNode date = item.path("date");
                results.add(Arguments.of(date.path("day").asInt(), date.path("month").asInt(), date.path("year").asInt(),
                        item.path("valid").asBoolean()));
            }
            validationCases = results.toArray(new Arguments[0]);

            results = new ArrayList<>();
            Iterator<JsonNode> leapYearElements = root.path("leap-year-cases").elements();
            while (leapYearElements.hasNext()) {
                JsonNode item = leapYearElements.next();
                results.add(Arguments.of(item.path("year").asInt(), item.path("is-leap").asBoolean()));
            }
            leapYearCases = results.toArray(new Arguments[0]);

            results = new ArrayList<>();
            Iterator<JsonNode> absoluteDaysElements = root.path("absolute-days-cases").elements();
            while (absoluteDaysElements.hasNext()) {
                JsonNode item = absoluteDaysElements.next();
                JsonNode date = item.path("date");
                results.add(Arguments.of(new Date(date.path("day").asInt(), date.path("month").asInt(), date.path("year").asInt()),
                        item.path("absolute-days").asInt()));
            }
            absoluteDaysCases = results.toArray(new Arguments[0]);
        }
    }

    private static Stream<Arguments> getValidationCases() throws LastMonthDayOutOfRangeException, MonthOutOfRangeException, YearOutOfRangeException, NonLeapYearDayOutOfRangeException, DayOutOfRangeException, IOException {
        readFile();
        return Stream.of(validationCases);
    }

    private static Stream<Arguments> getLeapYearCases() throws LastMonthDayOutOfRangeException, MonthOutOfRangeException, YearOutOfRangeException, NonLeapYearDayOutOfRangeException, DayOutOfRangeException, IOException {
        readFile();
        return Stream.of(leapYearCases);
    }

    private static Stream<Arguments> getAbsoluteDaysCases() throws LastMonthDayOutOfRangeException, MonthOutOfRangeException, YearOutOfRangeException, NonLeapYearDayOutOfRangeException, DayOutOfRangeException, IOException {
        readFile();
        return Stream.of(absoluteDaysCases);
    }

    @DisplayName("Test day, month, and year value validation rules")
    @ParameterizedTest(name = "run #{index} with [{arguments}]")
    @MethodSource(value = "getValidationCases")
    public void TestValidation(int day, int month, int year, Boolean isExpectedToBeValid) throws LastMonthDayOutOfRangeException, MonthOutOfRangeException, YearOutOfRangeException, NonLeapYearDayOutOfRangeException, DayOutOfRangeException {
        Date date;
        if (isExpectedToBeValid)
            date = new Date(day, month, year);
        else
            assertThrows(Throwable.class, ()->new Date(day, month, year));
    }

    @DisplayName("Test is leap year calculation")
    @ParameterizedTest(name = "run #{index} with [{arguments}]")
    @MethodSource(value = "getLeapYearCases")
    public void TestIsLeapYear(int year, Boolean isExpectedToBeLeap) throws YearOutOfRangeException {
        assertEquals(isExpectedToBeLeap, Date.IsLeapYear(year));
    }

    @DisplayName("Calculate absolute days since 1 Thoot year 1")
    @ParameterizedTest(name = "run #{index} with [{arguments}]")
    @MethodSource(value = "getAbsoluteDaysCases")
    public void TestAbsoluteDays(Date date, int expectedAbsoluteDays) {
        assertEquals(expectedAbsoluteDays, date.getAbsoluteDays());
    }

    @DisplayName("Calculate date from absolute days")
    @ParameterizedTest(name = "run #{index} with [{arguments}]")
    @MethodSource(value = "getAbsoluteDaysCases")
    public void TestCreateFromAbsoluteDays(Date expectedDate, int absoluteDays) {
        assertEquals(expectedDate, Date.createFromAbsoluteDays(absoluteDays));
    }
}
