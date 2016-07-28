package com.sharathp.myorktimes.di;

import com.sharathp.myorktimes.activities.ArticleListActivity;
import com.sharathp.myorktimes.di.modules.ApplicationModule;
import com.sharathp.myorktimes.di.modules.RestModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, RestModule.class})
public interface ApplicationComponent {

    void inject(ArticleListActivity articleListActivity);
}
