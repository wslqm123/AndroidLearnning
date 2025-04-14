package com.lqm.androidlearning.thirdlibrary.viewmodel

import androidx.lifecycle.ViewModel
import com.lqm.androidlearning.common.LogUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RxjavaViewModel : ViewModel() {

    private var composite: CompositeDisposable? = null

    fun just() {
        if (composite == null) {
            composite = CompositeDisposable()
        }
        Observable.just("Rxjava just")
            .map { "map - $it" }
            .subscribe({
                LogUtil.d("onNext: $it")
            }, {
                LogUtil.e("onError: ${it.message}", it)
            }, {
                LogUtil.d("onComplete")
            }).addTo(composite!!)
    }

    fun emit() {
        if (composite == null) {
            composite = CompositeDisposable()
        }
        Observable.create {
            try {
                LogUtil.dThread("emit data")
                it.onNext("emit Data 1")
                // 模拟一些耗时操作
                Thread.sleep(2000) // 阻塞线程，仅作示例，实际应使用 Schedulers
                it.onNext("emit Data 2")
                it.onComplete() // 必须调用 onComplete 或 onError
            } catch (e: Exception) {
                if (!it.isDisposed) { // 发射前检查是否已取消订阅
                    it.onError(e) // 发生错误时调用
                }
            }
        }.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtil.dThread("onNext: $it")
            }, {
                LogUtil.e("onError: ${it.message}", it)
            }, {
                LogUtil.dThread("onComplete")
            }, composite!!)
    }

    fun map() {
        if (composite == null) {
            composite = CompositeDisposable()
        }
        val observable1 = Observable.fromIterable<Int>(1..5).subscribeOn(Schedulers.computation())
        val handle: (id: Int) -> Observable<String> = { it ->
            Observable<String>.just("$it").delay(1000 - it * 200L, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
        }
        observable1
            .flatMap {
//            .concatMap { //  可以保证顺序
//            .switchMap { //  收到新事件会取消还在处理的旧事件
                LogUtil.dThread("operate map: $it")
                handle(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtil.dThread("onNext: $it")
            }, {
                LogUtil.dThread("onError: ${it.message}")
            }, {
                LogUtil.dThread("onComplete")
            }, composite!!)
    }

    fun merge() {
        if (composite == null) {
            composite = CompositeDisposable()
        }
        val observable1 = Observable.create<Char> {
            for (i in 0..3) {
                it.onNext('A' + i)
                Thread.sleep(i * 100L)
            }
        }.subscribeOn(Schedulers.io())

        val observable2 = Observable.create<Int> {
            for (i in 0..4) { // 多一个
                it.onNext(i)
                Thread.sleep(i * 200L)
            }
        }.subscribeOn(Schedulers.io())
        Observable
            .merge(observable1, observable2)
//            .zip(observable1, observable2, { t1, t2 ->
//                "${t1}-${t2}" // 合并数据，最终数量按少的为准
//            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtil.d("onNext: $it")
            }, {
                LogUtil.e("onError: ${it.message}", it)
            }, {
                LogUtil.d("onComplete")
            }, composite!!)

    }

    override fun onCleared() {
        super.onCleared()
        LogUtil.d("RxjavaViewModel onCleared")
        composite?.let {
            if (!it.isDisposed) {
                LogUtil.d("RxjavaViewModel onCleared dispose")
                it.dispose()
                it.clear()
            }
        }
    }
}