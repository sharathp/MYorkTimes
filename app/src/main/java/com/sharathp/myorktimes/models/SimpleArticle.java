package com.sharathp.myorktimes.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.sharathp.myorktimes.db.MYorkDatabase;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(database = MYorkDatabase.class)
@Parcel(analyze={SimpleArticle.class})
public class SimpleArticle extends BaseModel {

    @PrimaryKey
    String mId;

    @Column(name = "url")
    String mUrl;

    @Column(name = "headLine")
    String mHeadLine;

    @Column(name = "mediaUrl")
    String mMediaUrl;

    @Column(name = "mediaHeight")
    int mMediaHeight;

    @Column(name = "mediaWidth")
    int mMediaWidth;

    @Column(name = "publishedDate")
    Date mPublishedDate;

    @Column(name = "by")
    String mBy;

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

    public String getHeadLine() {
        return mHeadLine;
    }

    public void setHeadLine(final String headLine) {
        mHeadLine = headLine;
    }

    public String getMediaUrl() {
        return mMediaUrl;
    }

    public void setMediaUrl(final String mediaUrl) {
        mMediaUrl = mediaUrl;
    }

    public int getMediaHeight() {
        return mMediaHeight;
    }

    public void setMediaHeight(final int mediaHeight) {
        mMediaHeight = mediaHeight;
    }

    public int getMediaWidth() {
        return mMediaWidth;
    }

    public void setMediaWidth(final int mediaWidth) {
        mMediaWidth = mediaWidth;
    }

    public Date getPublishedDate() {
        return mPublishedDate;
    }

    public void setPublishedDate(final Date publishedDate) {
        mPublishedDate = publishedDate;
    }

    public String getBy() {
        return mBy;
    }

    public void setBy(final String by) {
        mBy = by;
    }

    public static SimpleArticle convertArticle(final Article article) {
        final SimpleArticle simpleArticle = new SimpleArticle();
        simpleArticle.setId(article.getId());
        simpleArticle.setUrl(article.getUrl());
        simpleArticle.setHeadLine(article.getMainHeadLine());
        simpleArticle.setPublishedDate(article.getPublishedDate());

        final Article.Media media = article.getThumbnail();
        if (media != null) {
            simpleArticle.setMediaUrl(media.getUrl());
            simpleArticle.setMediaHeight(media.getHeight());
            simpleArticle.setMediaWidth(media.getWidth());
        }

        final Article.ByLine byLine = article.getByLine();
        if (byLine != null) {
            simpleArticle.setBy(byLine.getOriginalAuthor());
        }
        return simpleArticle;
    }

    public static List<SimpleArticle> convertArticles(final List<Article> articles) {
        final List<SimpleArticle> simpleArticles = new ArrayList<>();
        for (final Article article : articles) {
            simpleArticles.add(convertArticle(article));
        }
        return simpleArticles;
    }
}
