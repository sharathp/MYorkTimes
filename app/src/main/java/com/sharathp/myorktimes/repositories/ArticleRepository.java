package com.sharathp.myorktimes.repositories;

import com.sharathp.myorktimes.models.ArticleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ArticleRepository {

    @GET("svc/search/v2/articlesearch.json")
    Call<ArticleResponse> getArticles(@Query("q") String query,
                                      @Query("sort") String sort,
                                      @Query("begin_date") String beginDate);

}
