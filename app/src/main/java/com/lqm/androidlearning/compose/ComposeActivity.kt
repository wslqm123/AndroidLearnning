package com.lqm.androidlearning.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.lqm.androidlearning.common.MyTopAppBar
import com.lqm.androidlearning.compose.ui.ComponentList
import com.lqm.androidlearning.compose.viewmodel.ComposeViewModel
import com.lqm.androidlearning.ui.theme.AndroidLearningTheme

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(this)[ComposeViewModel::class.java]
        setContent {
            AndroidLearningTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        MyTopAppBar(onBackClick = {
                            finish()
                        }, "Compose组件")
                    },
                    content = { innerPadding ->
                        ComposeComponent(
                            modifier = Modifier.padding(innerPadding),
                            viewModel
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun ComposeComponent(
    modifier: Modifier = Modifier,
    viewModel: ComposeViewModel = ComposeViewModel()
) {
    Column(modifier = modifier) {
        ComponentList(viewModel)
    }

}