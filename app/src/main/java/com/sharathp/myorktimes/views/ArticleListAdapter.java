package com.sharathp.myorktimes.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sharathp.myorktimes.R;
import com.sharathp.myorktimes.models.Article;
import com.sharathp.myorktimes.util.Constants;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.AbstractArticleViewHolder> {
    private static final int TYPE_IMAGE_ARTICLE = 0;
    private static final int TYPE_TEXT_ARTICLE = 1;
    private static final int TYPE_LOADING = 2;
    private boolean mIsEndReached;
    // weak reference to not avoid garbage collection the instance..
    private WeakReference<LoadingItemHolder> mLoadingItemHolder;

    private List<Article> mArticles;
    private final ArticleItemCallback mArticleItemCallback;

    public ArticleListAdapter(final List<Article> articles, final ArticleItemCallback articleItemCallback) {
        mArticles = articles;
        mArticleItemCallback = articleItemCallback;
    }

    @Override
    public int getItemViewType(final int position) {
        if (isPositionForLoadingIndicator(position)) {
            return TYPE_LOADING;
        }

        final Article article = mArticles.get(position);

        if (article.getThumbnail() == null) {
            return TYPE_TEXT_ARTICLE;
        } else {
            return TYPE_IMAGE_ARTICLE;
        }
    }

    @Override
    public AbstractArticleViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final Context context = parent.getContext();

        final LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case TYPE_IMAGE_ARTICLE: {
                final View movieView = inflater.inflate(R.layout.item_article_with_image, parent, false);
                return new ImageArticleViewHolder(movieView, mArticleItemCallback);
            }
            case TYPE_TEXT_ARTICLE: {
                final View movieView = inflater.inflate(R.layout.item_article_text, parent, false);
                return new TextArticleViewHolder(movieView, mArticleItemCallback);
            }
            case TYPE_LOADING: {
                final View view = inflater.inflate(R.layout.view_articles_loading, parent, false);
                return new LoadingItemHolder(view);
            }
            default: {
                throw new IllegalArgumentException("Invalid viewType: " + viewType);
            }
        }
    }

    @Override
    public void onBindViewHolder(final AbstractArticleViewHolder holder, final int position) {
        // this is loading indicator, so, do nothing
        if (isPositionForLoadingIndicator(position)) {
            // maintain a reference to holder to handle the case where mEndOfFeedReached is set
            // after the view is bound here
            mLoadingItemHolder = new WeakReference<>((LoadingItemHolder)holder);
            // bind it here since the cursor is already exhausted at this point and super class would complain about it..
            if (mIsEndReached) {
                ((LoadingItemHolder)holder).showEndOfFeedReached();
            }
            return;
        }

        Article article = mArticles.get(position);
        holder.bind(article);
    }

    @Override
    public int getItemCount() {
        if (mArticles == null || mArticles.isEmpty()) {
            return 0;
        }

        // to show the spinner
        return mArticles.size() + 1;
    }

    private boolean isPositionForLoadingIndicator(final int position) {
        // last item is the loading indicator
        return (position == mArticles.size());
    }

    public void setArticles(final List<Article> articles) {
        if (articles != null) {
            mArticles = articles;
        } else {
            mArticles.clear();
        }
        notifyDataSetChanged();
    }

    public void addMovies(final List<Article> articles) {
        mArticles.addAll(articles);
        notifyDataSetChanged();
    }

    public void setEndReached() {
        mIsEndReached = true;
        final LoadingItemHolder loadingItemHolder = getLoadingItemHolder();
        if (loadingItemHolder != null) {
            loadingItemHolder.showEndOfFeedReached();
        }
    }

    public void clearEndReached() {
        mIsEndReached = false;
        final LoadingItemHolder loadingItemHolder = getLoadingItemHolder();
        if (loadingItemHolder != null) {
            loadingItemHolder.hideEndOfFeedReached();
        }
    }

    private LoadingItemHolder getLoadingItemHolder() {
        if (mLoadingItemHolder == null) {
            return null;
        }
        return mLoadingItemHolder.get();
    }

    public static abstract class AbstractArticleViewHolder extends RecyclerView.ViewHolder {
        protected final ArticleItemCallback mArticleItemCallback;
        protected Article mArticle;

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

        public final void bind(final Article article) {
            mArticle = article;
            doBind();
        }

        protected abstract void doBind();
    }

    public static class ImageArticleViewHolder extends AbstractArticleViewHolder {

        @BindView(R.id.iv_article_thumbnail)
        ImageView mThumbnailImageView;

        @BindView(R.id.tv_article_title)
        TextView mTitleTextView;

        public ImageArticleViewHolder(final View itemView, final ArticleItemCallback articleItemCallback) {
            super(itemView, articleItemCallback);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void doBind() {
            mTitleTextView.setText(mArticle.getMainHeadLine());

            final Article.Media image = mArticle.getThumbnail();
            // set height
            setThumbnailImageViewDimensions(image);

//            mThumbnailImageView.setHeightRatio((float)media.getHeight()/(float)media.getWidth());

//            Picasso.with(itemView.getContext())
//                    .load(Constants.getPosterImageUrl(mMovie.getPosterPath()))
//                    .fit()
//                    .centerInside()
//                    .placeholder(R.drawable.placeholder)
//                    .error(R.drawable.error_placeholder)
//                    .transform(new RoundedCornersTransformation(Constants.ROUND_TRANSFORMATION_RADIUS, Constants.ROUND_TRANSFORMATION_MARGIN))
//                    .into(mPosterImageView);

            Glide.with(itemView.getContext())
                    .load(Constants.getImageUrl(image.getUrl()))
                    .fitCenter()
                    .into(mThumbnailImageView);
        }

        private void setThumbnailImageViewDimensions(final Article.Media image) {
            final int viewWidth = mThumbnailImageView.getWidth();
            final float viewHeight = ((float)image.getHeight()/(float)image.getWidth()) * viewWidth;
            mThumbnailImageView.getLayoutParams().height = (int) viewHeight;
            itemView.requestLayout();
        }
    }

    static class TextArticleViewHolder extends AbstractArticleViewHolder {

        @BindView(R.id.tv_article_title)
        TextView mTitleTextView;

        public TextArticleViewHolder(final View itemView, final ArticleItemCallback articleItemCallback) {
            super(itemView, articleItemCallback);
            ButterKnife.bind(this, itemView);
        }

        public void doBind() {
            mTitleTextView.setText(mArticle.getMainHeadLine());
        }
    }

    static class LoadingItemHolder extends AbstractArticleViewHolder {

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

    /**
     * Interface to be implemented to be notified about actions on article items.
     */
    public interface ArticleItemCallback {

        /**
         * Callback invoked when an article is tapped/selected.
         *
         * @param article
         */
        void onArticleSelected(Article article);
    }
}