package com.fbecvort.springapirest.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    private DateUtils(){}

    public static boolean checkIfDateHappenedToday(Date date){
        // create a calendar instance and set it to the beginning of today
        Calendar calToday = Calendar.getInstance();
        calToday.set(Calendar.HOUR_OF_DAY, 0);
        calToday.set(Calendar.MINUTE, 0);
        calToday.set(Calendar.SECOND, 0);
        calToday.set(Calendar.MILLISECOND, 0);
        Date today = calToday.getTime();

        // create a calendar instance for the date to compare and set it to the beginning of that day
        Calendar calDate = Calendar.getInstance();
        calDate.setTime(date);
        calDate.set(Calendar.HOUR_OF_DAY, 0);
        calDate.set(Calendar.MINUTE, 0);
        calDate.set(Calendar.SECOND, 0);
        calDate.set(Calendar.MILLISECOND, 0);
        Date compareDate = calDate.getTime();

        return compareDate.compareTo(today) == 0;
    }

    public static boolean checkIfDateHappenedBetweenTwoDates(Date date, Date start, Date end) {
        return date.after(start) && date.before(end);
    }

    public static Date createDateFromString(String dateString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.parse(dateString);
    }

}
