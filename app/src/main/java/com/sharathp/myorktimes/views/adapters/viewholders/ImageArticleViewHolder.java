package com.sharathp.myorktimes.views.adapters.viewholders;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sharathp.myorktimes.R;
import com.sharathp.myorktimes.util.Constants;
import com.sharathp.myorktimes.views.DynamicHeightImageView;
import com.sharathp.myorktimes.views.adapters.ArticleItemCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageArticleViewHolder extends AbstractArticleViewHolder {

    @BindView(R.id.iv_article_thumbnail)
    DynamicHeightImageView mThumbnailImageView;

    @BindView(R.id.tv_article_title)
    TextView mTitleTextView;

    public ImageArticleViewHolder(final View itemView, final ArticleItemCallback articleItemCallback) {
        super(itemView, articleItemCallback);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void doBind() {
        mTitleTextView.setText(mArticle.getHeadLine());

        mThumbnailImageView.setHeightRatio((float) mArticle.getMediaHeight() / (float)mArticle.getMediaWidth());

        Glide.clear(mThumbnailImageView);

        Glide.with(itemView.getContext())
                .load(Constants.getImageUrl(mArticle.getMediaUrl()))
                .fitCenter()
                .placeholder(R.drawable.ic_progress_indeterminate)
                .error(R.drawable.ic_error)
                .into(mThumbnailImageView);
    }
}
