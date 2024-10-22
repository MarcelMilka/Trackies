package com.example.trackies.isSignedIn.allTrackies.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.trackies.isSignedIn.trackie.TrackieViewState
import com.example.trackies.isSignedIn.trackie.staticTrackie
import com.example.trackies.isSignedIn.trackie.trackie
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS

@Composable
fun displayAllTrackies(
    listOfTrackies: List<TrackieViewState>,
    listOfTrackiesForToday: List<TrackieViewState>,
    statesOfTrackiesForToday: Map<String, Boolean>,
    onMarkAsIngested: (TrackieViewState) -> Unit,
    onDisplayDetails: (TrackieViewState) -> Unit
) {

    LazyColumn(

        modifier = Modifier
            .fillMaxSize(),

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
                        trackieViewState = trackieViewState,
                        stateOfTheTrackie = statesOfTrackiesForToday[trackieViewState.name]!!,
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