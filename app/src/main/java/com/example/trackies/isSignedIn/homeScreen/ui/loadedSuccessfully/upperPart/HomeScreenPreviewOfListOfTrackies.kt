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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.trackies.isSignedIn.homeScreen.viewState.HomeScreenViewState
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.isSignedIn.xTrackie.ui.trackies.trackie
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelViewState
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textTitleMedium

@Composable
fun previewOfListOfTrackies(
    homeScreenViewState: HomeScreenViewState,
    sharedViewState: SharedViewModelViewState.LoadedSuccessfully,

    onMarkAsIngested: (TrackieModel) -> Unit,
    onDisplayDetails: (TrackieModel) -> Unit
) {

    val targetHeightOfLazyColumn = homeScreenViewState.heightOfLazyColumn
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
            .height(height = heightOfLazyColumn.dp)
            .testTag(tag = "previewOfListOfTrackies"),

        verticalArrangement = Arrangement.Top,

        content = {

//          display trackies for today
            items(items = sharedViewState.trackiesForToday.take(n = 3)) { trackieModel ->

                trackie(
                    trackieModel = trackieModel,
                    isMarkedAsIngested = sharedViewState.statesOfTrackiesForToday[trackieModel.name]!!,

                    onMarkAsIngested = {

                        onMarkAsIngested(trackieModel)
                    },
                    onDisplayDetails = {

                        onDisplayDetails(trackieModel)
                    }
                )

                verticalSpacerS()
            }

//          display how many Trackies for today are left to display e.g. '+2 more trackies'
            item {

                if (sharedViewState.trackiesForToday.count() > 3) {

                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .testTag(tag = "Row with information about trackies left to display"),

                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,

                        content = {

                            val amountOfTrackiesLeft = sharedViewState.trackiesForToday.count() - 3

                            if (amountOfTrackiesLeft == 1) {

                                textTitleMedium(content = "+ $amountOfTrackiesLeft more trackie")
                            }

                            else {

                                textTitleMedium(content = "+ $amountOfTrackiesLeft more trackies")
                            }
                        }
                    )
                }
            }
        }
    )
}