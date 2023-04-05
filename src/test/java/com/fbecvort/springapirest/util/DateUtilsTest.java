package com.fbecvort.springapirest.util;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    void checkIfDateHappenedToday() {
        // Get the current date
        LocalDate localDate = LocalDate.now();
        // Create dates for yesterday, today and tomorrow
        Date yesterday = Date.from(localDate.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date today = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date tomorrow = Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Test each scenario
        assertFalse(DateUtils.checkIfDateHappenedToday(yesterday));
        assertTrue(DateUtils.checkIfDateHappenedToday(today));
        assertFalse(DateUtils.checkIfDateHappenedToday(tomorrow));
    }

    @Test
    void checkIfDateHappenedBetweenTwoDates() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date beforeRange = dateFormat.parse("2023-03-01");
        Date inRange = dateFormat.parse("2023-03-20");
        Date equalStart = dateFormat.parse("2023-03-15");
        Date equalEnd = dateFormat.parse("2023-03-31");
        Date afterRange = dateFormat.parse("2023-04-01");

        Date start = dateFormat.parse("2023-03-15");
        Date end = dateFormat.parse("2023-03-31");

        assertFalse(DateUtils.checkIfDateHappenedBetweenTwoDates(beforeRange, start, end));
        assertTrue(DateUtils.checkIfDateHappenedBetweenTwoDates(inRange, start, end));
        assertTrue(DateUtils.checkIfDateHappenedBetweenTwoDates(equalStart, start, end));
        assertTrue(DateUtils.checkIfDateHappenedBetweenTwoDates(equalEnd, start, end));
        assertFalse(DateUtils.checkIfDateHappenedBetweenTwoDates(afterRange, start, end));
    }

    @Test
    void createDateFromString() throws ParseException {
        // Define a sample date string
        String dateString = "2023-04-04T12:30:45.000Z";

        // Parse the date string into a date object
        Date expectedDate = Date.from(Instant.parse("2023-04-04T12:30:45.000Z"));
        Date actualDate = DateUtils.createDateFromString(dateString);

        // Assert that the actual date matches the expected date
        assertEquals(expectedDate, actualDate);
    }
}