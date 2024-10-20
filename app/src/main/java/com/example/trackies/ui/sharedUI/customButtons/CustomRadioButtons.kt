package com.example.trackies.ui.sharedUI.customButtons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.theme.PrimaryColor
import com.example.trackies.ui.theme.fonts

@Composable
fun mediumRadioTextButton(text: String, isSelected: Boolean, onClick: (Boolean) -> Unit ) {

    var textColor by remember { mutableStateOf(PrimaryColor) }
    textColor = when(isSelected) {
        true -> {
            PrimaryColor
        }
        false -> {
            White
        }
    }

    Text(
        text = text,
        style = fonts.titleMedium,
        color = textColor,
        modifier = Modifier
            .padding(end = 10.dp)
            .clickable(
                enabled = !isSelected,
                onClick = {
                    onClick(!isSelected)
                }
            )
    )
}