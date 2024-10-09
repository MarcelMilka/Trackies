package com.example.trackies.ui.sharedUI.customSpacers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.trackies.ui.theme.Dimensions

@Composable
fun verticalSpacerS() {

    Spacer(modifier = Modifier.height(Dimensions.smallestSpaceBetweenComponents))
}

@Composable
fun verticalSpacerM() {

    Spacer(modifier = Modifier.height(Dimensions.mediumSpaceBetweenComponents))
}

@Composable
fun verticalSpacerL() {

    Spacer(modifier = Modifier.height(Dimensions.largeSpaceBetweenComponents))
}