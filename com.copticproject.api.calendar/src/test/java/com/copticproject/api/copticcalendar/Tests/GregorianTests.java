package com.copticproject.api.copticcalendar.Tests;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import com.copticproject.api.copticcalendar.*;
import com.copticproject.api.copticcalendar.Date;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

public class GregorianTests {
    private static ArrayList<Map.Entry<Date, GregorianCalendar>> data;

    private static ArrayList<Map.Entry<Date, GregorianCalendar>> getData() throws LastMonthDayOutOfRangeException, MonthOutOfRangeException, YearOutOfRangeException, NonLeapYearDayOutOfRangeException, DayOutOfRangeException, IOException {
        if (data == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(GregorianTests.class.getClassLoader().getResource("gregorian-conversion-cases.json").getFile());
            Iterator<JsonNode> items = objectMapper.readTree(file).elements();

            data = new ArrayList<Map.Entry<Date, GregorianCalendar>>();
            while (items.hasNext()) {
                JsonNode item = items.next();
                JsonNode coptic = item.path("coptic");
                JsonNode gregorian = item.path("gregorian");

                data.add(new AbstractMap.SimpleEntry(
                        new Date(coptic.path("day").asInt(), coptic.path("month").asInt(), coptic.path("year").asInt()),
                        new GregorianCalendar(gregorian.path("year").asInt(), gregorian.path("month").asInt() - 1, gregorian.path("day").asInt())
                ));
            }
        }

        return data;
    }

    private static Stream<Arguments> getGregorianToCopticCases() throws LastMonthDayOutOfRangeException, MonthOutOfRangeException, YearOutOfRangeException, NonLeapYearDayOutOfRangeException, DayOutOfRangeException, IOException {
        ArrayList<Arguments> args = new ArrayList<>();
        getData().forEach((e)->args.add(Arguments.of(e.getValue(), e.getKey())));
        return Stream.of(args.toArray(new Arguments[0]));
    }

    private static Stream<Arguments> getCopticToGregorianCases() throws LastMonthDayOutOfRangeException, MonthOutOfRangeException, YearOutOfRangeException, NonLeapYearDayOutOfRangeException, DayOutOfRangeException, IOException {
        ArrayList<Arguments> args = new ArrayList<>();
        getData().forEach((e)->args.add(Arguments.of(e.getKey(), e.getValue())));
        return Stream.of(args.toArray(new Arguments[0]));
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
