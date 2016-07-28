package com.sharathp.myorktimes.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;

public class Article {
    private static final String KEY_HEADLINE_MAIN = "main";

    // this is returning a very small image
    // private static final String SUBTYPE_THUMBNAIL = "thumbnail";

    @SerializedName("_id")
    private String mId;

    @SerializedName("web_url")
    private String mUrl;

    @SerializedName("headline")
    private Map<String, String> mHeadLines;

    @SerializedName("multimedia")
    private Media[] mMedia;

    @SerializedName("pub_date")
    private Date mPublishedDate;

    public String getId() {
        return mId;
    }

    public void setId(final String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(final String url) {
        mUrl = url;
    }

    public Map<String, String> getHeadLines() {
        return mHeadLines;
    }

    public void setHeadLines(final Map<String, String> headLines) {
        mHeadLines = headLines;
    }

    public Media[] getMedia() {
        return mMedia;
    }

    public void setMedia(final Media[] media) {
        mMedia = media;
    }

    public Date getPublishedDate() {
        return mPublishedDate;
    }

    public void setPublishedDate(final Date publishedDate) {
        mPublishedDate = publishedDate;
    }

    public String getMainHeadLine() {
        if (mHeadLines == null) {
            return null;
        }

        return mHeadLines.get(KEY_HEADLINE_MAIN);
    }

    public Media getThumbnail() {
        if (mMedia == null || mMedia.length < 1) {
            return null;
        }

        // get the first one
        return mMedia[0];
    }

    public static class Media {

        @SerializedName("width")
        private String mWidth;

        @SerializedName("height")
        private String mHeight;

        @SerializedName("url")
        private String mUrl;

        @SerializedName("type")
        private String mType;

        @SerializedName("subtype")
        private String mSubtype;

        public String getWidth() {
            return mWidth;
        }

        public void setWidth(final String width) {
            mWidth = width;
        }

        public String getHeight() {
            return mHeight;
        }

        public void setHeight(final String height) {
            mHeight = height;
        }

        public String getUrl() {
            return mUrl;
        }

        public void setUrl(final String url) {
            mUrl = url;
        }

        public String getType() {
            return mType;
        }

        public void setType(final String type) {
            mType = type;
        }

        public String getSubtype() {
            return mSubtype;
        }

        public void setSubtype(final String subtype) {
            mSubtype = subtype;
        }
    }
}