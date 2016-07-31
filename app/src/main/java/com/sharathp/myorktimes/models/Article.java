package com.sharathp.myorktimes.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.Transient;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Parcel
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

    // randomly chose media
    @Transient
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

    @Parcel
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