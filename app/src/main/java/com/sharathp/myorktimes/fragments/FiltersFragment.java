package com.sharathp.myorktimes.fragments;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;

import com.sharathp.myorktimes.R;
import com.sharathp.myorktimes.databinding.FragmentFiltersBinding;
import com.sharathp.myorktimes.util.DateUtils;

import java.sql.Date;
import java.util.GregorianCalendar;

public class FiltersFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private static final String TAG_DATE_PICKER = "datePicker";

    private FragmentFiltersBinding mBinding;

    public static FiltersFragment createInstance() {
        return new FiltersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_filters, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initCalendar();
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        final ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    private void initCalendar() {
        final TextInputEditText minimumDateEditText = mBinding.tieArticleFromDate;
        minimumDateEditText.setInputType(InputType.TYPE_NULL);
        // this is required once the focus is obtained
        minimumDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });

        // this is required the first time focus is obtained
        minimumDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    showCalendar();
                }
            }
        });
    }

    private void showCalendar() {
        final DatePickerFragment datePickerFragment = DatePickerFragment.createInstance(DateUtils.getToday().getTime());
        datePickerFragment.show(getChildFragmentManager(), TAG_DATE_PICKER);
        datePickerFragment.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(final DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
        final GregorianCalendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        saveDate(new Date(calendar.getTimeInMillis()));
    }

    public void saveDate(final Date date) {
        // TODO - save to shared preferences
    }
}