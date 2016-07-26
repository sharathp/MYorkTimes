package com.sharathp.myorktimes;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.sharathp.myorktimes.di.ApplicationComponent;
import com.sharathp.myorktimes.di.DaggerApplicationComponent;
import com.sharathp.myorktimes.di.modules.ApplicationModule;

public class MYorkTimesApplication extends Application {
    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initDependencyInjection();
    }

    private void initDependencyInjection() {
        mComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {
        return mComponent;
    }

    public static MYorkTimesApplication from(@NonNull final Context context) {
        return (MYorkTimesApplication) context.getApplicationContext();
    }
}