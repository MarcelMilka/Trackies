package com.example.trackies.ui.sharedUI.customButtons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun iconButtonToNavigateBetweenActivities (icon: ImageVector, onClick: () -> Unit) {

    IconButton(

        onClick = { onClick() },

        content = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = White,
            )
        }
    )
}