package com.sharathp.myorktimes.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.sharathp.myorktimes.R;
import com.sharathp.myorktimes.util.DateUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LocalPreferencesRepository {
    private static final String KEY_START_DATE = "MYORK_TIMES_PREF_START_DATE";
    private static final String KEY_END_DATE = "MYORK_TIMES_PREF_END_DATE";
    private static final String KEY_SORT = "MYORK_TIMES_PREF_SORT";
    private static final String KEY_NEWS_DESK_SECTIONS = "MYORK_NEWS_DESK_SECTIONS";

    private static final long INVALID_END_DATE = -1;

    private final SharedPreferences mSharedPreferences;
    private final Context mContext;

    @Inject
    public LocalPreferencesRepository(final SharedPreferences sharedPreferences, final Context context) {
        this.mSharedPreferences = sharedPreferences;
        mContext = context;
    }

    public Date getPreferredStartDate() {
        final Date today = DateUtils.getToday();
        final long startDate = mSharedPreferences.getLong(KEY_START_DATE, today.getTime());
        return new Date(startDate);
    }

    public void setPreferredStartDate(final Date startDate) {
        mSharedPreferences
                .edit()
                .putLong(KEY_START_DATE, startDate.getTime())
                .commit();
    }

    public Date getPreferredEndDate() {
        final long endDate = mSharedPreferences.getLong(KEY_END_DATE, INVALID_END_DATE);
        if (INVALID_END_DATE == endDate) {
            return null;
        }
        return new Date(endDate);
    }

    public void setPreferredEndDate(final Date endDate) {
        mSharedPreferences
                .edit()
                .putLong(KEY_END_DATE, endDate.getTime())
                .commit();
    }

    public Set<String> getPreferredNewsDeskSections() {
        final Set<String> newsDeskSections = mSharedPreferences.getStringSet(KEY_NEWS_DESK_SECTIONS, new HashSet<>());
        // return a copy as modifying it will cause unexpected issues -
        // https://developer.android.com/reference/android/content/SharedPreferences.html#getStringSet(java.lang.String, java.util.Set<java.lang.String>)
        return new HashSet<>(newsDeskSections);
    }

    public void savePreferredNewsDeskSection(final String section) {
        Set<String> existingDeskSections = getPreferredNewsDeskSections();
        existingDeskSections.add(section);
        setPreferredNewsDeskSections(existingDeskSections);
    }

    public void removePreferredNewsDeskSection(final String section) {
        Set<String> existingDeskSections = getPreferredNewsDeskSections();
        existingDeskSections.remove(section);
        setPreferredNewsDeskSections(existingDeskSections);
    }

    public void setPreferredNewsDeskSections(final Set<String> sections) {
        mSharedPreferences
                .edit()
                .putStringSet(KEY_NEWS_DESK_SECTIONS, sections)
                .commit();
    }

    public String getPreferredSortBy() {
        final String[] sortArray = mContext.getResources().getStringArray(R.array.filter_sort_order);

        // default to the first key
        final String sortOrder = mSharedPreferences.getString(KEY_SORT, sortArray[0]);
        return sortOrder;
    }

    public void setPreferredSortBy(final String sortOrder) {
        mSharedPreferences
                .edit()
                .putString(KEY_SORT, sortOrder)
                .commit();
    }
}