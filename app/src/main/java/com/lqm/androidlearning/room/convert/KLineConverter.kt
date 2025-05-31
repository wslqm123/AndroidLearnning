package com.lqm.androidlearning.room.convert

import androidx.room.TypeConverter
import com.lqm.androidlearning.room.FifteenMinBTC

object KLineConverter {
    @TypeConverter
    fun toFifthMinBtc(array: Array<Double>?): FifteenMinBTC? {
        if (array.isNullOrEmpty() || array.size < 7) {
            return null
        }
        return FifteenMinBTC(
            array[0].toLong(), array[6].toLong(), array[1], array[4], array[2], array[3]
        )

    }

    @TypeConverter
    fun toArray(data: FifteenMinBTC?): Array<Double>? {
        if (data == null) {
            return null
        }
        return arrayOf(
            data.startTime.toDouble(),
            data.open,
            data.high,
            data.low,
            data.close,
            0.0,
            data.endTime.toDouble()
        )
    }
}