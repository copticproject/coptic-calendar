package com.copticproject.copticcalendar.Tests;

import com.copticproject.copticcalendar.Date;
import com.copticproject.copticcalendar.Gregorian;
import org.junit.Test;
import static org.junit.Assert.*;

public class GregorianTests {
    @Test
    public void TestConvertionToCoptic(java.util.Date date, Date expected) {
        assertEquals(expected, Gregorian.ToCoptic(date));
    }

    @Test
    public void TestConvertionFromCoptic(Date date, java.util.Date expected) {
        assertEquals(expected, Gregorian.FromCoptic(date));
    }
}
