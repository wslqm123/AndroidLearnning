package com.lqm.androidlearning.thirdlibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.lqm.androidlearning.thirdlibrary.viewmodel.RxjavaViewModel
import com.lqm.androidlearning.ui.theme.AndroidLearningTheme

class RxjavaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(this)[RxjavaViewModel::class.java]
        setContent {
            AndroidLearningTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HelloRxjava(
                        modifier = Modifier.padding(innerPadding),
                        viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun HelloRxjava(
    modifier: Modifier = Modifier,
    viewModel: RxjavaViewModel = RxjavaViewModel()
) {
    Column(modifier = modifier) {
        Button(
            onClick = { viewModel.just() },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Rxjava just"
            )
        }
        Button(
            onClick = { viewModel.emit() },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Rxjava emit"
            )
        }
        Button(
            onClick = { viewModel.map() },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Rxjava map"
            )
        }
        Button(
            onClick = { viewModel.merge() },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Rxjava merge"
            )
        }
    }

}