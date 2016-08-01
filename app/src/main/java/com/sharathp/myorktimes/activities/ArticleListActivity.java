package com.sharathp.myorktimes.activities;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.sharathp.myorktimes.R;
import com.sharathp.myorktimes.databinding.ActivityArticleListBinding;
import com.sharathp.myorktimes.fragments.ArticleListFragment;
import com.sharathp.myorktimes.fragments.BookmarkListFragment;
import com.sharathp.myorktimes.fragments.FiltersFragment;

public class ArticleListActivity extends AppCompatActivity {
    private static final String TAG = ArticleListActivity.class.getSimpleName();
    private static final int INDEX_HOME = 0;

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mDrawer;
    private FloatingActionButton mFiltersFab;

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityArticleListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_article_list);
        mToolbar = binding.toolbar;
        mDrawerLayout = binding.drawerLayout;
        mDrawer = binding.nvDrawer;
        mFiltersFab = binding.fabFilter;

        mFiltersFab.setOnClickListener(v -> showFiltersDialog());

        mDrawerToggle = setupDrawerToggle();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        setSupportActionBar(mToolbar);
        setupDrawerContent();

        showHome();
    }

    private void showHome() {
        // simulate clicking which shows home
        selectDrawerItem(mDrawer.getMenu().getItem(INDEX_HOME));
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent() {
        mDrawer.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }

    private void showFiltersDialog() {
        final FragmentManager fm = getSupportFragmentManager();
        final FiltersFragment editNameDialogFragment = FiltersFragment.createInstance();
        editNameDialogFragment.show(fm, "filters_fragment");
    }

    public void selectDrawerItem(final MenuItem menuItem) {
        Fragment fragment;
        switch(menuItem.getItemId()) {
            case R.id.nav_home: {
                fragment = ArticleListFragment.createInstance();
                mFiltersFab.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.nav_bookmarks: {
                fragment = BookmarkListFragment.createInstance();
                mFiltersFab.setVisibility(View.GONE);
                break;
            }
            default:
                Log.w(TAG, "Unknown menu item: " + menuItem.getTitle());
                fragment = ArticleListFragment.createInstance();
        }

        // Insert the fragment by replacing any existing fragment
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_content, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }
}