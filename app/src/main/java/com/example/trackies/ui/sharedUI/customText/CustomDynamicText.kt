package com.example.trackies.ui.sharedUI.customText

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import com.example.trackies.ui.theme.fonts

@Composable fun dynamicTextTitleSmallWhiteToGreen(isGreen: Boolean, content: String) {

    var targetColor by remember { mutableStateOf(White) }

    targetColor = when (isGreen) {true -> {Green} false -> {White}}

    val color by animateColorAsState(
        targetValue = targetColor,
    )

    Text(
        text = content,
        style = fonts.titleSmall,
        color = color
    )
}