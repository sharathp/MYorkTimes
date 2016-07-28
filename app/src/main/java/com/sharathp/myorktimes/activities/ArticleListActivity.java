package com.sharathp.myorktimes.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.sharathp.myorktimes.MYorkTimesApplication;
import com.sharathp.myorktimes.R;
import com.sharathp.myorktimes.databinding.ActivityArticleListBinding;
import com.sharathp.myorktimes.models.Article;
import com.sharathp.myorktimes.models.ArticleResponse;
import com.sharathp.myorktimes.repositories.ArticleRepository;
import com.sharathp.myorktimes.views.ArticleListAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MYorkTimesApplication.from(this).getComponent().inject(this);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_article_list);
        setSupportActionBar(mBinding.toolbarLayout.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mArticleListAdapter = new ArticleListAdapter(new ArrayList<>(), this);
        final RecyclerView moviesRecyclerView = mBinding.rvArticles;
        moviesRecyclerView.setAdapter(mArticleListAdapter);
        moviesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    protected void onResume() {
        super.onResume();

        mCurrentCall = mArticleRepository.getArticles("trump", "oldest", "20160112");
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
                mArticleListAdapter.addMovies(articleResponse.getResponse().getDocs());
            }

            @Override
            public void onFailure(final Call<ArticleResponse> call, final Throwable t) {
                Toast.makeText(ArticleListActivity.this, "Error retrieving articles: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onArticleSelected(final Article article) {
        // no-op
    }
}
