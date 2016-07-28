package com.sharathp.myorktimes.di.modules;

import android.support.annotation.NonNull;

import com.sharathp.myorktimes.BuildConfig;
import com.sharathp.myorktimes.repositories.ArticleRepository;
import com.sharathp.myorktimes.repositories.okhttp.APIKeyInterceptor;
import com.sharathp.myorktimes.util.Constants;

import javax.inject.Singleton;

import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestModule {

    @Singleton
    @NonNull
    @Provides
    public ArticleRepository providesArticleRepository() {
        final OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new APIKeyInterceptor(BuildConfig.NYTIMES_API_TOKEN));

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_NY_TIMES)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(ArticleRepository.class);
    }
}
