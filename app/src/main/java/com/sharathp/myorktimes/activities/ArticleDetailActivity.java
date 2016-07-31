package com.sharathp.myorktimes.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sharathp.myorktimes.MYorkTimesApplication;
import com.sharathp.myorktimes.R;
import com.sharathp.myorktimes.databinding.ActivityArticleDetailBinding;
import com.sharathp.myorktimes.models.SimpleArticle;
import com.sharathp.myorktimes.repositories.BookmarksRepository;
import com.sharathp.myorktimes.util.ViewUtils;

import org.parceler.Parcels;

import javax.inject.Inject;

public class ArticleDetailActivity extends AppCompatActivity implements
        BookmarksRepository.ExistsCallback,
        BookmarksRepository.InsertCallback,
        BookmarksRepository.DeleteCallback {

    private static final String TAG = ArticleDetailActivity.class.getSimpleName() + ": ArticleDetailActivity";
    public static final String EXTRA_ARTICLE = ArticleDetailActivity.class.getSimpleName() + ":ARTICLE";

    @Inject
    BookmarksRepository mBookmarksRepository;

    private ActivityArticleDetailBinding mBinding;
    private SimpleArticle mArticle;

    private MenuItem mRemoveBookmark;
    private MenuItem mAddBookmark;

    public static Intent createIntent(final Context context, final SimpleArticle article) {
        final Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(EXTRA_ARTICLE, Parcels.wrap(article));
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share : {
                shareArticleLink();
                return true;
            }
            case R.id.action_add_bookmark: {
                addBookmark();
                return true;
            }
            case R.id.action_remove_bookmark: {
                removeBookmark();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article_detail, menu);
        mAddBookmark = menu.findItem(R.id.action_add_bookmark);
        mRemoveBookmark = menu.findItem(R.id.action_remove_bookmark);

        // hide both initially
        hideBookmarkMenuItems();
        return true;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_article_detail);
        MYorkTimesApplication.from(this).getComponent().inject(this);

        setSupportActionBar(mBinding.toolbarLayout.toolbar);
        mBinding.toolbarLayout.toolbarIcon.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewUtils.setToolbarTitleFont(this, mBinding.toolbarLayout.toolbarTitle);

        mArticle = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_ARTICLE));
        configureWebView();

        mBookmarksRepository.isArticleBookmarked(mArticle, this);
    }

    @Override
    public void existsCheckSuccessfully(final SimpleArticle article, final boolean isBookmarked) {
        toggleBookmarkMenuItems(isBookmarked);
    }

    @Override
    public void bookmarkDeletedSuccessfully(final SimpleArticle article) {
        Log.i(TAG, "Bookmark deletion success: " + article.getId());
        toggleBookmarkMenuItems(false);
    }

    @Override
    public void bookmarkDeleteFailed(final SimpleArticle article, final Throwable error) {
        Log.e(TAG, "Bookmark deletion failed: " + article.getId(), error);
        hideBookmarkMenuItems();
    }

    @Override
    public void bookmarkInsertedSuccessfully(final SimpleArticle article) {
        Log.i(TAG, "Bookmark insertion success: " + article.getId());
        toggleBookmarkMenuItems(true);
    }

    @Override
    public void bookmarkInsertFailed(final SimpleArticle article, final Throwable error) {
        Log.e(TAG, "Bookmark insertion failed: " + article.getId(), error);
        hideBookmarkMenuItems();
    }

    private void toggleBookmarkMenuItems(final boolean isBookmarked) {
        mRemoveBookmark.setVisible(isBookmarked);
        mAddBookmark.setVisible(! isBookmarked);
    }

    private void hideBookmarkMenuItems() {
        mAddBookmark.setVisible(false);
        mRemoveBookmark.setVisible(false);
    }

    private void removeBookmark() {
        mBookmarksRepository.deleteBookmark(mArticle, this);
    }

    private void addBookmark() {
        mBookmarksRepository.insertBookmark(mArticle, this);
    }

    private void configureWebView() {
        final WebView webView = mBinding.webview;
        // Configure related browser settings
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        // Enable responsive layout
        webView.getSettings().setUseWideViewPort(true);
        // Zoom out if the content width is greater than the width of the veiwport
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.getSettings().setSupportZoom(true);
        // allow pinch to zooom
        webView.getSettings().setBuiltInZoomControls(true);

        // Configure the client to use when opening URLs
        webView.setWebViewClient(new MyBrowser());

        // Load the initial URL
        webView.loadUrl(mArticle.getUrl());
    }

    private void shareArticleLink() {
        final Intent shareIntent = getShareIntent();
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_intent_title)));
    }

    private Intent getShareIntent() {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, mArticle.getUrl());
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mArticle.getHeadLine());
        return shareIntent;
    }

    // Manages the behavior when URLs are loaded
    class MyBrowser extends WebViewClient {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            final WebView webView = mBinding.webview;

            // already visible, nothing to do
            if (View.VISIBLE == webView.getVisibility()) {
                return;
            }

            mBinding.webview.setVisibility(View.VISIBLE);
            mBinding.pbArticleLoadingBar.setVisibility(View.GONE);
        }
    }
}
