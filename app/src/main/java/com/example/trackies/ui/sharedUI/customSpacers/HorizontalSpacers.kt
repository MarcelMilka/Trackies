package com.example.trackies.ui.sharedUI.customSpacers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.globalConstants.Dimensions

@Composable
fun horizontalSpacerS() {

    Spacer(modifier = Modifier.width(Dimensions.smallestSpaceBetweenComponents))
}