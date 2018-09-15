package com.turastory.shanghycon

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener
import kotlinx.android.synthetic.main.activity_home.*


/**
 * Created by tura on 2018-09-15.
 */

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupFeeds()
    }

    private fun setupFeeds() {
        feedListView.apply {
            val linearLayoutManager = LinearLayoutManager(this@HomeActivity)
            val feedAdapter = FeedAdapter().apply { loadNewFeeds() }

            layoutManager = linearLayoutManager
            adapter = feedAdapter
            addOnScrollListener(object : InfiniteScrollListener(15, linearLayoutManager) {
                override fun onScrolledToEnd(firstVisibleItemPosition: Int) {
                    feedAdapter.loadNewFeeds()
                }
            })
//            addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
        }
    }
}