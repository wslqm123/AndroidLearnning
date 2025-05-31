package com.lqm.androidlearning.compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lqm.androidlearning.compose.textData
import com.lqm.androidlearning.compose.viewmodel.ComposeViewModel


@Composable
fun ComponentList(
    viewModel: ComposeViewModel = ComposeViewModel()
) {
    Row {
        LazyColumn(modifier = Modifier.width(120.dp)) {
            items(textData, key = { it.name }) {
                Column {
                    Card(
                        modifier = Modifier
                            .width(120.dp)
                            .height(48.dp),
                        shape = RoundedCornerShape(0f, 0f, 0f, 0f),
                        colors = CardColors(
                            Color.White,
                            Color.Unspecified,
                            Color.Unspecified,
                            Color.Unspecified
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = it.name, textAlign = TextAlign.Center)
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFE1E1E1))
                    )
                }

            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Text is a central piece of any UI, and Jetpack Compose makes it easier to display or write text. Compose leverages composition of its building blocks, meaning you don’t need to overwrite properties and methods or extend big classes to have a specific composable design and logic working the way you want.",
                modifier = Modifier.padding(16.dp)
            )
        }

    }

}