package com.guowei.colorsapp.networking

import com.guowei.colorsapp.cache.SessionCache
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sessionCache: SessionCache
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        sessionCache.getToken()?.let {
            requestBuilder.addHeader("Authorization", it)
        }

        return chain.proceed(requestBuilder.build())
    }
}