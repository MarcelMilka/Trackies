package com.example.trackies.isSignedIn.addNewTrackie.ui.segments.nameOfTrackie.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.nameOfTrackie.staticValues.NameOfTrackieHeightOptions
import com.example.trackies.ui.theme.Dimensions
import com.example.trackies.ui.theme.SecondaryColor

@Composable
fun nameOfTrackieLoading() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(NameOfTrackieHeightOptions.displayUnactivatedComponent.dp)
            .background(
                shape = RoundedCornerShape(size = Dimensions.roundedCornersOfBigElements),
                color = SecondaryColor
            )
    )
}