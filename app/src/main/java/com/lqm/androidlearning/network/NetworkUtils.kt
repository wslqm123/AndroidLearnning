package com.lqm.androidlearning.network

import com.lqm.androidlearning.common.LogUtil
import com.lqm.androidlearning.common.MoshiUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class NetworkUtils {

    companion object {
        private var githubApi: GithubApiService? = null
        private var binanceApi: BinanceApiService? = null

        fun <T> createApi(clazz: Class<T>, url: String = "https://api.github.com/"): T {
            return buildRetrofit(url).create<T>(clazz)
        }

        fun getGithubApi(): GithubApiService {
            return githubApi ?: createApi(GithubApiService::class.java).apply {
                githubApi = this
            }
        }

        fun getBinanceApi(): BinanceApiService {
            return binanceApi ?: createApi(
                BinanceApiService::class.java,
                "https://api.binance.com/"
            ).apply {
                binanceApi = this
            }
        }

        private fun buildRetrofit(url: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(url)
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