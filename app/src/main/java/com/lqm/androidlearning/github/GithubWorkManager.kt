package com.lqm.androidlearning.github

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.lqm.androidlearning.common.LogUtil
import kotlinx.coroutines.delay

class GithubWorkManager(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    companion object {
        const val KEY_INPUT_URL = "input_url"
        const val KEY_OUTPUT_PATH = "output_path"
        const val TAG = "GithubWorkManagerTag" // 用于识别任务
    }

    // doWork() 是挂起函数，在这里执行后台任务
    override suspend fun doWork(): Result {
        val inputUrl = inputData.getString(KEY_INPUT_URL)
        LogUtil.d("Worker started for URL: $inputUrl")

        return try {
            // 模拟耗时操作，例如网络请求或文件处理
            delay(3000) // 使用 kotlinx.coroutines.delay
            // 假设任务成功，并返回结果数据
            val outputPath = "/path/to/downloaded/file"
            val outputData = workDataOf(KEY_OUTPUT_PATH to outputPath) // 创建输出 Data
            LogUtil.d("Worker finished successfully. Output: $outputPath")
            Result.success(outputData) // 返回成功及输出数据

        } catch (e: Exception) {
            LogUtil.d("Worker failed: ${e.message}")
            if (runAttemptCount < 3) { // runAttemptCount 是内置属性
                LogUtil.d("Retrying worker...")
                Result.retry() // 请求重试
            } else {
                LogUtil.d("Worker permanently failed after $runAttemptCount attempts.")
                Result.failure() // 标记为最终失败
            }
        }
    }

}

