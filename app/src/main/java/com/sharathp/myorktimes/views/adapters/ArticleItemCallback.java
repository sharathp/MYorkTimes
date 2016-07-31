package com.sharathp.myorktimes.views.adapters;

import com.sharathp.myorktimes.models.SimpleArticle;

/**
 * Interface to be implemented to be notified about actions on article items.
 */
public interface ArticleItemCallback {

    /**
     * Callback invoked when an article is tapped/selected.
     *
     * @param article
     */
    void onArticleSelected(SimpleArticle article);
}
