package com.sharathp.myorktimes.util;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateUtils {

    // note, we are deliberately using java.sql.Date to ignore hour, min and sec
    public static Date getToday() {
        final Calendar calendar = Calendar.getInstance();
        final GregorianCalendar gregorianCalendar = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        return new Date(gregorianCalendar.getTimeInMillis());
    }

    public static String getRelativeTime(final Date date) {
        return android.text.format.DateUtils.getRelativeTimeSpanString(date.getTime(),
                System.currentTimeMillis(),
                android.text.format.DateUtils.DAY_IN_MILLIS,
                android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE).toString();
    }
}
