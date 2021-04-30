package com.skillbox.github.data

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.skillbox.github.GitHubApi
import com.skillbox.github.TokenInterceptor

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object Network {

    val networkFlipperPlugin = NetworkFlipperPlugin()
    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(TokenInterceptor())
        .addNetworkInterceptor(FlipperOkhttpInterceptor(networkFlipperPlugin))
        .build()


    private val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl("https://api.github.com/")
        .build()

    val gitHubApi: GitHubApi
        get() = retrofit.create()

}