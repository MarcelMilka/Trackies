package com.example.trackies.isSignedIn.xTrackie.ui.componentsOfTrackies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.theme.TrackieMarkedAsIngested

@Preview
@Composable
fun magicButtonMarkedAsIngested() {

    Row(

        modifier = Modifier
            .width(70.dp)
            .height(50.dp)
            .background(
                color = TrackieMarkedAsIngested,
                shape = RoundedCornerShape(18.dp)
            )
            .testTag(tag = "magicButtonMarkedAsIngested"),

        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,

        content = {

            Icon(

                imageVector = Icons.Rounded.EmojiEvents,
                tint = Color.White,
                contentDescription = null
            )
        }
    )
}