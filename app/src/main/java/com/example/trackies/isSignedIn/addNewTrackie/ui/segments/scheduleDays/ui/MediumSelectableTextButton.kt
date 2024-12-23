package com.example.trackies.isSignedIn.addNewTrackie.ui.segments.scheduleDays.ui

import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.foundation.layout.padding
import com.example.trackies.ui.theme.PrimaryColor
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import com.example.trackies.ui.theme.fonts
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun mediumInputChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    var color by remember {

        mutableStateOf(White)
    }

    color = if (isSelected) PrimaryColor else White

    Text(

        text = text,

        style = fonts.titleMedium,

        color = color,

        modifier = Modifier
            .padding( end = 10.dp )
            .clickable(
                onClick = {

                    onClick()
                }
            )
    )
}