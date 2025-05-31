package com.lqm.androidlearning.compose

import android.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.lqm.androidlearning.compose.model.ComposeItemData
import com.lqm.androidlearning.compose.model.ParamType

val textData = arrayOf(
    ComposeItemData(
        name = "FontSize",
        type = ParamType.FontSize,
        options = arrayOf(12.dp, 16.dp, 20.dp)
    ),
    ComposeItemData(
        name = "Color",
        type = ParamType.Color,
        options = arrayOf(Color.BLACK, Color.GREEN, Color.RED)
    ),
    ComposeItemData(
        name = "FontStyle",
        type = ParamType.FontStyle,
        options = arrayOf(FontStyle.Normal, FontStyle.Italic)
    ),
)