package com.sharathp.myorktimes.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.sharathp.myorktimes.R;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class LocalPreferencesRepository {
    private static final String KEY_START_DATE = "MYORK_TIMES_PREF_START_DATE";
    private static final String KEY_SORT = "MYORK_TIMES_PREF_SORT";
    private static final String KEY_NEWS_DESK_SECTIONS = "MYORK_NEWS_DESK_SECTIONS";

    private final SharedPreferences mSharedPreferences;
    private final Context mContext;

    @Inject
    public LocalPreferencesRepository(final SharedPreferences sharedPreferences, final Context context) {
        this.mSharedPreferences = sharedPreferences;
        mContext = context;
    }

    public Date getPreferredStartDate() {
        return null;
    }

    public List<String> getPreferredNewsDeskSections() {
        return null;
    }

    public String getPreferredSortOrder() {
        final String[] sortArray = mContext.getResources().getStringArray(R.array.filter_sort_order);

        // default to the first key
        final String sortOrder = mSharedPreferences.getString(KEY_SORT, sortArray[0]);
        return sortOrder;
    }

    public void setPreferredSortOrder(final String sortOrder) {
        mSharedPreferences
                .edit()
                .putString(KEY_SORT, sortOrder)
                .commit();
    }
}