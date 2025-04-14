package com.lqm.androidlearning.github

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.lqm.androidlearning.common.UiState
import com.lqm.androidlearning.data.GithubUser
import com.lqm.androidlearning.github.viewmodel.GithubUserViewModel
import com.lqm.androidlearning.ui.theme.AndroidLearningTheme

class GithubUserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(this)[GithubUserViewModel::class.java]
        setContent {
            AndroidLearningTheme {
                Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->
                    UserInfoScreen(
                        modifier = Modifier.Companion.padding(innerPadding),
                        viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun UserInfoScreen(
    modifier: Modifier = Modifier,
    viewModel: GithubUserViewModel = GithubUserViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val userInfo by viewModel.userInfo.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        // 根据 uiState 显示不同的内容
        when (uiState) {
            UiState.Idle -> {
                // 初始状态，可能显示一个按钮或提示用户进行搜索
                SearchUserScreen(viewModel)
            }

            UiState.Loading -> {
                // 显示加载指示器
                LoadingView()
            }

            UiState.Success -> {
                // 显示用户信息
                if (userInfo != null) {
                    UserDetail(userInfo = userInfo!!)
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
        viewModel.startWork(context)
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
fun ErrorView(viewModel: GithubUserViewModel) {
    val username by viewModel.userNameValue.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Button({
                viewModel.getUserInfo(username)
            }) {
                Text("重试")
            }
        }
    }
}

@Composable
fun UserDetail(userInfo: GithubUser) {
    //  显示用户的详细信息，例如：
    Text(text = userInfo.toString())
}

@Composable
fun SearchUserScreen(viewModel: GithubUserViewModel) {
    val username by viewModel.userNameValue.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier.weight(1f),
                value = username, onValueChange = {
                    viewModel.onUserNameValueChanged(it)
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
                    viewModel.getUserInfo(username)
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
