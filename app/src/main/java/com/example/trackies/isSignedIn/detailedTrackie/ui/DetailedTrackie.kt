package com.example.trackies.isSignedIn.detailedTrackie.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardReturn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.trackies.isSignedIn.detailedTrackie.ui.loading.lowerPart.detailedTrackieWeeklyRegularityChartLoadedSuccessFully
import com.example.trackies.isSignedIn.detailedTrackie.ui.loading.upperPart.upperPartLoadedSuccessfully
import com.example.trackies.isSignedIn.detailedTrackie.ui.loading.upperPart.upperPartLoading
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelViewState
import com.example.trackies.ui.sharedUI.customButtons.iconButtonToNavigateBetweenActivities
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerL
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textHeadlineMedium
import com.example.trackies.ui.sharedUI.loadingText.loadingText
import com.example.trackies.isSignedIn.homeScreen.ui.loading.lowerPart.loadingRegularityChart
import com.example.trackies.ui.sharedUI.customText.textHeadlineLarge
import com.example.trackies.ui.sharedUI.customText.textHeadlineSmall
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.Dimensions

@Composable
fun detailedTrackie(
    sharedViewModelUiState: SharedViewModelViewState,
    trackieViewState: TrackieModel?,
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

                            when (trackieViewState) {

                                null -> {

                                    upperPartLoading()
                                }

                                else -> {

                                    upperPartLoadedSuccessfully(

                                        trackieViewState = trackieViewState,

                                        onDelete = {
                                            onDelete(trackieViewState)
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

                            when (sharedViewModelUiState) {

                                SharedViewModelViewState.Loading -> {

                                    loadingText()

                                    verticalSpacerS()

                                    loadingRegularityChart()
                                }

                                is SharedViewModelViewState.LoadedSuccessfully -> {

                                    textHeadlineMedium(content = "Regularity")

                                    verticalSpacerS()

                                    when (trackieViewState) {

                                        null -> {

                                            loadingRegularityChart()
                                        }

                                        else -> {

                                            detailedTrackieWeeklyRegularityChartLoadedSuccessFully(
                                                trackieViewState = trackieViewState,
                                                sharedViewModelUiState = sharedViewModelUiState
                                            )
                                        }
                                    }
                                }

                                is SharedViewModelViewState.FailedToLoadData -> {

//                                  big text 'Whoops...'
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(Dimensions.heightOfUpperFragment),

                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Bottom,

                                        content = {

                                            textHeadlineLarge(
                                                content = "Whoops..."
                                            )
                                        }
                                    )

//                                  smaller text 'An error occurred while loading your data. Try again later.' and cause of error
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize(),

                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.SpaceAround,

                                        content = {

                                            textHeadlineSmall(
                                                content = "An error occurred while loading your data. Try again later."
                                            )

                                            textTitleMedium(
                                                content = sharedViewModelUiState.errorMessage
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    )
                }
            )
        }
    )
}