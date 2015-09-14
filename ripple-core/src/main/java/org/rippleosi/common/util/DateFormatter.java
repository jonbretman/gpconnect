package org.rippleosi.common.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 */
public final class DateFormatter {

    private DateFormatter() {
        // Prevent construction
    }

    public static Date toDate(String input) {

        if (input == null) {
            return null;
        }

        try {
            // TODO Optimisation - Re-order this list of dates to put the most common formats first
            return DateUtils.parseDate(input, "yyyy-MM-dd",
                                       "yyyy-MM-dd'T'HH:mm:ss",
                                       "yyyy-MM-dd'T'HH:mm:ss.SSS",
                                       "yyyy-MM-dd'T'HH:mm:ss.SSSX",
                                       "yyyy-MM-dd'T'HH:mm:ss.SSSXX",
                                       "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                                       "HH:mm:ss");
        } catch (ParseException ignore) {
            return null;
        }
    }

    public static Date toDateOnly(String input) {
        Date date = toDate(input);
        if (date != null) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            calendar.setTime(date);

            calendar = DateUtils.truncate(calendar, Calendar.DATE);

            return calendar.getTime();
        }

        return null;
    }

    public static Date toTimeOnly(String input) {
        Date date = toDate(input);
        if (date != null) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            calendar.setTime(date);

            calendar.set(Calendar.YEAR, 1970);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DATE, 1);

            return calendar.getTime();
        }

        return null;
    }

    public static String toString(Date input) {
        if (input == null) {
            return null;
        }

        return DateFormatUtils.format(input, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }

    public static String combineDateTime(Date date, Date time) {

        String dateAsString = toString(date);
        String timeAsString = toString(time);

        if (dateAsString == null || timeAsString == null) {
            return null;
        }

        return StringUtils.substringBefore(dateAsString, "T") + "T" + StringUtils.substringAfter(timeAsString, "T");
    }

    public static String stripOddDate(String input) {
        if (input != null) {
            if (input.contains("/")) {
                input = StringUtils.substringAfter(input, "/");
            }
            if (input.contains("/")) {
                input = StringUtils.substringBefore(input, "/");
            }
            if (input.contains("+")) {
                input = StringUtils.substringBefore(input, "+");
            }
        }

        return input;
    }
}