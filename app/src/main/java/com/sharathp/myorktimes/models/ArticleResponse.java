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

    public boolean hasMoreResults() {
        if (mResponse == null || mResponse.getDocs() == null || mResponse.getDocs().isEmpty()
                || mResponse.getMeta() == null) {
            return false;
        }

        final Meta meta = mResponse.getMeta();
        return meta.getOffset() + mResponse.getDocs().size() < meta.getHits();
    }

    public static class Response {

        @SerializedName("docs")
        private List<Article> mDocs;

        @SerializedName("meta")
        private Meta mMeta;

        public List<Article> getDocs() {
            return mDocs;
        }

        public void setDocs(final List<Article> docs) {
            mDocs = docs;
        }

        public Meta getMeta() {
            return mMeta;
        }

        public void setMeta(final Meta meta) {
            mMeta = meta;
        }
    }

    public static class Meta {

        @SerializedName("hits")
        private int mHits;

        @SerializedName("offset")
        private int mOffset;

        public int getHits() {
            return mHits;
        }

        public void setHits(final int hits) {
            mHits = hits;
        }

        public int getOffset() {
            return mOffset;
        }

        public void setOffset(final int offset) {
            mOffset = offset;
        }
    }
}
