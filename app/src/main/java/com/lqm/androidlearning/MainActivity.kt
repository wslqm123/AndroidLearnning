package com.lqm.androidlearning

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.lqm.androidlearning.github.GithubUserActivity
import com.lqm.androidlearning.thirdlibrary.RxjavaActivity
import com.lqm.androidlearning.ui.theme.AndroidLearningTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidLearningTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    FunctionList(modifier = Modifier.padding(innerPadding))

                }
            }
        }
    }
}

@Composable
fun FunctionList(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(modifier = modifier) {
        Button(onClick = {
            context.startActivity(Intent(context, RxjavaActivity::class.java))
        }) {
            Text(
                text = "Rxjava"
            )
        }
        Button(onClick = {
            context.startActivity(Intent(context, GithubUserActivity::class.java))
        }) {
            Text(
                text = "Github"
            )
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidLearningTheme {
        Greeting("Android")
    }
}