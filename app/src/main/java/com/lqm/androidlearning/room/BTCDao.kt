package com.lqm.androidlearning.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BTCDao {
    @Query("SELECT * FROM BTC_15_min ORDER BY startTime ASC")
    fun getAllFlow(): Flow<List<FifteenMinBTC>>

    @Query("SELECT * FROM BTC_15_min WHERE startTime IN (:times)")
    suspend fun loadByTimes(times: LongArray): List<FifteenMinBTC>

    @Query("SELECT * FROM BTC_15_min WHERE startTime BETWEEN :startTime AND :endTime ORDER BY startTime ASC")
    suspend fun loadBySection(startTime: Long, endTime: Long): List<FifteenMinBTC>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 冲突策略：替换旧数据
    suspend fun insertAll(list: List<FifteenMinBTC>)

    @Delete
    suspend fun delete(data: FifteenMinBTC)

    @Query("DELETE FROM BTC_15_min")
    suspend fun deleteAllData()
}