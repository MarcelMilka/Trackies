package com.example.trackies.isSignedIn.homeScreen.ui.loading.upperPart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.trackieRelatedComponents.loadingTrackie

@Composable
fun previewOfListOfTrackiesLoading() {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp),

        verticalArrangement = Arrangement.Top,

        content = {

            this.item {

                loadingTrackie()

                verticalSpacerS()

                loadingTrackie()

                verticalSpacerS()

                loadingTrackie()
            }
        }
    )
}