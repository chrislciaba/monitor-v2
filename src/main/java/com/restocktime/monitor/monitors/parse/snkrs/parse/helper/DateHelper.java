package com.restocktime.monitor.monitors.parse.snkrs.parse.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

public class DateHelper {
    public static String formatDate(String dateStr){
        try {
            if(dateStr != null && dateStr.charAt(dateStr.length() - 1) != 'Z')
                dateStr = dateStr.substring(0, dateStr.length() - 1) + 'Z';
            long epochTimeSnkrs = Instant.parse(dateStr).getEpochSecond();

            Date date = new Date(epochTimeSnkrs * 1000);
            DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("America/New_York"));
            return format.format(date) + " EST";
        } catch(Exception e){
            return null;
        }
    }

    public static Long getEpochTimeSecs(String dateStr){
        try {
            return Instant.parse(dateStr).getEpochSecond();
        } catch (Exception e){
            return null;
        }
    }
}
