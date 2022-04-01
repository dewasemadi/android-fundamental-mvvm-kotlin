package com.dicoding.githubuser.data.remote.retrofit

import okhttp3.*

class InterceptorConfig(private val ACCESS_TOKEN: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "token $ACCESS_TOKEN")
            .build()
        return chain.proceed(request)
    }
}