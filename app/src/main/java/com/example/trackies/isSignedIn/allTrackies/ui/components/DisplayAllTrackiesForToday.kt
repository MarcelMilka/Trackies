package com.example.trackies.isSignedIn.allTrackies.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.isSignedIn.xTrackie.ui.trackies.trackie
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS

@Composable
fun displayAllTrackiesForToday(
    listOfTrackies: List<TrackieModel>,
    statesOfTrackiesForToday: Map<String,Boolean>,
    onMarkAsIngested: (TrackieModel) -> Unit,
    onDisplayDetails: (TrackieModel) -> Unit,
) {

    LazyColumn(

        modifier = Modifier
            .fillMaxSize()
            .testTag("displayAllTrackiesForToday"),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,

        content = {

            this.items(listOfTrackies) {

                trackie(
                    trackieModel = it,
                    isMarkedAsIngested = statesOfTrackiesForToday[it.name]!!,
                    onMarkAsIngested = {

                        onMarkAsIngested(it)
                    },
                    onDisplayDetails = {

                        onDisplayDetails(it)
                    }
                )

                verticalSpacerS()
            }
        }
    )
}