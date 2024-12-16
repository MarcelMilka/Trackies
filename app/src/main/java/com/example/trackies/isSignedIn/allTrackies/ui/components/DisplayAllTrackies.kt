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
import com.example.trackies.isSignedIn.xTrackie.ui.trackies.staticTrackie
import com.example.trackies.isSignedIn.xTrackie.ui.trackies.trackie
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS

@Composable
fun displayAllTrackies(
    listOfTrackies: List<TrackieModel>,
    listOfTrackiesForToday: List<TrackieModel>,
    statesOfTrackiesForToday: Map<String, Boolean>,
    onMarkAsIngested: (TrackieModel) -> Unit,
    onDisplayDetails: (TrackieModel) -> Unit
) {

    LazyColumn(

        modifier = Modifier
            .fillMaxSize()
            .testTag("displayAllTrackies"),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,

        content = {

            items(
                (listOfTrackiesForToday + listOfTrackies)
                    .toSet()
                    .toList()
            ) { trackieViewState ->

                if (listOfTrackiesForToday.contains(trackieViewState)) {

                    trackie(
                        trackieModel = trackieViewState,
                        isMarkedAsIngested = statesOfTrackiesForToday[trackieViewState.name]!!,
                        onMarkAsIngested = {
                            onMarkAsIngested(trackieViewState)
                        },
                        onDisplayDetails = {
                            onDisplayDetails(trackieViewState)
                        }
                    )
                }

                else {

                    staticTrackie(

                        name = trackieViewState.name,
                        totalDose = trackieViewState.totalDose,
                        measuringUnit = trackieViewState.measuringUnit,
                        repeatOn = trackieViewState.repeatOn,
                        ingestionTime = trackieViewState.ingestionTime,
                        onDisplayDetails = { onDisplayDetails(trackieViewState) }
                    )
                }

                verticalSpacerS()

            }
        }
    )
}