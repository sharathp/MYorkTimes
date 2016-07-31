package com.sharathp.myorktimes.util;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.TextView;

import com.sharathp.myorktimes.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class BindingUtils {
    private static final String DATE_FORMAT_START_DATE = "MMMM d, yyyy";
    private static final String DATE_FORMAT_END_DATE = DATE_FORMAT_START_DATE;

    @BindingAdapter({"bind:filterStartDate"})
    public static void formatFilterStartDate(final TextView tv, final Date date) {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_START_DATE);
        final String formattedDate = sdf.format(date);
        tv.setText(formattedDate);
    }

    @BindingAdapter({"bind:filterEndDate"})
    public static void formatFilterEndDate(final TextView tv, final Date date) {
        if (date == null) {
            setDefaultValue(tv);
            return;
        }

        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_END_DATE);
        final String formattedDate = sdf.format(date);
        tv.setText(formattedDate);
    }

    @BindingAdapter({"bind:newsDeskSections"})
    public static void formatNewsDeskSections(final TextView tv, final Set<String> newsDeskSections) {
        if (newsDeskSections == null || newsDeskSections.isEmpty()) {
            setDefaultValue(tv);
            return;
        }

        final String formattedNewsDeskSections = TextUtils.join(", ", newsDeskSections.toArray());
        tv.setText(formattedNewsDeskSections);
    }

    private static void setDefaultValue(final TextView tv) {
        final CharSequence defaultValue = tv.getContext().getResources().getString(R.string.filter_news_desk_none_selected);
        tv.setText(defaultValue);
    }
}