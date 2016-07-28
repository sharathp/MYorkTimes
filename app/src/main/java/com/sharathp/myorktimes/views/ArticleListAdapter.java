package com.sharathp.myorktimes.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sharathp.myorktimes.R;
import com.sharathp.myorktimes.models.Article;
import com.sharathp.myorktimes.util.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.AbstractArticleViewHolder> {
    private static final int TYPE_IMAGE_ARTICLE = 0;
    private static final int TYPE_TEXT_ARTICLE = 1;

    private List<Article> mArticles;
    private final ArticleItemCallback mArticleItemCallback;

    public ArticleListAdapter(final List<Article> articles, final ArticleItemCallback articleItemCallback) {
        mArticles = articles;
        mArticleItemCallback = articleItemCallback;
    }

    @Override
    public int getItemViewType(final int position) {
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
            default: {
                throw new IllegalArgumentException("Invalid viewType: " + viewType);
            }
        }
    }

    @Override
    public void onBindViewHolder(final AbstractArticleViewHolder holder, final int position) {
        Article article = mArticles.get(position);
        holder.bind(article);
    }

    @Override
    public int getItemCount() {
        if (mArticles == null) {
            return 0;
        }

        return mArticles.size();
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

    public static abstract class AbstractArticleViewHolder extends RecyclerView.ViewHolder {
        protected final ArticleItemCallback mArticleItemCallback;
        protected Article mArticle;

        private View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mArticleItemCallback.onArticleSelected(mArticle);
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

            Glide.with(itemView.getContext())
                    .load(Constants.getImageUrl(mArticle.getThumbnail().getUrl()))
                    .centerCrop()
                    .into(mThumbnailImageView);
        }
    }

    public static class TextArticleViewHolder extends AbstractArticleViewHolder {

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