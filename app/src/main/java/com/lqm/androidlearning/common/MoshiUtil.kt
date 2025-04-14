package com.lqm.androidlearning.common

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MoshiUtil {


    companion object {
        private var moshi: Moshi? = null

        fun getMoshi(): Moshi {
            return moshi ?: Moshi.Builder().addLast(KotlinJsonAdapterFactory()) // 添加 Kotlin 支持
                // .add(YourCustomAdapter)  // 如果有自定义适配器，在此添加
                .build().apply {
                    moshi = this
                }
        }
    }


    // 序列化对象为 JSON 字符串
    fun <T> toJson(data: T): String? {
        val adapter = getMoshi().adapter<T>(data!!::class.java)
        return adapter.toJson(data)
    }

    // 反序列化 JSON 字符串为对象
    fun <T : Any> fromJson(json: String, clazz: Class<T>): T? {
        val adapter = getMoshi().adapter(clazz)
        return adapter.fromJson(json)
    }

    // 序列化 List
    fun <T> toJsonList(users: List<T>, clazz: Class<T>): String? {
        val type = Types.newParameterizedType(List::class.java, clazz)
        val adapter = getMoshi().adapter<List<T>>(type)
        return adapter.toJson(users)
    }

    // 反序列化 List
    fun <T> fromJsonList(json: String, clazz: Class<T>): List<T>? {
        val type = Types.newParameterizedType(List::class.java, clazz)
        val adapter = getMoshi().adapter<List<T>>(type)
        return adapter.fromJson(json)
    }

}