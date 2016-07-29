package com.sharathp.myorktimes.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.sharathp.myorktimes.MYorkTimesApplication;
import com.sharathp.myorktimes.R;
import com.sharathp.myorktimes.databinding.ActivityArticleListBinding;
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

    @Inject
    ArticleRepository mArticleRepository;

    private ArticleListAdapter mArticleListAdapter;
    private ActivityArticleListBinding mBinding;
    private Call<ArticleResponse> mCurrentCall;
    private String mCurrentQuery;


    private RecyclerView.OnScrollListener mEndlessRecyclerViewScrollListener;
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
        moviesRecyclerView.setAdapter(mArticleListAdapter);
        moviesRecyclerView.setLayoutManager(mLayoutManager);
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
//
        // Customize searchview text and hint colors
        final int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        final EditText et = (EditText) searchView.findViewById(searchEditId);
        final int color = getResources().getColor(R.color.colorPrimaryDark);
        et.setTextColor(color);
        et.setHintTextColor(color);
        return true;
    }

    private void retrieveResults(final int page) {
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
                    Toast.makeText(ArticleListActivity.this, "Error retrieving articles: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                final ArticleResponse articleResponse = response.body();
                if (articleResponse == null || articleResponse.getResponse() == null || articleResponse.getResponse().getDocs() == null) {
                    Toast.makeText(ArticleListActivity.this, "No articles" + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                setEndlessRecyclerViewScrollListener(articleResponse);
                mArticleListAdapter.addMovies(articleResponse.getResponse().getDocs());
            }

            @Override
            public void onFailure(final Call<ArticleResponse> call, final Throwable t) {
                Toast.makeText(ArticleListActivity.this, "Error retrieving articles: " + t.getMessage(), Toast.LENGTH_LONG).show();
                tryClearEndlessRecyclerViewScrollListener();
            }

            private void setEndlessRecyclerViewScrollListener(final ArticleResponse articleResponse) {
                final boolean hasMoreResults = articleResponse.hasMoreResults();

                if (hasMoreResults) {
                    if (mEndlessRecyclerViewScrollListener != null) {
                        // already set, do nothing
                        return;
                    } else {
                        // looks like first time retrieving results, set the listener
                        setEndlessRecyclerViewScrollListener();
                    }
                } else {
                    tryClearEndlessRecyclerViewScrollListener();
                }
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
        });
    }

    private void tryClearEndlessRecyclerViewScrollListener() {
        if (mEndlessRecyclerViewScrollListener == null) {
            return;
        }
        mBinding.rvArticles.removeOnScrollListener(mEndlessRecyclerViewScrollListener);
        mEndlessRecyclerViewScrollListener = null;
    }
}
