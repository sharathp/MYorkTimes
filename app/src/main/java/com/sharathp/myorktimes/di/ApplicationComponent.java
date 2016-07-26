package com.sharathp.myorktimes.di;

import com.sharathp.myorktimes.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

}
