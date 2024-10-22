package com.example.trackies.isSignedIn.allTrackies.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardReturn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.isSignedIn.allTrackies.buisness.WhatToDisplay
import com.example.trackies.isSignedIn.allTrackies.ui.components.displayAllTrackies
import com.example.trackies.isSignedIn.allTrackies.ui.components.displayAllTrackiesForToday
import com.example.trackies.isSignedIn.homeScreen.ui.loading.upperPart.previewOfListOfTrackiesLoading
import com.example.trackies.isSignedIn.trackie.TrackieViewState
import com.example.trackies.isSignedIn.user.vm.SharedViewModelViewState
import com.example.trackies.ui.sharedUI.customButtons.iconButtonToNavigateBetweenActivities
import com.example.trackies.ui.sharedUI.customButtons.mediumRadioTextButton
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerL
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textHeadlineMedium
import com.example.trackies.ui.theme.BackgroundColor

@Composable
fun displayAllTrackies(
    sharedViewModelUiState: SharedViewModelViewState,
    fetchAllUsersTrackies: () -> Unit,
    onReturn: () -> Unit,
    onMarkTrackieAsIngested: (TrackieViewState) -> Unit,
    onDisplayDetailedTrackie: (TrackieViewState) -> Unit
) {

    var wholeWeek by remember { mutableStateOf(false) }
    var today by remember { mutableStateOf(true) }

    var whatToDisplay by remember { mutableStateOf(WhatToDisplay.TrackiesForToday) }

    Box(

        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundColor),

        content = {

            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),

                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,

                content = {

                    iconButtonToNavigateBetweenActivities(icon = Icons.Rounded.KeyboardReturn) {
                        onReturn()
                    }

                    verticalSpacerL()

                    textHeadlineMedium("All your trackies")

                    verticalSpacerS()

                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp),

                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,

                        content = {

                            mediumRadioTextButton(text = "whole week", isSelected = wholeWeek) {

                                wholeWeek = it
                                today = !it

                                whatToDisplay = WhatToDisplay.TrackiesForTheWholeWeek
                            }

                            mediumRadioTextButton(text = "today", isSelected = today) {

                                today = it
                                wholeWeek = !it

                                whatToDisplay = WhatToDisplay.TrackiesForToday
                            }
                        }
                    )

                    verticalSpacerS()

                    when (sharedViewModelUiState) {

                        SharedViewModelViewState.Loading -> {

                        }

                        is SharedViewModelViewState.LoadedSuccessfully -> {

                            when(whatToDisplay) {

                                WhatToDisplay.TrackiesForToday -> {

                                    displayAllTrackiesForToday(
                                        listOfTrackies = sharedViewModelUiState.trackiesForToday,
                                        statesOfTrackiesForToday = sharedViewModelUiState.statesOfTrackiesForToday,
                                        onMarkAsIngested = {
                                            onMarkTrackieAsIngested(it)
                                        },
                                        onDisplayDetails = {
                                            onDisplayDetailedTrackie(it)
                                        }
                                    )
                                }

                                WhatToDisplay.TrackiesForTheWholeWeek -> {

                                    when(sharedViewModelUiState.allTrackies) {

                                        null -> {

                                            previewOfListOfTrackiesLoading()

                                            fetchAllUsersTrackies()
                                        }

                                        else -> {

                                            displayAllTrackies(

                                                listOfTrackies = sharedViewModelUiState.allTrackies!!,

                                                listOfTrackiesForToday = sharedViewModelUiState.trackiesForToday,

                                                statesOfTrackiesForToday = sharedViewModelUiState.statesOfTrackiesForToday,

                                                onMarkAsIngested = {
                                                    onMarkTrackieAsIngested(it)
                                                },

                                                onDisplayDetails = {
                                                    onDisplayDetailedTrackie(it)
                                                }

                                            )
                                        }
                                    }
                                }
                            }
                        }

                        SharedViewModelViewState.FailedToLoadData -> {

                        }

                    }
                }
            )
        }
    )
}