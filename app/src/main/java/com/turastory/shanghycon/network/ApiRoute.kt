package com.turastory.shanghycon.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by tura on 2018-09-15.
 */
private val build = Retrofit.Builder()
    .baseUrl(UnsplashApi.baseUrl)
    .client(OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build())
    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()

val UNSPLASH: UnsplashApi = build
    .create(UnsplashApi::class.java)
