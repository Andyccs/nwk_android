package com.nwk.locopromo.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 * Created by andyccs on 31/12/14.
 */
public class DateTimeUtil {

    public static String toDayMonthYearHourMinute(String dateTime){
        DateTime redeemByDateTime = new DateTime(dateTime);
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendDayOfMonth(1)
                .appendLiteral(" ")
                .appendMonthOfYearText()
                .appendLiteral(" ")
                .appendYear(4,4)
                .appendLiteral(", ")
                .appendHourOfDay(2)
                .appendLiteral(":")
                .appendMinuteOfHour(2)
                .toFormatter();
        String redeemBy = formatter.print(redeemByDateTime);
        return redeemBy;
    }
}
