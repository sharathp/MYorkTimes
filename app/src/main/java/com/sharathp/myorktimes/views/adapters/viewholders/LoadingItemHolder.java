package com.sharathp.myorktimes.views.adapters.viewholders;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sharathp.myorktimes.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingItemHolder extends AbstractArticleViewHolder {

    @BindView(R.id.pb_loading_progress_bar)
    ProgressBar mLoadingProgressBar;

    @BindView(R.id.tv_article_end_message)
    TextView mEndOfArticlesTextView;

    public LoadingItemHolder(final View itemView) {
        super(itemView, null);
        ButterKnife.bind(this, itemView);
    }

    public void showEndOfFeedReached() {
        mLoadingProgressBar.setVisibility(View.GONE);
        mEndOfArticlesTextView.setVisibility(View.VISIBLE);
    }

    public void hideEndOfFeedReached() {
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        mEndOfArticlesTextView.setVisibility(View.GONE);
    }

    @Override
    protected void doBind() {
        // no-op
    }
}
