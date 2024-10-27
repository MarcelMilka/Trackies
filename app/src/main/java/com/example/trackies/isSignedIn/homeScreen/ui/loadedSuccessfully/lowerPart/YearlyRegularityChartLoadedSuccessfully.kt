package com.example.trackies.isSignedIn.homeScreen.ui.loadedSuccessfully.lowerPart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.sharedUI.customText.boldTextTitleMedium
import com.example.trackies.ui.sharedUI.customText.centeredTextTitleMedium

@Composable
fun yearlyRegularityChartLoadedSuccessfully() {

    Column(

        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),

        verticalArrangement = Arrangement.Center,

        horizontalAlignment = Alignment.CenterHorizontally,

        content = {

            boldTextTitleMedium(content = "This feature is not available yet.")
            centeredTextTitleMedium(content = "We're working on it. Stay tuned for future updates.")
        }
    )
}