package com.lqm.androidlearning

import android.app.Application
import com.lqm.androidlearning.room.BTCDataBase

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // 初始化数据库
        BTCDataBase.initDatabase(this)
    }
}