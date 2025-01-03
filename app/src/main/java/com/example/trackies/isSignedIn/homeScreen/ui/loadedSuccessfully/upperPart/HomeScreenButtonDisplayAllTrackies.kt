package com.example.trackies.isSignedIn.homeScreen.ui.loadedSuccessfully.upperPart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.theme.SecondaryColor

@Composable
fun buttonDisplayAllTrackies(onClick: () -> Unit) {

    Button(

        onClick = {

            onClick()
        },

        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .testTag(tag = "buttonDisplayAllTrackies"),

        shape = RoundedCornerShape(10.dp),

        colors = ButtonDefaults.buttonColors(
            containerColor = SecondaryColor,
            contentColor = Color.White
        ),

        content = {

            textTitleSmall(content = "show all trackies")
        }
    )
}