package com.sharathp.myorktimes.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sharathp.myorktimes.R;
import com.sharathp.myorktimes.databinding.ActivityArticleDetailBinding;
import com.sharathp.myorktimes.models.Article;

import org.parceler.Parcels;

public class ArticleDetailActivity  extends AppCompatActivity {
    public static final String EXTRA_ARTICLE = ArticleDetailActivity.class.getSimpleName() + ":ARTICLE";

    private ActivityArticleDetailBinding mBinding;
    private Article mArticle;

    public static Intent createIntent(final Context context, final Article article) {
        final Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(EXTRA_ARTICLE, Parcels.wrap(article));
        return intent;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_article_detail);

        setSupportActionBar(mBinding.toolbarLayout.toolbar);
        mBinding.toolbarLayout.toolbarIcon.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mArticle = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_ARTICLE));
        configureWebView();
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
