package com.sharathp.myorktimes.fragments;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

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
        initFilters();
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

    private void initFilters() {
        mBinding.llArticleFromDateContainer.setOnClickListener(v -> showCalendar());

        mBinding.llArticleSortContainer.setOnClickListener(v -> showSortOptions());

        mBinding.llArticleNewsDeskContainer.setOnClickListener(v -> showNewsDeskOptions());
    }

    private void showSortOptions() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] types = getActivity().getResources().getStringArray(R.array.filter_sort_order);
        builder.setItems(types, (dialog, which) -> {
            dialog.dismiss();
            saveSortBy(types[which]);
        });

        builder.show();
    }

    private void showNewsDeskOptions() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] newsDeskValues = getActivity().getResources().getStringArray(R.array.filter_news_desk);
        final boolean[] checkedOptions = new boolean[newsDeskValues.length];
        builder.setMultiChoiceItems(newsDeskValues, checkedOptions, (dialog, which, isChecked) -> {
            Toast.makeText(getActivity(), newsDeskValues[1] + ": " + isChecked, Toast.LENGTH_SHORT).show();
        });
        builder.show();
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
        mBinding.tvFilterStartDate.setText(date.toString());
        // TODO - save to shared preferences
    }

    public void saveSortBy(final String sortBy) {
        mBinding.tvFilterSort.setText(sortBy);
        // TODO - save to shared preferences
    }

    public void saveNewsDeskValues(final String[] selectedOptions) {
        // TODO - save to shared preferences
    }
}