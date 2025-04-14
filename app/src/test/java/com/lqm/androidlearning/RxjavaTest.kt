package com.lqm.androidlearning

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Test

class RxjavaTest {

    @Test
    fun testMapOperator_transformsDataCorrectly() {
        // Arrange
        val source = Observable.just(1, 2, 3)
        val testObserver = TestObserver<String>() // 创建 TestObserver

        // Act
        source
            .map {
                "Value: $it"
            }
            // .subscribeOn(Schedulers.trampoline()) // 可选，如果原链没有 Schedulers
            // .observeOn(Schedulers.trampoline()) // 可选，如果原链没有 Schedulers
            .subscribe(testObserver) // 订阅 TestObserver

        // Assert
        testObserver.assertComplete() // 检查是否完成
        testObserver.assertNoErrors() // 检查是否没有错误
        testObserver.assertValueCount(3) // 检查发射了多少个值
        testObserver.assertValues("Value: 1", "Value: 2", "Value: 3") // 检查发射的具体值和顺序
    }

    @Test
    fun testErrorHandling_onErrorReturnItem() {
        val source = Observable.error<Int>(RuntimeException("Test Error"))
        val testObserver = TestObserver<Int>()

        source
            .onErrorReturnItem(99)
            .subscribe(testObserver)

        testObserver.assertComplete()
        testObserver.assertNoErrors() // 因为错误被处理了
        testObserver.assertValue(99) // 检查是否返回了默认值
    }
}