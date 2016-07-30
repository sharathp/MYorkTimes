package com.sharathp.myorktimes.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.sharathp.myorktimes.MYorkTimesApplication;
import com.sharathp.myorktimes.R;
import com.sharathp.myorktimes.databinding.ActivityArticleListBinding;
import com.sharathp.myorktimes.fragments.FiltersFragment;
import com.sharathp.myorktimes.models.Article;
import com.sharathp.myorktimes.models.ArticleResponse;
import com.sharathp.myorktimes.repositories.ArticleRepository;
import com.sharathp.myorktimes.views.ArticleListAdapter;
import com.sharathp.myorktimes.views.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleListActivity extends AppCompatActivity implements ArticleListAdapter.ArticleItemCallback {
    private static final String TAG = ArticleListActivity.class.getSimpleName();

    @Inject
    ArticleRepository mArticleRepository;

    private ArticleListAdapter mArticleListAdapter;
    private ActivityArticleListBinding mBinding;
    private Call<ArticleResponse> mCurrentCall;
    private String mCurrentQuery;

    private EndlessRecyclerViewScrollListener mEndlessRecyclerViewScrollListener;
    private StaggeredGridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MYorkTimesApplication.from(this).getComponent().inject(this);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_article_list);
        setSupportActionBar(mBinding.toolbarLayout.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mArticleListAdapter = new ArticleListAdapter(new ArrayList<>(), this);
        final RecyclerView moviesRecyclerView = mBinding.rvArticles;
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        moviesRecyclerView.setAdapter(mArticleListAdapter);
        moviesRecyclerView.setLayoutManager(mLayoutManager);
        mBinding.fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                showFiltersDialog();
            }
        });
    }

    private void showFiltersDialog() {
        final FragmentManager fm = getSupportFragmentManager();
        final FiltersFragment editNameDialogFragment = FiltersFragment.createInstance();
        editNameDialogFragment.show(fm, "filters_fragment");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCurrentCall != null) {
            mCurrentCall.cancel();
        }

        mCurrentCall = null;
    }

    @Override
    public void onArticleSelected(final Article article) {
        // no-op
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article_list, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                tryClearEndlessRecyclerViewScrollListener();
                mArticleListAdapter.setArticles(null);
                mArticleListAdapter.clearEndReached();
                showInitialLoader();

                mCurrentQuery = query;
                // get the first page of results
                retrieveResults(0);

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Customize searchview text and hint colors
        final int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        final EditText et = (EditText) searchView.findViewById(searchEditId);
        final int color = getResources().getColor(R.color.colorPrimaryDark);
        et.setTextColor(color);
        et.setHintTextColor(color);
        return true;
    }

    private void retrieveResults(final int page) {
        Log.d(TAG, "Retrieving results: " + page);

        if (mCurrentCall != null) {
            mCurrentCall.cancel();
        }

        if (TextUtils.isEmpty(mCurrentQuery)) {
            return;
        }

        mCurrentCall = mArticleRepository.getArticles(mCurrentQuery, "oldest", "20160112", page);
        mCurrentCall.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(final Call<ArticleResponse> call, final Response<ArticleResponse> response) {
                if (! response.isSuccessful()) {
                    showMessageIfInitialLoad("Error retrieving articles");
                    return;
                }

                final ArticleResponse articleResponse = response.body();
                if (articleResponse == null
                        || articleResponse.getResponse() == null
                        || articleResponse.getResponse().getDocs() == null
                        || articleResponse.getResponse().getDocs().isEmpty()) {
                    showMessageIfInitialLoad("No articles found for the search criteria");
                    return;
                }

                showArticlesRecyclerViewIfInitialLoad();
                setEndlessRecyclerViewScrollListener(articleResponse);
                mArticleListAdapter.addMovies(articleResponse.getResponse().getDocs());
                Log.d(TAG, "Results size: " + mArticleListAdapter.getItemCount());
            }

            @Override
            public void onFailure(final Call<ArticleResponse> call, final Throwable t) {
                showMessageIfInitialLoad("Error retrieving articles");
            }

            private void setEndlessRecyclerViewScrollListener(final ArticleResponse articleResponse) {
                final boolean hasMoreResults = articleResponse.hasMoreResults();

                if (! hasMoreResults) {
                    markNoMoreItemsToLoad();
                    return;
                }

                if (mEndlessRecyclerViewScrollListener == null) {
                    // looks like first time retrieving results, set the listener
                    ArticleListActivity.this.setEndlessRecyclerViewScrollListener();
                }
            }
        });
    }

    private boolean isInitialLoad() {
        return mArticleListAdapter.getItemCount() == 0;
    }

    private void showMessageIfInitialLoad(final String message) {
        if (! isInitialLoad()) {
            // items already exist, so, message shouldn't be displayed, instead mark no items to load
            markNoMoreItemsToLoad();
            return;
        }

        mBinding.rvArticles.setVisibility(View.GONE);

        mBinding.flMessageContainer.setVisibility(View.VISIBLE);
        mBinding.tvArticlesMessage.setVisibility(View.VISIBLE);
        mBinding.pbAllArticlesLoadingBar.setVisibility(View.GONE);

        mBinding.tvArticlesMessage.setText(message);
    }

    private void showInitialLoader() {
        mBinding.rvArticles.setVisibility(View.GONE);

        mBinding.flMessageContainer.setVisibility(View.VISIBLE);
        mBinding.pbAllArticlesLoadingBar.setVisibility(View.VISIBLE);
        mBinding.tvArticlesMessage.setVisibility(View.GONE);
    }

    private void showArticlesRecyclerViewIfInitialLoad() {
        if (! isInitialLoad()) {
            // recycler view should be already visible, nothing to do
            return;
        }

        mBinding.rvArticles.setVisibility(View.VISIBLE);

        mBinding.flMessageContainer.setVisibility(View.GONE);
        mBinding.pbAllArticlesLoadingBar.setVisibility(View.GONE);
        mBinding.tvArticlesMessage.setVisibility(View.GONE);
    }

    private void setEndlessRecyclerViewScrollListener() {
        mEndlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(final int page, final int totalItemsCount) {
                retrieveResults(page);
            }
        };

        mBinding.rvArticles.addOnScrollListener(mEndlessRecyclerViewScrollListener);
    }

    private void markNoMoreItemsToLoad() {
        if (mEndlessRecyclerViewScrollListener != null) {
            mEndlessRecyclerViewScrollListener.setEndReached(true);
        }
        mArticleListAdapter.setEndReached();
    }

    private void tryClearEndlessRecyclerViewScrollListener() {
        if (mEndlessRecyclerViewScrollListener == null) {
            return;
        }
        mBinding.rvArticles.removeOnScrollListener(mEndlessRecyclerViewScrollListener);
        mEndlessRecyclerViewScrollListener = null;
    }
}
