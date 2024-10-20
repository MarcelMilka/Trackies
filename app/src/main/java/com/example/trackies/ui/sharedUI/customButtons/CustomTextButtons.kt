package com.example.trackies.ui.sharedUI.customButtons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.theme.fonts

@Composable
fun mediumTextButton(text: String, onClick: () -> Unit ) {

    Text(
        text = text,
        style = fonts.titleMedium,
        color = White,
        modifier = Modifier
            .padding(end = 10.dp)
            .clickable {onClick()}
    )
}