package com.turastory.shanghycon.network

import com.turastory.shanghycon.vo.AccountBalance
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by tura on 2018-09-15.
 */
interface HyconApi {
    companion object {
        const val baseUrl = "http://10.10.1.80:2442/api/v1/wallet/"
    }

    @GET("H2UevVaqYWo3Quw9oKPqU632VwP9oCcKB/balance")
    fun getBalance(): Observable<AccountBalance>
}