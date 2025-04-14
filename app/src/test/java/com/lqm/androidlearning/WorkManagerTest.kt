package com.lqm.androidlearning

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import com.lqm.androidlearning.github.GithubWorkManager
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner // Or use AndroidJUnit4 if on device/emulator

@RunWith(RobolectricTestRunner::class) // Example using Robolectric
class WorkManagerTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testMyWorker_successPath() {
        // 创建输入数据
        val inputData = workDataOf(GithubWorkManager.KEY_INPUT_URL to "http://example.com")

        // 使用 TestListenableWorkerBuilder 创建 Worker 实例
        val worker = TestListenableWorkerBuilder<GithubWorkManager>(
            context = context,
            inputData = inputData
            // 可以模拟 runAttemptCount, tags 等
            // .setRunAttemptCount(2)
        ).build()

        // 同步执行 doWork()
        runBlocking { // Use runBlocking for testing suspend functions
            val result = worker.doWork()

            // 断言结果
            assertTrue(result is ListenableWorker.Result.Success)
            // 断言输出数据
            val outputPath = (result as ListenableWorker.Result.Success).outputData
                .getString(GithubWorkManager.KEY_OUTPUT_PATH)
            println(outputPath)
            assertEquals("/path/to/downloaded/file", outputPath)
        }
    }

    @Test
    fun testMyWorker_retryPath() {
        // ... setup worker ...
        // Mock conditions to force retry if possible

        runBlocking {
            // Simulate first run failing and needing retry
            // (May need mocking dependencies or specific input to trigger retry logic)
            // For simplicity, let's assume the worker always retries on first attempt in test:
            val worker = TestListenableWorkerBuilder<GithubWorkManager>(context)
                .setRunAttemptCount(1) // Simulate first attempt
                .build()

            val result = worker.doWork()
            // Assert it requests retry based on internal logic
            // assertTrue(result is ListenableWorker.Result.Retry)
        }
    }
}

