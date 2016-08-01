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
import com.sharathp.myorktimes.views.adapters.viewholders.TextArticleViewHolder;

import java.util.List;

public class BookmarksListAdapter extends RecyclerView.Adapter<AbstractArticleViewHolder> {
    private static final int TYPE_IMAGE_ARTICLE = 0;
    private static final int TYPE_TEXT_ARTICLE = 1;
    private List<SimpleArticle> mArticles;
    private final ArticleItemCallback mArticleItemCallback;

    public BookmarksListAdapter(final List<SimpleArticle> articles, final ArticleItemCallback articleItemCallback) {
        mArticles = articles;
        mArticleItemCallback = articleItemCallback;
    }

    @Override
    public int getItemViewType(final int position) {
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
            default: {
                throw new IllegalArgumentException("Invalid viewType: " + viewType);
            }
        }
    }

    @Override
    public void onBindViewHolder(final AbstractArticleViewHolder holder, final int position) {
        SimpleArticle article = mArticles.get(position);
        holder.bind(article);
    }

    @Override
    public int getItemCount() {
        if (mArticles == null || mArticles.isEmpty()) {
            return 0;
        }

        return mArticles.size();
    }

    public void setBookmarks(final List<SimpleArticle> articles) {
        if (articles != null) {
            mArticles = articles;
        } else {
            mArticles.clear();
        }
        notifyDataSetChanged();
    }

    public void addBookmarks(final List<SimpleArticle> articles) {
        mArticles.addAll(articles);
        notifyDataSetChanged();
    }
}