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

    public static Date createDateFromString(String dateString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = formatter.parse(dateString);
        return date;
    }

}
