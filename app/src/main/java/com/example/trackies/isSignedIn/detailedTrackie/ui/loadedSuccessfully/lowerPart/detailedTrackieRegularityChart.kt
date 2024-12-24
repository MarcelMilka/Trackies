package com.example.trackies.isSignedIn.detailedTrackie.ui.loadedSuccessfully.lowerPart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.sharedUI.customText.boldTextTitleMedium
import com.example.trackies.ui.sharedUI.customText.centeredTextTitleMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun detailedTrackieRegularityChart() {

    Column(

        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .testTag(tag = "detailedTrackieRegularityChart"),

        verticalArrangement = Arrangement.Center,

        horizontalAlignment = Alignment.CenterHorizontally,

        content = {

            boldTextTitleMedium(content = "This feature is not available yet.")
            centeredTextTitleMedium(content = "We're working on it. Stay tuned for future updates.")
        }
    )
}