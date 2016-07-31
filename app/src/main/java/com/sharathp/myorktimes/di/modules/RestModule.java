package com.sharathp.myorktimes.di.modules;

import android.support.annotation.NonNull;

import com.sharathp.myorktimes.BuildConfig;
import com.sharathp.myorktimes.repositories.ArticleRepository;
import com.sharathp.myorktimes.repositories.okhttp.APIKeyInterceptor;
import com.sharathp.myorktimes.util.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RestModule {

    @Singleton
    @NonNull
    @Provides
    public ArticleRepository providesArticleRepository() {
        final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new APIKeyInterceptor(BuildConfig.NYTIMES_API_TOKEN));
        builder.addInterceptor(httpLoggingInterceptor);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_API_NY_TIMES)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        return retrofit.create(ArticleRepository.class);
    }
}
