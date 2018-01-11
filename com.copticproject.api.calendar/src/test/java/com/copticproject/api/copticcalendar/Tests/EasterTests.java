package com.copticproject.api.copticcalendar.Tests;

import com.copticproject.api.copticcalendar.*;
import com.copticproject.api.copticcalendar.Date;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EasterTests {
    private static Arguments[] easternEasterDate;
    private static Arguments[] westernEasterDate;

    private static void readData() throws LastMonthDayOutOfRangeException, MonthOutOfRangeException, YearOutOfRangeException, NonLeapYearDayOutOfRangeException, DayOutOfRangeException, IOException {
        if (easternEasterDate == null || westernEasterDate == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(GregorianTests.class.getClassLoader().getResource("easter-dates-test-cases.json").getFile());
            Iterator<JsonNode> items = objectMapper.readTree(file).elements();

            ArrayList<Arguments> eastern = new ArrayList<>();
            ArrayList<Arguments> western = new ArrayList<>();

            while (items.hasNext()) {
                JsonNode item = items.next();
                eastern.add(Arguments.of(new GregorianCalendar(
                        item.path("year").asInt(), item.path("eastern-month").asInt() - 1, item.path("eastern-day").asInt())));
                western.add(Arguments.of(new GregorianCalendar(
                        item.path("year").asInt(), item.path("western-month").asInt() - 1, item.path("western-day").asInt())));
            }

            easternEasterDate = eastern.toArray(new Arguments[0]);
            westernEasterDate = western.toArray(new Arguments[0]);
        }
    }

    private static Stream<Arguments> getWesternEasterDateCases() throws LastMonthDayOutOfRangeException, MonthOutOfRangeException, YearOutOfRangeException, NonLeapYearDayOutOfRangeException, DayOutOfRangeException, IOException {
        readData();
        return Stream.of(westernEasterDate);
    }

    private static Stream<Arguments> getEasternEasterDateCases() throws LastMonthDayOutOfRangeException, MonthOutOfRangeException, YearOutOfRangeException, NonLeapYearDayOutOfRangeException, DayOutOfRangeException, IOException {
        readData();
        return Stream.of(easternEasterDate);
    }

    @DisplayName("Calculate western Easter date")
    @ParameterizedTest(name = "run #{index} with [{arguments}]")
    @MethodSource(value = "getWesternEasterDateCases")
    public void TestWesternEasterDateCalculation(GregorianCalendar easterDate) {
        assertEquals(easterDate, Easter.getWesternEaster(easterDate.get(GregorianCalendar.YEAR)));
    }

    @DisplayName("Calculate eastern Easter date")
    @ParameterizedTest(name = "run #{index} with [{arguments}]")
    @MethodSource(value = "getEasternEasterDateCases")
    public void TestEasternEasterDateCalculation(GregorianCalendar easterDate) {
        assertEquals(easterDate, Easter.getEasternEaster(easterDate.get(GregorianCalendar.YEAR)));
    }
}
