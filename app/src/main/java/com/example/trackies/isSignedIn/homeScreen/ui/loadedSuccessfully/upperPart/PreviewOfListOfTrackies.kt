package com.example.trackies.isSignedIn.homeScreen.ui.loadedSuccessfully.upperPart

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.isSignedIn.homeScreen.viewState.HomeScreenViewState
import com.example.trackies.isSignedIn.trackie.TrackieViewState
import com.example.trackies.isSignedIn.trackie.trackie
import com.example.trackies.isSignedIn.user.vm.SharedViewModelViewState
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textTitleMedium

@Composable
fun previewOfListOfTrackies(
    homeScreenUiState: HomeScreenViewState,
    sharedViewModelUiState: SharedViewModelViewState.LoadedSuccessfully,
    onMarkAsIngested: (TrackieViewState) -> Unit,
    onDisplayDetails: (TrackieViewState) -> Unit
) {

    val targetHeightOfLazyColumn = homeScreenUiState.heightOfLazyColumn
    val heightOfLazyColumn by animateIntAsState(
        targetValue = targetHeightOfLazyColumn,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 50,
            easing = LinearOutSlowInEasing
        ),
        label = "",
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = heightOfLazyColumn.dp),

        verticalArrangement = Arrangement.Top,

        content = {

            items(
                items = sharedViewModelUiState.trackiesForToday.take(n = 3)
            ) { trackieViewState ->

                trackie(
                    trackieViewState = trackieViewState,
                    stateOfTheTrackie = sharedViewModelUiState.statesOfTrackiesForToday[trackieViewState.name]!!,
                    onMarkAsIngested = {
                        onMarkAsIngested(trackieViewState)
                    },
                    onDisplayDetails = {
                        onDisplayDetails(trackieViewState)
                    }
                )

                verticalSpacerS()

            }

            item {

                if (sharedViewModelUiState.trackiesForToday.count() > 3) {

                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp),

                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,

                        content = {

                            val amountOfTrackiesLeft = sharedViewModelUiState.trackiesForToday.count() - 3

                            if (amountOfTrackiesLeft == 1) {
                                textTitleMedium(content = "+ $amountOfTrackiesLeft more trackie")
                            }

                            else {
                                textTitleMedium(content = "+ $amountOfTrackiesLeft more trackie")
                            }
                        }
                    )
                }
            }
        }
    )
}