package com.example.trackies.isSignedIn.homeScreen.ui.loading.lowerPart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.theme.Dimensions
import com.example.trackies.ui.theme.SecondaryColor

@Composable
fun loadingRegularityChart() {

    Box(

        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(

                color = SecondaryColor,
                shape = RoundedCornerShape(size = Dimensions.roundedCornersOfBigElements)
            )
            .testTag(tag = "loadingRegularityChart")
    )
}