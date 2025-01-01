package com.example.trackies.isSignedIn.homeScreen.ui.loading.upperPart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.globalConstants.Dimensions
import com.example.trackies.ui.theme.PrimaryColor

@Composable
fun loadingButtonAddAnotherTrackie() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(
                color = PrimaryColor,
                shape = RoundedCornerShape(
                    size = Dimensions.roundedCornersOfMediumElements
                )
            )
            .testTag(tag = "loadingButtonAddAnotherTrackie"),
    )
}