package com.lqm.androidlearning.network

import com.lqm.androidlearning.binance.model.BinanceTime
import retrofit2.http.GET
import retrofit2.http.Query

interface BinanceApiService {

    @GET("api/v3/ping")
    suspend fun getPing(): String?

    @GET("api/v3/time")
    suspend fun getTime(): BinanceTime?

    @GET("api/v3/klines")
    suspend fun getKLines(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("startTime") startTime: Long,
        @Query("endTime") endTime: Long,
    ): Array<Array<Double>>


}