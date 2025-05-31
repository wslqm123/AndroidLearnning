package com.lqm.androidlearning.room.convert

import androidx.room.TypeConverter
import java.util.Date

object DateConverter { //  在 Kotlin 中，通常使用 object (单例) 或 class
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}