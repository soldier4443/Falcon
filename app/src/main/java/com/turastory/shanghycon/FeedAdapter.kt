package com.turastory.shanghycon

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.turastory.shanghycon.network.UNSPLASH
import com.turastory.shanghycon.vo.Feed
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_feed.view.*

/**
 * Created by tura on 2018-09-15.
 */

class FeedAdapter : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    private val feeds = mutableListOf<Feed>()

    fun loadNewFeeds() {
        UNSPLASH.getRecentPhotos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                // TODO: Show Loading?
            }
            .doOnComplete {
                // TODO: Hide Loading?
            }
            .doOnError {
                // TODO: Error handling
            }
            .subscribe { newFeeds ->
                val start = feeds.size
                val count = newFeeds.size

                feeds.addAll(newFeeds)
                notifyItemRangeInserted(start, count)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_feed))
    }

    override fun getItemCount() = feeds.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(feeds[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(feed: Feed) {
            feed.description?.let {
                itemView.titleText.text = it
            } ?: let {
                itemView.titleText.text = "No Description"
            }

            GlideApp.with(itemView)
                .load(feed.urls.thumb)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(itemView.imageView)
        }
    }
}