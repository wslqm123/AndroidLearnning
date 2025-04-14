package com.lqm.androidlearning.common

import android.util.Log

class LogUtil {

    companion object {
        fun d(msg: String) {
            Log.d(LOG_TAG, msg)
        }


        fun i(msg: String) {
            Log.i(LOG_TAG, msg)
        }

        fun e(msg: String, e: Throwable? = null) {
            Log.e(LOG_TAG, msg, e)
        }

        fun dThread(msg: String) {
            Log.d(LOG_TAG, "$msg, thread: ${Thread.currentThread().name}")
        }

        fun dStack(msg: String) {
            val stackTrace = Log.getStackTraceString(Throwable())
            Log.d(LOG_TAG, "$msg\n$stackTrace")
        }
    }
}