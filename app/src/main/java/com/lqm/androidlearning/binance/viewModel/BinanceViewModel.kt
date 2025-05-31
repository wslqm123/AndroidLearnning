package com.lqm.androidlearning.binance.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lqm.androidlearning.binance.BinanceRepository
import com.lqm.androidlearning.common.DayMillis
import com.lqm.androidlearning.common.LogUtil
import com.lqm.androidlearning.common.UiState
import com.lqm.klines.model.intervalToMills
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BinanceViewModel : ViewModel() {

    private val repository = BinanceRepository()

    // 私有可变的 StateFlow
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)

    // 公开只读的 StateFlow
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // K线 StateFlow
    private val _kLinesInfo = MutableStateFlow<Array<Array<Double>>>(arrayOf())
    val kLinesInfo: StateFlow<Array<Array<Double>>> = _kLinesInfo.asStateFlow()

    private val _serviceTime = MutableStateFlow(0L)
    val serviceTime: StateFlow<Long> = _serviceTime.asStateFlow()

    private val _symbolValue = MutableStateFlow("BTCUSDT")
    val symbolValue: StateFlow<String> = _symbolValue.asStateFlow()

    private val _interval = MutableStateFlow("15m")
    val intervalValue: StateFlow<String> = _interval.asStateFlow()

    private val _hasMore = MutableStateFlow(true)
    val hasMoreValue: StateFlow<Boolean> = _hasMore.asStateFlow()
    private var isLoadingMore = false

    fun onSymbolChanged(symbol: String) {
        _symbolValue.value = symbol
    }

    private var curStartTime = System.currentTimeMillis() - DayMillis
    private var curEndTime = System.currentTimeMillis()

    fun getServiceTime() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getServiceTime().catch {
                LogUtil.e("get service time fail", it)
            }.collectLatest {
                LogUtil.d("get service time success: $it")
            }
        }
    }


    fun getPreData() {
        if (isLoadingMore) {
            return
        }
        val timeRange = calcTimeRange(false)
        getKLinesInfo(symbolValue.value, intervalValue.value, timeRange.first, timeRange.second, 0)
    }

    fun getLastData() {
        if (isLoadingMore) {
            return
        }
        val timeRange = calcTimeRange(true)
        getKLinesInfo(
            symbolValue.value,
            intervalValue.value,
            timeRange.first,
            timeRange.second,
            _kLinesInfo.value.size
        )
    }

    fun getKLinesInfo(
        symbol: String,
        interval: String,
        startTime: Long = curStartTime,
        endTime: Long = curEndTime,
        insertPos: Int = -1,
    ) {
        LogUtil.d("start get kLines data symbol: $symbol, interval: $interval, startTime: $startTime, endTime: $endTime")
        viewModelScope.launch(Dispatchers.IO) {
            if (insertPos == -1) {
                _uiState.value = UiState.Loading
            } else {
                isLoadingMore = true
            }
            repository.getKLinesFlow(symbol, interval, startTime, endTime).catch {
                LogUtil.e("get kLines info fail", it)
                if (insertPos == -1) {
                    _uiState.value = UiState.Fail
                } else {
                    isLoadingMore = false
                }
            }.collectLatest {
                if (insertPos == -1) {
                    curStartTime = it.first()[0].toLong()
                    curEndTime = it.last()[0].toLong()
                    _kLinesInfo.value = it
                } else if (insertPos == 0) {
                    curStartTime = it.first()[0].toLong()
                    _kLinesInfo.value = it + _kLinesInfo.value
                } else {
                    curEndTime = it.last()[0].toLong()
                    _kLinesInfo.value = _kLinesInfo.value + it
                }
                LogUtil.d("get kLines info success, data size: ${_kLinesInfo.value.size}")
                if (insertPos == -1) {
                    _uiState.value = UiState.Success
                } else {
                    isLoadingMore = false
                }
            }
        }
    }

    private fun calcTimeRange(forward: Boolean): Pair<Long, Long> {
        val timeRange = intervalToMills(intervalValue.value) * 200 // 默认取200个数据
        var startTime: Long
        var endTime: Long
        if (forward) {
            startTime = curEndTime
            endTime = startTime + timeRange
        } else {
            startTime = curStartTime - timeRange
            endTime = curStartTime
        }
        return Pair<Long, Long>(startTime, endTime)
    }

}
