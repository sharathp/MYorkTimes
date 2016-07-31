package com.sharathp.myorktimes.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class RepositoryUtil {
    private static final String DATE_FORMAT_REPOSITORY = "yyyyMMdd";
    private static final String NEWS_DESK_PREFIX = "news_desk";

    public static String getFormattedStartDate(final Date startDate) {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_REPOSITORY);
        return sdf.format(startDate);
    }

    public static String getFilteredQuery(final Set<String> newsDeskSections) {
        if (newsDeskSections == null || newsDeskSections.isEmpty()) {
            return null;
        }

        // e.g., if newsDeskSections = <Education, Health>, the formatted query should
        // be news_desk:("Education"%20"Health")
        final StringBuilder sb = new StringBuilder(NEWS_DESK_PREFIX);
        sb.append(":(\"");
        sb.append(TextUtils.join("\" \"", newsDeskSections.toArray()));
        sb.append("\")");
        return sb.toString();
    }
}
