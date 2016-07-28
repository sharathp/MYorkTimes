package com.sharathp.myorktimes.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticleResponse {

    @SerializedName("response")
    private Response mResponse;

    public Response getResponse() {
        return mResponse;
    }

    public void setResponse(final Response response) {
        mResponse = response;
    }

    public static class Response {

        @SerializedName("docs")
        private List<Article> mDocs;

        public List<Article> getDocs() {
            return mDocs;
        }

        public void setDocs(final List<Article> docs) {
            mDocs = docs;
        }
    }
}
