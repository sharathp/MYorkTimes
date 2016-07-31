package com.sharathp.myorktimes.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Article {
    private static final String KEY_HEADLINE_MAIN = "main";

    // this is returning a very small image
    // private static final String SUBTYPE_THUMBNAIL = "thumbnail";

    @SerializedName("_id")
    String mId;

    @SerializedName("web_url")
    String mUrl;

    @SerializedName("headline")
    Map<String, String> mHeadLines;

    @SerializedName("multimedia")
    List<Media> mMedia;

    @SerializedName("pub_date")
    Date mPublishedDate;

    @SerializedName("byline")
    ByLine mByLine;

    // randomly chose media
    private Media mSelectedMedia;

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

    public List<Media> getMedia() {
        return mMedia;
    }

    public void setMedia(List<Media> media) {
        mMedia = media;
    }

    public Date getPublishedDate() {
        return mPublishedDate;
    }

    public void setPublishedDate(final Date publishedDate) {
        mPublishedDate = publishedDate;
    }

    public ByLine getByLine() {
        return mByLine;
    }

    public void setByLine(ByLine byLine) {
        mByLine = byLine;
    }

    public String getMainHeadLine() {
        if (mHeadLines == null) {
            return null;
        }

        return mHeadLines.get(KEY_HEADLINE_MAIN);
    }

    public Media getThumbnail() {
        if (mMedia == null || mMedia.isEmpty()) {
            return null;
        }

        // if an image was previously selected, return it
        if (mSelectedMedia == null) {
            // get random image
            final int index = new Random().nextInt(mMedia.size());
            mSelectedMedia = mMedia.get(index);
        }
        return mSelectedMedia;
    }

    public static class Media {

        @SerializedName("width")
        int mWidth;

        @SerializedName("height")
        int mHeight;

        @SerializedName("url")
        String mUrl;

        @SerializedName("type")
        String mType;

        @SerializedName("subtype")
        String mSubtype;

        public int getWidth() {
            return mWidth;
        }

        public void setWidth(final int width) {
            mWidth = width;
        }

        public int getHeight() {
            return mHeight;
        }

        public void setHeight(final int height) {
            mHeight = height;
        }

        public String getUrl() {
            return mUrl;
        }

        public void setUrl(final String url) {
            mUrl = url;
        }
    }

    public static class ByLine {
        private final static String PREFIX_BY = "By ";

        @SerializedName("original")
        String mOriginal;

        @SerializedName("organization")
        String mOrganization;

        public String getOriginal() {
            return mOriginal;
        }

        public void setOriginal(final String original) {
            mOriginal = original;
        }

        // removes "By" if it exists
        public String getOriginalAuthor() {
            if (getOriginal() == null) {
                return null;
            }

            if (mOriginal.startsWith(PREFIX_BY)) {
                return mOriginal.substring(PREFIX_BY.length(), mOriginal.length());
            }
            return mOriginal;
        }
    }

}