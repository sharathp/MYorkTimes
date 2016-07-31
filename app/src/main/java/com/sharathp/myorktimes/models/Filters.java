package com.sharathp.myorktimes.models;

import java.util.Date;
import java.util.Set;

public class Filters {
    private String mSort;
    private Set<String> mNewsDeskSections;
    private Date mStartDate;
    private Date mEndDate;

    public String getSort() {
        return mSort;
    }

    public void setSort(String sort) {
        mSort = sort;
    }

    public Set<String> getNewsDeskSections() {
        return mNewsDeskSections;
    }

    public void setNewsDeskSections(final Set<String> newsDeskSections) {
        mNewsDeskSections = newsDeskSections;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(final Date startDate) {
        mStartDate = startDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(final Date endDate) {
        mEndDate = endDate;
    }
}
