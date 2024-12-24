package com.example.trackies.isSignedIn.detailedTrackie.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardReturn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.trackies.isSignedIn.detailedTrackie.ui.loadedSuccessfully.lowerPart.detailedTrackieRegularityChart
import com.example.trackies.isSignedIn.detailedTrackie.ui.loadedSuccessfully.upperPart.detailedTrackieUpperPart
import com.example.trackies.isSignedIn.detailedTrackie.ui.loading.upperPart.loadingUpperPartOfDetailedTrackie
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelViewState
import com.example.trackies.ui.sharedUI.customButtons.iconButtonToNavigateBetweenActivities
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerL
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textHeadlineMedium
import com.example.trackies.isSignedIn.homeScreen.ui.loading.lowerPart.loadingRegularityChart
import com.example.trackies.ui.sharedUI.loadingText.loadingText
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.Dimensions

@Composable
fun detailedTrackie(
    sharedViewModelViewState: SharedViewModelViewState,
    trackieModel: TrackieModel?,
    onReturn: () -> Unit,
    onDelete: (TrackieModel) -> Unit
) {

    Box(

        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundColor),

        content = {

            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = Dimensions.padding)
                    .background(color = BackgroundColor),

                verticalArrangement = Arrangement.SpaceBetween,

                content = {

//                  Upper part
                    Column(

                        modifier = Modifier
                            .fillMaxWidth(),

                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top,

                        content = {

                            iconButtonToNavigateBetweenActivities(icon = Icons.Rounded.KeyboardReturn) {

                                onReturn()
                            }

                            verticalSpacerL()

                            when (trackieModel) {

                                null -> {

                                    loadingUpperPartOfDetailedTrackie()
                                }

                                else -> {

                                    detailedTrackieUpperPart(

                                        trackieModel = trackieModel,

                                        onDelete = {

                                            onDelete(trackieModel)
                                        }
                                    )
                                }
                            }
                        }
                    )

//                  Lower part
                    Column(

                        modifier = Modifier
                            .fillMaxWidth(),

                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Bottom,

                        content = {

                            when (sharedViewModelViewState) {

                                SharedViewModelViewState.Loading -> {}

                                is SharedViewModelViewState.LoadedSuccessfully -> {

                                    when (trackieModel) {

                                        null -> {

                                            loadingText()

                                            verticalSpacerS()

                                            loadingRegularityChart()
                                        }

                                        else -> {

                                            textHeadlineMedium(content = "Regularity")

                                            verticalSpacerS()

                                            detailedTrackieRegularityChart()
                                        }
                                    }
                                }

                                is SharedViewModelViewState.FailedToLoadData -> {}
                            }
                        }
                    )
                }
            )
        }
    )
}