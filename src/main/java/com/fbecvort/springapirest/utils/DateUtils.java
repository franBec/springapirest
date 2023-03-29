package com.fbecvort.springapirest.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private DateUtils(){}

    public static boolean checkIfDateHappenedToday(Date date){
        // create a calendar instance and set it to the beginning of today
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();

        return date.compareTo(today) == 0;
    }

    public static boolean checkIfDateHappenedBetweenTwoDates(Date date, Date start, Date end) {
        return date.after(start) && date.before(end);
    }

    public static Date setDateAtEndOfTheDay(Date date){
        LocalDateTime localPeriodoEnd = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDate localDate = localPeriodoEnd.toLocalDate();
        LocalTime localTime = LocalTime.of(23, 59, 59); // Set the time to 23:59:59
        LocalDateTime endOfDay = LocalDateTime.of(localDate, localTime);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }
}
