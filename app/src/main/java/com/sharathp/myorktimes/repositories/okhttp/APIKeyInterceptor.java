package com.sharathp.myorktimes.repositories.okhttp;

import com.sharathp.myorktimes.util.Constants;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class APIKeyInterceptor implements Interceptor {
    private static final String QUERY_PARAM_API_KEY = "api-key";
    private final String mApiKey;

    public APIKeyInterceptor(final String apiKey) {
        mApiKey = apiKey;
    }

    @Override
    public Response intercept(final Chain chain) throws IOException {
        Request request = chain.request();

        if (isNYTimesRequest(request)) {
            request = addAPIKey(request);
        }

        return chain.proceed(request);
    }

    private boolean isNYTimesRequest(final Request request) {
        return request.url().url().toString().startsWith(Constants.BASE_URL_API_NY_TIMES);
    }

    private Request addAPIKey(final Request request) {
        HttpUrl url = request.url().newBuilder().addQueryParameter(QUERY_PARAM_API_KEY, mApiKey).build();
        final Request newRequest = request.newBuilder().url(url).build();
        return  newRequest;
    }
}
