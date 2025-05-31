package com.lqm.androidlearning.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BTC_15_min")
data class FifteenMinBTC(
    @PrimaryKey() val startTime: Long,
    @ColumnInfo() val endTime: Long,
    @ColumnInfo() val open: Double,
    @ColumnInfo() val close: Double,
    @ColumnInfo() val high: Double,
    @ColumnInfo() val low: Double
)
