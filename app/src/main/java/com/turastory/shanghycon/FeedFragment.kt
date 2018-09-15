package com.turastory.shanghycon

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener
import kotlinx.android.synthetic.main.fragment_feed.*

/**
 * Created by tura on 2018-09-15.
 */
class FeedFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFeeds()
    }

    private fun setupFeeds() {
        feedListView.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            val feedAdapter = FeedAdapter().apply { loadNewFeeds() }

            layoutManager = linearLayoutManager
            adapter = feedAdapter
            addOnScrollListener(object : InfiniteScrollListener(15, linearLayoutManager) {
                override fun onScrolledToEnd(firstVisibleItemPosition: Int) {
                    feedAdapter.loadNewFeeds()
                }
            })
        }
    }
}