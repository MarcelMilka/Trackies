package com.example.trackies.isSignedIn.addNewTrackie.presentation.scheduleDays.loadedSuccessfully

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.theme.PrimaryColor
import com.example.trackies.ui.theme.fonts

@Composable
fun mediumSelectableTextButton(
    text: String,
    isSelected: Boolean,
    onAddToScheduledDays: () -> Unit,
    onRemoveFromScheduledDays: () -> Unit
) {


    var textColor by remember { mutableStateOf(PrimaryColor) }
    textColor = when(isSelected) {
        true -> {
            PrimaryColor
        }
        false -> {
            Color.White
        }
    }

    Text(
        text = text,
        style = fonts.titleMedium,
        color = textColor,
        modifier = Modifier
            .padding( end = 10.dp )
            .clickable(
                onClick = {
                    when(!isSelected) {
                        true -> {onAddToScheduledDays()}
                        false -> {onRemoveFromScheduledDays()}
                    }
                }
            )
    )
}