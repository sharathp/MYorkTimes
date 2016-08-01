package com.sharathp.myorktimes.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharathp.myorktimes.MYorkTimesApplication;
import com.sharathp.myorktimes.R;
import com.sharathp.myorktimes.databinding.FragmentBookmarksListBinding;
import com.sharathp.myorktimes.models.SimpleArticle;
import com.sharathp.myorktimes.repositories.BookmarksRepository;
import com.sharathp.myorktimes.util.NavigationUtils;
import com.sharathp.myorktimes.views.adapters.ArticleItemCallback;
import com.sharathp.myorktimes.views.adapters.BookmarksListAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class BookmarkListFragment extends Fragment implements ArticleItemCallback {

    @Inject
    BookmarksRepository mBookmarksRepository;
    private FragmentBookmarksListBinding mBinding;
    private BookmarksListAdapter mBookmarksListAdapter;

    public static Fragment createInstance() {
        return new BookmarkListFragment();
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MYorkTimesApplication.from(getActivity()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup parent, final Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmarks_list, parent, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetVisibility();
        // retrieve bookmarks
        mBookmarksRepository.retrieveAllBookmarks(bookmarks -> addBookmarks(bookmarks));
    }

    @Override
    public void onPause() {
        super.onPause();
        // clear bookmarks, next resume will update the bookmarks
        mBookmarksListAdapter.setBookmarks(null);
    }

    private void addBookmarks(final List<SimpleArticle> bookmarks) {
        mBinding.pbAllBookmarksLoadingBar.setVisibility(View.GONE);

        if (bookmarks == null || bookmarks.isEmpty()) {
            mBinding.rvBookmarks.setVisibility(View.GONE);
            mBinding.flMessageContainer.setVisibility(View.VISIBLE);
            mBinding.tvBookmarksMessage.setVisibility(View.VISIBLE);
        } else {
            mBinding.rvBookmarks.setVisibility(View.VISIBLE);
            mBinding.flMessageContainer.setVisibility(View.GONE);
            mBinding.tvBookmarksMessage.setVisibility(View.GONE);
            mBookmarksListAdapter.addBookmarks(bookmarks);
        }
    }

    private void resetVisibility() {
        mBinding.rvBookmarks.setVisibility(View.GONE);
        mBinding.flMessageContainer.setVisibility(View.VISIBLE);
        mBinding.pbAllBookmarksLoadingBar.setVisibility(View.VISIBLE);
        mBinding.tvBookmarksMessage.setVisibility(View.GONE);
    }

    private void initViews() {
        final RecyclerView recyclerView = mBinding.rvBookmarks;
        mBookmarksListAdapter = new BookmarksListAdapter(new ArrayList<>(), this);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setAdapter(mBookmarksListAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onArticleSelected(final SimpleArticle article) {
        startActivity(NavigationUtils.getDetailActivityIntent(getActivity(), article));
    }
}