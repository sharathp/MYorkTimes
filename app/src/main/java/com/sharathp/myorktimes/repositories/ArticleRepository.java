package com.sharathp.myorktimes.repositories;

import com.sharathp.myorktimes.models.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ArticleRepository {

    @GET("svc/search/v2/articlesearch.json")
    Call<List<Article>> getArticles(@Query("q") String query,
                                    @Query("sort") String sort,
                                    @Query("begin_date") String beginDate);

}
