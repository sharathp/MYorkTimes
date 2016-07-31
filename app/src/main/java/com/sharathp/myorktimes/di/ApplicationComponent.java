package com.sharathp.myorktimes.di;

import com.sharathp.myorktimes.activities.ArticleDetailActivity;
import com.sharathp.myorktimes.activities.ArticleListActivity;
import com.sharathp.myorktimes.di.modules.ApplicationModule;
import com.sharathp.myorktimes.di.modules.RestModule;
import com.sharathp.myorktimes.fragments.BookmarkListFragment;
import com.sharathp.myorktimes.fragments.FiltersFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, RestModule.class})
public interface ApplicationComponent {

    void inject(ArticleListActivity articleListActivity);

    void inject(FiltersFragment filtersFragment);

    void inject(ArticleDetailActivity articleDetailActivity);

    void inject(BookmarkListFragment bookmarkListFragment);
}
