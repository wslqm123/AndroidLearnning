package com.lqm.androidlearning.github.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.lqm.androidlearning.common.LogUtil
import com.lqm.androidlearning.common.UiState
import com.lqm.androidlearning.data.GithubUser
import com.lqm.androidlearning.github.GithubRepository
import com.lqm.androidlearning.github.GithubWorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class GithubUserViewModel : ViewModel() {

    private val repository = GithubRepository()

    // 私有可变的 StateFlow
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)

    // 公开只读的 StateFlow
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // 新增：存储用户信息的 StateFlow
    private val _userInfo = MutableStateFlow<GithubUser?>(null)
    val userInfo: StateFlow<GithubUser?> = _userInfo.asStateFlow()

    private val _userNameValue = MutableStateFlow("")
    val userNameValue: StateFlow<String> = _userNameValue.asStateFlow()

    fun getUserInfo(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UiState.Loading
            repository.getUserFlow(userName).catch {
                LogUtil.e("get user info fail", it)
                _uiState.value = UiState.Fail
            }.collectLatest {
                LogUtil.d("get user info success")
                _userInfo.value = it
                _uiState.value = UiState.Success
            }
        }
    }

    fun getEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getEventsFlow().catch {
                LogUtil.e("get events fail", it)
            }.collectLatest {
                LogUtil.d("get events success: $it")
            }
        }
    }

    fun onUserNameValueChanged(userName: String) {
        _userNameValue.value = userName
    }

    fun startWork(context: Context) {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED) // 需要非计费网络 (如 Wi-Fi)
            .setRequiresCharging(true) // 需要设备正在充电
            .setRequiresBatteryNotLow(true) // 需要电池电量不低
            // .setRequiresDeviceIdle(true) // 需要设备处于空闲状态 (API 23+)
            // .setRequiresStorageNotLow(true) // 需要存储空间不低
            .build()

        // --- 输入数据 ---
        val inputData: Data = workDataOf(
            GithubWorkManager.KEY_INPUT_URL to "https://api.github.com/events"
            // 可以添加更多键值对...
        )

        // --- 周期性任务 ---
        // 注意：最小重复间隔是 15 分钟 (MIN_PERIODIC_INTERVAL_MILLIS)
        val myPeriodicWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<GithubWorkManager>(12, TimeUnit.HOURS) // 每12小时执行一次
                // 也可以设置 Flex 时间 (执行窗口的最后一段时间)
                 .setInitialDelay(10, TimeUnit.MINUTES) // 首次执行前延迟10分钟
                .setInputData(inputData)
//                .setConstraints(constraints) // 将约束应用到请求
                .addTag(GithubWorkManager.TAG) // 添加标签
                .build()

        // 获取 WorkManager 实例 (通常在 Activity, Fragment, Service 或 Application Context 中)
        val workManager = WorkManager.getInstance(context)

        // 将单个任务加入队列
        workManager.enqueue(myPeriodicWorkRequest)

        // 获取任务 ID，用于后续观察或取消
        val workId = myPeriodicWorkRequest.id
        LogUtil.d("Enqueued work with ID: $workId")

        // --- 使用 Kotlin Flow (需要 androidx.work:work-runtime-ktx 依赖) ---
        // 在 CoroutineScope 中收集
        viewModelScope.launch { // 在 Activity/Fragment 中
            workManager.getWorkInfoByIdFlow(workId).collect { workInfo ->
                // 处理 workInfo，逻辑同上
                LogUtil.d("Work $workId State (Flow): ${workInfo?.state}")
            }
        }

    }
}
