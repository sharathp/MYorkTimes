package com.sharathp.myorktimes.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharathp.myorktimes.R;
import com.sharathp.myorktimes.models.SimpleArticle;
import com.sharathp.myorktimes.views.adapters.viewholders.AbstractArticleViewHolder;
import com.sharathp.myorktimes.views.adapters.viewholders.ImageArticleViewHolder;
import com.sharathp.myorktimes.views.adapters.viewholders.LoadingItemHolder;
import com.sharathp.myorktimes.views.adapters.viewholders.TextArticleViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

public class ArticlesListAdapter extends RecyclerView.Adapter<AbstractArticleViewHolder> {
    private static final int TYPE_IMAGE_ARTICLE = 0;
    private static final int TYPE_TEXT_ARTICLE = 1;
    private static final int TYPE_LOADING = 2;
    private boolean mIsEndReached;

    // weak reference to not avoid garbage collection the instance..
    private WeakReference<LoadingItemHolder> mLoadingItemHolder;

    private List<SimpleArticle> mArticles;
    private final ArticleItemCallback mArticleItemCallback;

    public ArticlesListAdapter(final List<SimpleArticle> articles, final ArticleItemCallback articleItemCallback) {
        mArticles = articles;
        mArticleItemCallback = articleItemCallback;
    }

    @Override
    public int getItemViewType(final int position) {
        if (isPositionForLoadingIndicator(position)) {
            return TYPE_LOADING;
        }

        final SimpleArticle article = mArticles.get(position);

        if (article.getMediaUrl() == null) {
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

        SimpleArticle article = mArticles.get(position);
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

    public void setArticles(final List<SimpleArticle> articles) {
        if (articles != null) {
            mArticles = articles;
        } else {
            mArticles.clear();
        }
        notifyDataSetChanged();
    }

    public void addArticles(final List<SimpleArticle> articles) {
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
}