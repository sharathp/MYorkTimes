package com.sharathp.myorktimes.di.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.sharathp.myorktimes.MYorkTimesApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final MYorkTimesApplication mApplication;

    public ApplicationModule(final MYorkTimesApplication application) {
        mApplication = application;
    }

    @Singleton
    @NonNull
    @Provides
    Context provideApplicationContext() {
        return mApplication;
    }

    @Singleton
    @NonNull
    @Provides
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mApplication);
    }
}
