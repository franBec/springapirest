package com.fbecvort.springapirest.utils;

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
}
