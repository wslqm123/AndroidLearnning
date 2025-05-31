package com.lqm.androidlearning.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lqm.androidlearning.room.convert.DateConverter

// 假设你的 BTCDataBase.kt 文件内容如下：
@Database(entities = [FifteenMinBTC::class], version = 1, exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class BTCDataBase : RoomDatabase() { //  确保你的类名与文件名一致或根据需要调整

    abstract fun BTCDao(): BTCDao

    companion object {
        // @Volatile 注解确保 INSTANCE 变量的修改对所有线程立即可见。
        @Volatile
        private var INSTANCE: BTCDataBase? = null

        private const val DATABASE_NAME = "btc_database" // 定义数据库文件名

        fun initDatabase(context: Context): BTCDataBase {
            // 如果 INSTANCE 不为 null，则直接返回它。
            // 否则，同步创建一个新的数据库实例。
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, // 使用 applicationContext 防止内存泄漏
                    BTCDataBase::class.java,    // 你的数据库类
                    DATABASE_NAME               // 数据库文件名
                )
                    // 在这里可以添加数据库迁移策略、预填充数据等配置
                    // .addMigrations(MIGRATION_1_2, MIGRATION_2_3) // 示例：添加迁移
                    // .fallbackToDestructiveMigration() // 示例：如果迁移未找到，则销毁并重建数据库 (开发时常用)
                    // .addCallback(MyRoomDatabaseCallback()) // 示例：添加回调
                    // .allowMainThreadQueries() //  不推荐：允许在主线程执行查询 (仅用于非常简单的测试或特定情况)
                    .build()
                INSTANCE = instance
                // 返回实例
                instance
            }
        }

        fun getBTCDao(): BTCDao? {
            return INSTANCE?.BTCDao()
        }
    }
}