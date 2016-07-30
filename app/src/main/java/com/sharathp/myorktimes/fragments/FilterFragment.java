package com.sharathp.myorktimes.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class FilterFragment  extends DialogFragment {
    public static final String ARG_SELECTED_DATE = FilterFragment.class.getSimpleName() + ":DATE";

    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private Long mSelectedDate;

    public static FilterFragment createInstance() {
        return new FilterFragment();
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // use today as minimum
//        final Date today = DateUtils.getToday();

        final Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.setTimeInMillis(mSelectedDate);
        final int selectedYear = selectedCalendar.get(Calendar.YEAR);
        final int selectedMonth = selectedCalendar.get(Calendar.MONTH);
        final int selectedDay = selectedCalendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), mOnDateSetListener, selectedYear, selectedMonth, selectedDay);
//        datePickerDialog.getDatePicker().setMinDate(today.getTime());
        datePickerDialog.getDatePicker().updateDate(selectedYear, selectedMonth, selectedDay);
        datePickerDialog.setTitle(null);

        return datePickerDialog;
    }
}