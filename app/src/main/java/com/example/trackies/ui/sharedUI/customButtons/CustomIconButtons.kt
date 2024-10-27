package com.example.trackies.ui.sharedUI.customButtons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun iconButtonToNavigateBetweenActivities (
    icon: ImageVector,
    onClick: () -> Unit
) {

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

@Composable
fun iconButton(
    icon: ImageVector,
    onClick: () -> Unit
) {

    IconButton(

        onClick = {

            onClick()
        },

        content = {
            Icon(

                imageVector = icon,

                contentDescription = null,

                tint = White,
            )
        }
    )
}


@Composable
fun smallIconButton(
    icon: ImageVector,
    onClick: () -> Unit
) {

    IconButton(

        onClick = {
            onClick()
        },

        content = {

            Icon(

                modifier = Modifier
                    .size(15.dp),

                imageVector = icon,

                contentDescription = null,

                tint = White,
            )
        }
    )
}