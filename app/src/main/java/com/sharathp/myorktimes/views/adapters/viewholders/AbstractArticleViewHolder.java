package com.sharathp.myorktimes.views.adapters.viewholders;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sharathp.myorktimes.R;
import com.sharathp.myorktimes.models.SimpleArticle;
import com.sharathp.myorktimes.util.DateUtils;
import com.sharathp.myorktimes.util.TextUtils;
import com.sharathp.myorktimes.views.adapters.ArticleItemCallback;

import butterknife.BindView;

public abstract class AbstractArticleViewHolder extends RecyclerView.ViewHolder {
    protected final ArticleItemCallback mArticleItemCallback;
    protected SimpleArticle mArticle;

    @Nullable
    @BindView(R.id.tv_article_pub_by)
    TextView mPublishedByTextView;

    @Nullable
    @BindView(R.id.tv_article_pub_date)
    TextView mPublishedDateTextView;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            if (mArticleItemCallback != null) {
                mArticleItemCallback.onArticleSelected(mArticle);
            }
        }
    };

    public AbstractArticleViewHolder(final View itemView, final ArticleItemCallback articleItemCallback) {
        super(itemView);
        itemView.setOnClickListener(mOnClickListener);
        mArticleItemCallback = articleItemCallback;
    }

    public final void bind(final SimpleArticle article) {
        mArticle = article;

        String author = itemView.getContext().getString(R.string.author_name_unknown);
        if (article.getBy() != null) {
            author = TextUtils.convertToTitleCase(article.getBy());
        }

        mPublishedByTextView.setText(author);
        mPublishedDateTextView.setText(DateUtils.getRelativeTime(article.getPublishedDate()));

        doBind();
    }

    protected abstract void doBind();
}
