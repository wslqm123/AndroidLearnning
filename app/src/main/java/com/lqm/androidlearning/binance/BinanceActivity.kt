package com.lqm.androidlearning.binance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lqm.androidlearning.binance.viewModel.BinanceViewModel
import com.lqm.androidlearning.common.UiState
import com.lqm.androidlearning.ui.theme.AndroidLearningTheme
import com.lqm.klines.KLineChartView
import com.lqm.klines.model.KLineInterval
import com.lqm.klines.model.matrixToKLineChar

class BinanceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(this)[BinanceViewModel::class.java]
        setContent {
            AndroidLearningTheme {
                Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->
                    KLinesScreen(
                        modifier = Modifier.Companion.padding(innerPadding),
                        viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun KLinesScreen(
    modifier: Modifier = Modifier,
    viewModel: BinanceViewModel = BinanceViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val kLines by viewModel.kLinesInfo.collectAsState()
    val hasMore by viewModel.hasMoreValue.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        // 根据 uiState 显示不同的内容
        when (uiState) {
            UiState.Idle -> {
                // 初始状态，可能显示一个按钮或提示用户进行搜索
                SearchKLinesScreen(viewModel)
            }

            UiState.Loading -> {
                // 显示加载指示器
                LoadingView()
            }

            UiState.Success -> {
                // 显示用户信息
                if (kLines.isNotEmpty()) {
                    KLinesDetail(viewModel, kLines, hasMore)
                } else {
                    //  处理 userInfo 为 null 的情况 (例如：显示一个空状态)
                    ErrorView(viewModel)
                }
            }

            UiState.Fail -> {
                // 显示错误信息
                ErrorView(viewModel)
            }
        }
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
//        viewModel.getEvents()
        viewModel.getServiceTime()
    }
}

@Composable
fun LoadingView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(viewModel: BinanceViewModel) {
    val symbol by viewModel.symbolValue.collectAsStateWithLifecycle()
    val interval by viewModel.intervalValue.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Button({
                viewModel.getKLinesInfo(symbol, interval)
            }) {
                Text("重试")
            }
        }
    }
}

@Composable
fun KLinesDetail(
    viewModel: BinanceViewModel = BinanceViewModel(),
    kLines: Array<Array<Double>>,
    hasMore: Boolean
) {
    //  显示K线的详细信息
    matrixToKLineChar("BTCUSDT", KLineInterval.MIN_15, kLines, hasMore)?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            it.hasMoreData = hasMore
            KLineChartView(it, { forward ->
                if (forward) {
                    viewModel.getLastData()
                } else {
                    viewModel.getPreData()
                }

            })
        }
    }
//    Text(text = "${kLines.size}")
}


@Composable
fun SearchKLinesScreen(viewModel: BinanceViewModel) {
    val symbol by viewModel.symbolValue.collectAsStateWithLifecycle()
    val interval by viewModel.intervalValue.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier.weight(1f),
                value = symbol, onValueChange = {
                    viewModel.onSymbolChanged(it)
                },
                placeholder = {
                    Text(text = "Please input username")
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                )
            )
            Button(
                onClick = {
                    viewModel.getKLinesInfo(
                        symbol = symbol,
                        interval = interval,
                        startTime = 1747868400000,
                        endTime = 1747954800000
                    )
                }, modifier = Modifier
                    .padding(start = 10.dp)
                    .width(90.dp)
                    .height(48.dp)
            ) {
                Text("搜索")
            }
        }

    }
}
