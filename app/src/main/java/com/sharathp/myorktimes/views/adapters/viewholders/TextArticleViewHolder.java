package com.sharathp.myorktimes.views.adapters.viewholders;

import android.view.View;
import android.widget.TextView;

import com.sharathp.myorktimes.R;
import com.sharathp.myorktimes.views.adapters.ArticleItemCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TextArticleViewHolder extends AbstractArticleViewHolder {

    @BindView(R.id.tv_article_title)
    TextView mTitleTextView;

    public TextArticleViewHolder(final View itemView, final ArticleItemCallback articleItemCallback) {
        super(itemView, articleItemCallback);
        ButterKnife.bind(this, itemView);
    }

    public void doBind() {
        mTitleTextView.setText(mArticle.getHeadLine());
    }
}
