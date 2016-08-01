package com.sharathp.myorktimes.fragments;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.sharathp.myorktimes.MYorkTimesApplication;
import com.sharathp.myorktimes.R;
import com.sharathp.myorktimes.databinding.FragmentFiltersBinding;
import com.sharathp.myorktimes.models.Filters;
import com.sharathp.myorktimes.repositories.LocalPreferencesRepository;
import com.sharathp.myorktimes.util.DateUtils;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import javax.inject.Inject;

public class FiltersFragment extends DialogFragment {
    private static final String TAG_DATE_PICKER = "datePicker";

    private FragmentFiltersBinding mBinding;
    private final Filters mFilters = new Filters();

    @Inject
    LocalPreferencesRepository mPreferencesRepository;

    public static FiltersFragment createInstance() {
        return new FiltersFragment();
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MYorkTimesApplication.from(getActivity()).getComponent().inject(this);
        populateFilters();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_filters, container, false);
        mBinding.setFilters(mFilters);
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
        mBinding.llArticleFromDateContainer.setOnClickListener(v -> showStartCalendar());

        mBinding.llArticleToDateContainer.setOnClickListener(v -> showEndCalendar());

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
        final boolean[] checkedOptions = getSelectedNewsDeskValues(newsDeskValues);

        builder.setMultiChoiceItems(newsDeskValues, checkedOptions, (dialog, which, isChecked) -> {
            final String item = newsDeskValues[which];
            updateNewsDeskSections(item, isChecked);
        });
        builder.show();
    }

    private boolean[] getSelectedNewsDeskValues(final String[] allNewsDeskValues) {
        final boolean[] selected = new boolean[allNewsDeskValues.length];
        final Set<String> preferredNewsDeskValues = mPreferencesRepository.getPreferredNewsDeskSections();
        for (int i = 0; i < allNewsDeskValues.length; i++) {
            final String currentNewsDeskValue = allNewsDeskValues[i];
            selected[i] = preferredNewsDeskValues.contains(currentNewsDeskValue);
        }
        return selected;
    }

    private void showStartCalendar() {
        final DatePickerFragment datePickerFragment = DatePickerFragment.createInstance(mFilters.getStartDate().getTime());
        datePickerFragment.show(getChildFragmentManager(), TAG_DATE_PICKER);
        datePickerFragment.setOnDateSetListener((view, year, monthOfYear, dayOfMonth) -> {
            final GregorianCalendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            saveStartDate(new Date(calendar.getTimeInMillis()));
        });
    }

    private void showEndCalendar() {
        Date date = mFilters.getEndDate();
        if (date == null) {
            date = DateUtils.getToday();
        }

        final DatePickerFragment datePickerFragment = DatePickerFragment.createInstance(date.getTime());
        datePickerFragment.show(getChildFragmentManager(), TAG_DATE_PICKER);
        datePickerFragment.setOnDateSetListener((view, year, monthOfYear, dayOfMonth) -> {
            final GregorianCalendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            saveEndDate(new Date(calendar.getTimeInMillis()));
        });
    }

    private void populateFilters() {
        mFilters.setNewsDeskSections(mPreferencesRepository.getPreferredNewsDeskSections());
        mFilters.setSort(mPreferencesRepository.getPreferredSortBy());
        mFilters.setStartDate(mPreferencesRepository.getPreferredStartDate());
        mFilters.setEndDate(mPreferencesRepository.getPreferredEndDate());
    }

    private void saveStartDate(final Date date) {
        mPreferencesRepository.setPreferredStartDate(date);
        mFilters.setStartDate(date);
        mBinding.setFilters(mFilters);
    }

    private void saveEndDate(final Date date) {
        mPreferencesRepository.setPreferredEndDate(date);
        mFilters.setEndDate(date);
        mBinding.setFilters(mFilters);
    }

    private void saveSortBy(final String sortBy) {
        mPreferencesRepository.setPreferredSortBy(sortBy);
        mFilters.setSort(sortBy);
        mBinding.setFilters(mFilters);
    }

    private void updateNewsDeskSections(final String newsDeskSection, final boolean selected) {
        if (selected) {
            mPreferencesRepository.savePreferredNewsDeskSection(newsDeskSection);
        } else {
            mPreferencesRepository.removePreferredNewsDeskSection(newsDeskSection);
        }
        mFilters.setNewsDeskSections(mPreferencesRepository.getPreferredNewsDeskSections());
        mBinding.setFilters(mFilters);
    }
}