package com.example.trackies.isSignedIn.allTrackies.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardReturn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.isSignedIn.allTrackies.buisness.ListOfTrackiesToDisplay
import com.example.trackies.isSignedIn.allTrackies.ui.components.displayAllTrackies
import com.example.trackies.isSignedIn.allTrackies.ui.components.displayAllTrackiesForToday
import com.example.trackies.isSignedIn.homeScreen.ui.loading.upperPart.loadingPreviewOfListOfTrackies
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelViewState
import com.example.trackies.ui.sharedUI.customButtons.iconButtonToNavigateBetweenActivities
import com.example.trackies.ui.sharedUI.customButtons.mediumRadioTextButton
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerL
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textHeadlineLarge
import com.example.trackies.ui.sharedUI.customText.textHeadlineMedium
import com.example.trackies.ui.sharedUI.customText.textHeadlineSmall
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.Dimensions

@Composable
fun displayAllTrackies(
    listToDisplay: ListOfTrackiesToDisplay,
    onChangeListToDisplay: (ListOfTrackiesToDisplay) -> Unit,
    sharedViewModelUiState: SharedViewModelViewState,
    fetchAllUsersTrackies: () -> Unit,
    onReturn: () -> Unit,
    onMarkTrackieAsIngested: (TrackieModel) -> Unit,
    onDisplayDetailedTrackie: (TrackieModel) -> Unit
) {

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

                            mediumRadioTextButton(
                                text = "whole week",
                                isSelected = listToDisplay == ListOfTrackiesToDisplay.WholeWeek
                            ) {

                                onChangeListToDisplay(ListOfTrackiesToDisplay.WholeWeek)
                            }

                            mediumRadioTextButton(
                                text = "today",
                                isSelected = listToDisplay == ListOfTrackiesToDisplay.Today
                            ) {

                                onChangeListToDisplay(ListOfTrackiesToDisplay.Today)
                            }
                        }
                    )

                    verticalSpacerS()

                    when (sharedViewModelUiState) {

                        SharedViewModelViewState.Loading -> {

                            loadingPreviewOfListOfTrackies()
                        }

                        is SharedViewModelViewState.LoadedSuccessfully -> {

                            when(listToDisplay) {

                                ListOfTrackiesToDisplay.Today -> {

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

                                ListOfTrackiesToDisplay.WholeWeek -> {

                                    when(sharedViewModelUiState.allTrackies) {

                                        null -> {

                                            loadingPreviewOfListOfTrackies()

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

                        is SharedViewModelViewState.FailedToLoadData -> {

//                          big text 'Whoops...'
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

//                          smaller text 'An error occurred while loading your data. Try again later.' and cause of error
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