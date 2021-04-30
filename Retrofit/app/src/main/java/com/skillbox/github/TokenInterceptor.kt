package com.skillbox.github

import com.skillbox.github.data.AccessToken
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("Authorization", AccessToken.token)
            .build()

        return chain.proceed(newRequest)

    }
}