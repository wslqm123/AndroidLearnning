package com.lqm.klines.model

data class KLineItem(
    val startTime: Long, // K线开始时间
    val endTime: Long, // K线结束时间
    val open: Double,  // 开盘价
    val close: Double, // 收盘价
    val high: Double, // 最高价
    val low: Double, // 最低价
    val volume: String? = null, // 成交量
    val turnover: String? = null, // 成交额
    val transactions: Long? = null // 成交笔数
)

data class KLineChartData(
    val symbol: String, // 交易对
    val interval: KLineInterval, // K线周期
    val data: List<KLineItem>,
    val visibleStartTime: Long? = null, //  当前可见区域的开始时间 (用于缩放和平移后确定范围)
    val visibleEndTime: Long? = null, //  当前可见区域的结束时间
    var maxVisibleItems: Int = 50, // 最大可见项数，最小值为15
    var offsetSize: Float = -10f, // 默认偏移条目数（初始化时距离右边界的K线数）
    val visibleItemStartIndex: Int = 0,     // 可见区域第一个数据点在 data 列表中的索引
    val visibleItemEndIndex: Int = data.size - 1, // 可见区域最后一个数据点在 data 列表中的索引
//    val currentDisplayHighPrice: Double,  // 当前显示K线区域的最高价
//    val currentDisplayLowPrice: Double,   // 当前显示K线区域的最低价
    val currentDisplayMaxVolume: Double? = null, // 当前显示区域的最大成交量 (如果显示成交量图)
    val overallStartTime: Long? = data.firstOrNull()?.startTime,
    val overallEndTime: Long? = data.lastOrNull()?.startTime, // 假设 startTime 是递增的
    val overallHighPrice: Double? = null, //  整个数据集的最高价 (所有已加载数据)
    val overallLowPrice: Double? = null,  //  整个数据集的最低价 (所有已加载数据)
    val isLoadingMore: Boolean = false,      // 是否正在加载更多历史数据
    var hasMoreData: Boolean = true,         // 是否还有更多历史数据可以加载
    val selectedIndicatorTypes: List<String> = listOf("MA", "VOLUME") //  当前用户选择显示的指标类
)

enum class KLineInterval(val description: String) {
    MIN_5("5m"),
    MIN_15("15m"),
    MIN_30("30m"),
    HOUR_1("1h"),
    HOUR_4("4h"),
    HOUR_12("12h"),
    DAY_1("1d"),
    DAY_2("2d"),
    DAY_7("7d"),
}

fun intervalToMills(interval: String): Long {
    return when (interval) {
        KLineInterval.MIN_5.description -> 300000L
        KLineInterval.MIN_15.description -> 900000L
        KLineInterval.MIN_30.description -> 1800000L
        KLineInterval.HOUR_1.description -> 3600000L
        KLineInterval.HOUR_4.description -> 14400000L
        KLineInterval.HOUR_12.description -> 43200000L
        KLineInterval.DAY_1.description -> 86400000L
        KLineInterval.DAY_2.description -> 172800000L
        KLineInterval.DAY_7.description -> 604800000L
        else -> 3600000L
    }
}

fun arrayToKLine(item: Array<Double>): KLineItem? {
    if (item.isEmpty() || item.size < 7) {
        return null
    }
    return KLineItem(item[0].toLong(), item[6].toLong(), item[1], item[4], item[2], item[3])
}


fun matrixToKLineChar(
    symbol: String,
    interval: KLineInterval,
    data: Array<Array<Double>>,
    hasMore: Boolean
): KLineChartData? {
    if (data.isEmpty()) {
        return null
    }
    return KLineChartData(
        symbol,
        interval,
        data.mapNotNull { arrayToKLine(it) },
        visibleItemStartIndex = data.size - 50,
        visibleItemEndIndex = data.size - 1
    )
}