package com.sharathp.myorktimes.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sharathp.myorktimes.MYorkTimesApplication;
import com.sharathp.myorktimes.R;
import com.sharathp.myorktimes.databinding.FragmentArticleListBinding;
import com.sharathp.myorktimes.models.ArticleResponse;
import com.sharathp.myorktimes.models.SimpleArticle;
import com.sharathp.myorktimes.repositories.ArticleRepository;
import com.sharathp.myorktimes.repositories.LocalPreferencesRepository;
import com.sharathp.myorktimes.util.NavigationUtils;
import com.sharathp.myorktimes.util.NetworkUtils;
import com.sharathp.myorktimes.util.RepositoryUtils;
import com.sharathp.myorktimes.views.EndlessRecyclerViewScrollListener;
import com.sharathp.myorktimes.views.adapters.ArticleItemCallback;
import com.sharathp.myorktimes.views.adapters.ArticlesListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleListFragment extends Fragment implements ArticleItemCallback {
    private static final String TAG = ArticleListFragment.class.getSimpleName();

    @Inject
    ArticleRepository mArticleRepository;

    @Inject
    LocalPreferencesRepository mPreferencesRepository;

    private FragmentArticleListBinding mBinding;

    private ArticlesListAdapter mArticleListAdapter;
    private Call<ArticleResponse> mCurrentCall;
    private String mCurrentQuery;

    private EndlessRecyclerViewScrollListener mEndlessRecyclerViewScrollListener;
    private StaggeredGridLayoutManager mLayoutManager;

    public static Fragment createInstance() {
        return new ArticleListFragment();
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MYorkTimesApplication.from(getActivity()).getComponent().inject(this);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_list, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mCurrentCall != null) {
            mCurrentCall.cancel();
        }

        mCurrentCall = null;
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_article_list, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                tryClearEndlessRecyclerViewScrollListener();
                mArticleListAdapter.setArticles(null);
                mArticleListAdapter.clearEndReached();

                if (! NetworkUtils.isOnline()) {
                    showMessageIfInitialLoad(getString(R.string.message_no_internet));
                } else {
                    showInitialLoader();

                    mCurrentQuery = query;
                    // get the first page of results
                    retrieveResults(0);
                }
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
    }

    @Override
    public void onArticleSelected(final SimpleArticle article) {
        startActivity(NavigationUtils.getDetailActivityIntent(getActivity(), article));
    }

    private void initViews() {
        mArticleListAdapter = new ArticlesListAdapter(new ArrayList<>(), this);
        final RecyclerView moviesRecyclerView = mBinding.rvArticles;
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        moviesRecyclerView.setAdapter(mArticleListAdapter);
        moviesRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void retrieveResults(final int page) {
        Log.d(TAG, "Retrieving results: " + page);

        if (mCurrentCall != null) {
            mCurrentCall.cancel();
        }

        if (TextUtils.isEmpty(mCurrentQuery)) {
            return;
        }

        mCurrentCall = mArticleRepository.getArticles(mCurrentQuery, getFilteredQuery(), getSortBy(), getStartDate(), getEndDate(), page);
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
                mArticleListAdapter.addArticles(SimpleArticle.convertArticles(articleResponse.getResponse().getDocs()));
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
                    ArticleListFragment.this.setEndlessRecyclerViewScrollListener();
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

    private String getStartDate() {
        final Date startDate = mPreferencesRepository.getPreferredStartDate();
        return RepositoryUtils.getFormattedStartDate(startDate);
    }

    private String getEndDate() {
        final Date endDate = mPreferencesRepository.getPreferredEndDate();
        if (endDate == null) {
            return null;
        }
        return RepositoryUtils.getFormattedStartDate(endDate);
    }

    private String getSortBy() {
        return mPreferencesRepository.getPreferredSortBy();
    }

    private String getFilteredQuery() {
        final Set<String> sections = mPreferencesRepository.getPreferredNewsDeskSections();
        return RepositoryUtils.getFilteredQuery(sections);
    }
}