package com.lqm.androidlearning.network

import com.lqm.androidlearning.common.LogUtil
import com.lqm.androidlearning.common.MoshiUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class NetworkUtils {

    companion object {
        private var api: GithubApiService? = null

        fun <T> createApi(clazz: Class<T>): T {
            return buildRetrofit().create<T>(clazz)
        }

        fun getDefaultApi(): GithubApiService {
            return api ?: createApi(GithubApiService::class.java).apply {
                api = this
            }
        }

        private fun buildRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(getClient())
                .addConverterFactory(MoshiConverterFactory.create(MoshiUtil.getMoshi())) // 使用 Moshi 实例
                .build()
        }

        private fun getClient(): OkHttpClient {
            val logging = HttpLoggingInterceptor {
                LogUtil.d(it)
            }.apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
            val customHeaders = mapOf(
                "Accept" to "application/vnd.github+json",
                "User-Agent" to "LearnAndroidDevelop"
            )
            val headerInterceptor = HeaderInterceptor(customHeaders)
            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(headerInterceptor)
                .build()
        }


    }


}