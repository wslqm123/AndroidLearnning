package com.lqm.androidlearning.network

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val headers: Map<String, String>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()
        // 可以添加或修改现有的 Header
        //  .header("Authorization", "token") // 添加token
        //  .removeHeader("Cache-Control") // 如果需要移除某个 Header

        // 添加多个 Header
        headers.forEach { (name, value) ->
            requestBuilder.header(name, value)
        }

        val modifiedRequest = requestBuilder.build()
        return chain.proceed(modifiedRequest)
    }
}