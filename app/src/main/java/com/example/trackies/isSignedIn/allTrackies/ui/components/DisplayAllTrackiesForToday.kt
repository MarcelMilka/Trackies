package com.example.trackies.isSignedIn.allTrackies.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.trackies.isSignedIn.trackie.TrackieViewState
import com.example.trackies.isSignedIn.trackie.trackie
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS

@Composable
fun displayAllTrackiesForToday(
    listOfTrackies: List<TrackieViewState>,
    statesOfTrackiesForToday: Map<String,Boolean>,
    onMarkAsIngested: (TrackieViewState) -> Unit,
    onDisplayDetails: (TrackieViewState) -> Unit,
) {

    LazyColumn(

        modifier = Modifier
            .fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,

        content = {

            this.items(listOfTrackies) {

                trackie(
                    trackieViewState = it,
                    stateOfTheTrackie = statesOfTrackiesForToday[it.name]!!,
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