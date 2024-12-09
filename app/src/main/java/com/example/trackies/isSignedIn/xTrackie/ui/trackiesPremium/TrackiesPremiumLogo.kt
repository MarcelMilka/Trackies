package com.example.trackies.isSignedIn.xTrackie.ui.trackiesPremium

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.theme.PrimaryColor

@Composable
fun trackiesPremiumLogo() {

    Box(
        modifier = Modifier
            .width(60.dp)
            .height(20.dp)
            .background(
                color = PrimaryColor,
                shape = RoundedCornerShape(20.dp)
            ),

        contentAlignment = Alignment.Center,

        content = {

            textTitleSmall(content = "premium")
        }
    )
}