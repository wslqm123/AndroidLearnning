package com.lqm.androidlearning.binance

import com.lqm.androidlearning.common.FifteenMinMillis
import com.lqm.androidlearning.common.LogUtil
import com.lqm.androidlearning.network.NetworkUtils
import com.lqm.androidlearning.room.BTCDataBase
import com.lqm.androidlearning.room.FifteenMinBTC
import kotlinx.coroutines.flow.flow

class BinanceRepository() {

    private val TAG = "BinanceRepository"
    private val btcDao by lazy { BTCDataBase.getBTCDao() }

    suspend fun getPing() = flow {
        val result = NetworkUtils.getBinanceApi().getPing()
        emit(result)
    }

    suspend fun getServiceTime() = flow {
        val result = NetworkUtils.getBinanceApi().getTime()
        emit(result)
    }

    /**
     * @param symbol 交易对
     * @param interval K线间隔（5m、15m、1h、2h、1d...）
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    suspend fun getKLinesFlow(
        symbol: String,
        interval: String,
        startTime: Long,
        endTime: Long,
    ) = flow {
        var result: Array<Array<Double>>? = loadDataFromRoom(symbol, interval, startTime, endTime)
        if (result.isNullOrEmpty()) {
            LogUtil.d("Load BTCUSDT data from Binance API")
            result = NetworkUtils.getBinanceApi()
                .getKLines(symbol, interval, startTime, endTime)
            saveDataToRoom(symbol, interval, result)

        }
        emit(result)
    }

    private suspend fun loadDataFromRoom(
        symbol: String,
        interval: String,
        startTime: Long,
        endTime: Long
    ): Array<Array<Double>>? {
        if (symbol != "BTCUSDT" || interval != "15m") {
            return null
        }
        LogUtil.d(TAG, "Load BTCUSDT data from Room")
        val expectSize = (endTime - startTime) / FifteenMinMillis - 2
        val dataList = btcDao?.loadBySection(startTime, endTime)
        if (dataList.isNullOrEmpty() || dataList.size < expectSize) {
            return null
        }
        return dataList.map { data ->
            arrayOf(
                data.startTime.toDouble(),
                data.open,
                data.high,
                data.low,
                data.close,
                0.0,
                data.endTime.toDouble()
            )
        }.toTypedArray()
    }

    private suspend fun saveDataToRoom(
        symbol: String,
        interval: String, data: Array<Array<Double>>
    ) {
        if (symbol == "BTCUSDT" && interval == "15m" && data.isNotEmpty()) {
            LogUtil.d(TAG, "Save BTCUSDT data to Room")
            val saveData = data.mapNotNull { array ->
                if (array.size > 7) {
                    FifteenMinBTC(
                        array[0].toLong(), array[6].toLong(), array[1], array[4], array[2], array[3]
                    )
                } else {
                    null
                }
            }
            btcDao?.insertAll(saveData)
        }
    }


}