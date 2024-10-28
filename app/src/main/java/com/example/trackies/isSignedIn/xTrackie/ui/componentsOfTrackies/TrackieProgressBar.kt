package com.example.trackies.isSignedIn.xTrackie.ui.componentsOfTrackies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.theme.PrimaryColor
import com.example.trackies.ui.theme.White50

@Composable fun trackieProgressBar(progress: Int) {

    Box (
        modifier = Modifier
            .width(80.dp)
            .height(10.dp)

            .background(
                color = White50,
                shape = RoundedCornerShape(5.dp)
            ),

        content = {
            Box (
                modifier = Modifier
                    .width(progress.dp)
                    .height(10.dp)
                    .background(
                        color = PrimaryColor,
                        shape = RoundedCornerShape(5.dp)
                    ),
            )
        }
    )
}