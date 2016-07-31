package com.sharathp.myorktimes.util;

import android.content.Context;
import android.content.Intent;

import com.sharathp.myorktimes.activities.ArticleDetailActivity;
import com.sharathp.myorktimes.models.SimpleArticle;

public class NavigationUtils {

    public static Intent getDetailActivityIntent(final Context context, final SimpleArticle article) {
        final Intent intent = ArticleDetailActivity.createIntent(context, article);
        return intent;
    }
}
