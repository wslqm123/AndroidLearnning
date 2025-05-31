package com.lqm.androidlearning.compose.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

class ComposeItemData(
    val name: String = "",
    val type: ParamType = ParamType.UnKnow,
    val options: Array<Any> = arrayOf()
)

enum class ParamType {
    UnKnow,
    Color,
    FontSize,
    FontStyle,
    FontWeight,
    FontFamily,
    LetterSpacing,
    TextDecoration,
    TextAlign,
    LineHeight,
    Overflow,
    SoftWrap,
}

class ComponentParams {
    val color: Color = Color.Unspecified
    val fontSize: TextUnit = TextUnit.Unspecified
    val fontStyle: FontStyle? = null
    val fontWeight: FontWeight? = null
    val fontFamily: FontFamily? = null
    val letterSpacing: TextUnit = TextUnit.Unspecified
    val textDecoration: TextDecoration? = null
    val textAlign: TextAlign? = null
    val lineHeight: TextUnit = TextUnit.Unspecified
    val overflow: TextOverflow = TextOverflow.Clip
    val softWrap: Boolean = true
    val maxLines: Int = Int.MAX_VALUE
}

class ModifierParams {

}