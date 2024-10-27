package com.example.trackies.isSignedIn.detailedTrackie.ui

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardReturn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.isSignedIn.constantValues.CurrentTime
import com.example.trackies.isSignedIn.detailedTrackie.ui.loading.lowerPart.detailedTrackieWeeklyRegularityChartLoadedSuccessFully
import com.example.trackies.isSignedIn.detailedTrackie.ui.loading.upperPart.upperPartLoadedSuccessfully
import com.example.trackies.isSignedIn.detailedTrackie.ui.loading.upperPart.upperPartLoading
import com.example.trackies.isSignedIn.trackie.TrackieViewState
import com.example.trackies.isSignedIn.user.vm.SharedViewModelViewState
import com.example.trackies.ui.sharedUI.customButtons.iconButtonToNavigateBetweenActivities
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerL
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textHeadlineMedium
import com.example.trackies.ui.sharedUI.loadingText.loadingText
import com.example.trackies.isSignedIn.homeScreen.ui.loading.lowerPart.regularityChartLoading
import com.example.trackies.isSignedIn.homeScreen.ui.loadedSuccessfully.lowerPart.homeScreenWeeklyRegularityChartLoadedSuccessFully
import com.example.trackies.isSignedIn.trackie.trackie
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.Dimensions
import com.example.trackies.ui.theme.PrimaryColor
import com.example.trackies.ui.theme.SecondaryColor
import com.example.trackies.ui.theme.White50
import com.example.trackies.ui.theme.fonts

@Composable
fun detailedTrackie(
    sharedViewModelUiState: SharedViewModelViewState,
    trackieViewState: TrackieViewState?,
    onReturn: () -> Unit,
    onDelete: (TrackieViewState) -> Unit
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

                                    regularityChartLoading()
                                }

                                is SharedViewModelViewState.LoadedSuccessfully -> {

                                    textHeadlineMedium(content = "Regularity")

                                    verticalSpacerS()

                                    when (trackieViewState) {

                                        null -> {

                                            regularityChartLoading()
                                        }

                                        else -> {

                                            detailedTrackieWeeklyRegularityChartLoadedSuccessFully(
                                                trackieViewState = trackieViewState,
                                                sharedViewModelUiState = sharedViewModelUiState
                                            )
                                        }
                                    }
                                }

                                SharedViewModelViewState.FailedToLoadData -> {}
                            }
                        }
                    )
                }
            )
        }
    )
}