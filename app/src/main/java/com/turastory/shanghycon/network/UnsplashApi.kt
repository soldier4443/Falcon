package com.turastory.shanghycon.network

import com.turastory.shanghycon.vo.Feed
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by tura on 2018-09-15.
 */
interface UnsplashApi {
    companion object {
        const val baseUrl = "https://api.unsplash.com/"
    }

    @GET("photos?client_id=fc5f0d9deefb8ca5748fec70a99961e129ed6c1faf093d93baa065acc02bf9b5")
    fun getRecentPhotos(): Observable<List<Feed>>
}