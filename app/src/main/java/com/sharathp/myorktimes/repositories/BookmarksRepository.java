package com.sharathp.myorktimes.repositories;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.sharathp.myorktimes.db.MYorkDatabase;
import com.sharathp.myorktimes.models.SimpleArticle;
import com.sharathp.myorktimes.models.SimpleArticle_Table;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BookmarksRepository {

    @Inject
    public BookmarksRepository() {
        // no-op
    }

    // assumes all entries are bookmarked
    public void retrieveAllBookmarks(final RetrieveCallback retrieveCallback) {
        SQLite.select()
                .from(SimpleArticle.class)
                .async()
                .queryListResultCallback((transaction, tResult) -> {
                    retrieveCallback.bookmarksRetrievedSuccessfully(tResult);
                });
    }

    public void deleteBookmark(final SimpleArticle simpleArticle, final DeleteCallback deleteCallback) {
        final DatabaseDefinition database = FlowManager.getDatabase(MYorkDatabase.class);
        final Transaction transaction = database.beginTransactionAsync(databaseWrapper -> simpleArticle.delete())
                .success(it -> {
                    deleteCallback.bookmarkDeletedSuccessfully(simpleArticle);
                })
                .error((it, error) -> deleteCallback.bookmarkDeleteFailed(simpleArticle, error))
                .build();
        transaction.execute();
    }

    public void insertBookmark(final SimpleArticle simpleArticle, final InsertCallback insertCallback) {
        final DatabaseDefinition database = FlowManager.getDatabase(MYorkDatabase.class);
        final Transaction transaction = database.beginTransactionAsync(databaseWrapper -> simpleArticle.insert())
                .success(it -> {
                    insertCallback.bookmarkInsertedSuccessfully(simpleArticle);
                })
                .error((it, error) -> insertCallback.bookmarkInsertFailed(simpleArticle, error))
                .build();
        transaction.execute();
    }

    public void isArticleBookmarked(final SimpleArticle simpleArticle, final ExistsCallback existsCallback) {
        SQLite.select()
                .from(SimpleArticle.class)
                .where(SimpleArticle_Table.mId.eq(simpleArticle.getId()))
                .async()
                .querySingleResultCallback((transaction, result) -> {
                    final boolean exists = result != null;
                    existsCallback.existsCheckSuccessfully(simpleArticle, exists);
                })
                .execute();
    }

    public interface RetrieveCallback {

        /**
         * Notify the bookmarks are retrieved.
         *
         * @param bookmarks - bookmarks
         */
        void bookmarksRetrievedSuccessfully(List<SimpleArticle> bookmarks);
    }

    public interface InsertCallback {

        /**
         * Notify the article is inserted succesfully.
         *
         * @param article - article
         */
        void bookmarkInsertedSuccessfully(SimpleArticle article);

        /**
         * Notify the article insertion failed.
         *
         * @param article - article
         * @param error - error
         */
        void bookmarkInsertFailed(SimpleArticle article, Throwable error);
    }

    public interface DeleteCallback {

        /**
         * Notify the article is deleted successfully.
         *
         * @param article - article
         */
        void bookmarkDeletedSuccessfully(SimpleArticle article);

        /**
         * Notify the article deletion failed.
         *
         * @param article - article
         * @param error - error
         */
        void bookmarkDeleteFailed(SimpleArticle article, Throwable error);
    }

    public interface ExistsCallback {

        /**
         * Notify that check is successful.
         *
         * @param article - article
         * @param isBookmared - flag indicating whether article is bookmarked
         */
        void existsCheckSuccessfully(SimpleArticle article, boolean isBookmarked);
    }
}