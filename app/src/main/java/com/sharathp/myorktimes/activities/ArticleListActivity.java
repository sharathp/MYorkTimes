package com.sharathp.myorktimes.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sharathp.myorktimes.MYorkTimesApplication;
import com.sharathp.myorktimes.R;
import com.sharathp.myorktimes.repositories.ArticleRepository;

import javax.inject.Inject;

public class ArticleListActivity extends AppCompatActivity {

    @Inject
    ArticleRepository mArticleRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        MYorkTimesApplication.from(this).getComponent().inject(this);
    }
}
