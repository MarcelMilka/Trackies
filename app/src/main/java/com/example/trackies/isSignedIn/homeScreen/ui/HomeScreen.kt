package com.example.trackies.isSignedIn.homeScreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.isSignedIn.homeScreen.buisness.HomeScreenChartToDisplay
import com.example.trackies.isSignedIn.homeScreen.ui.loadedSuccessfully.lowerPart.rowWithRadioButtonsLoadedSuccessfully
import com.example.trackies.isSignedIn.homeScreen.ui.loadedSuccessfully.upperPart.buttonAddAnotherTrackie
import com.example.trackies.isSignedIn.homeScreen.ui.loadedSuccessfully.upperPart.buttonDisplayAllTrackies
import com.example.trackies.isSignedIn.homeScreen.ui.loadedSuccessfully.upperPart.previewOfListOfTrackies
import com.example.trackies.ui.sharedUI.loadingText.loadingText
import com.example.trackies.isSignedIn.homeScreen.ui.loading.lowerPart.regularityChartLoading
import com.example.trackies.isSignedIn.homeScreen.ui.loading.lowerPart.rowWithRadioButtonsLoading
import com.example.trackies.isSignedIn.homeScreen.ui.loading.upperPart.loadingButtonAddAnotherTrackie
import com.example.trackies.isSignedIn.homeScreen.ui.loading.upperPart.loadingButtonShowAllTrackies
import com.example.trackies.isSignedIn.homeScreen.ui.loading.upperPart.previewOfListOfTrackiesLoading
import com.example.trackies.isSignedIn.homeScreen.viewState.HomeScreenViewState
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelViewState
import com.example.trackies.ui.sharedUI.customButtons.iconButtonToNavigateBetweenActivities
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerL
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textHeadlineLarge
import com.example.trackies.ui.sharedUI.customText.textHeadlineMedium
import com.example.trackies.ui.sharedUI.customText.textHeadlineSmall
import com.example.trackies.isSignedIn.homeScreen.ui.loadedSuccessfully.lowerPart.homeScreenWeeklyRegularityChartLoadedSuccessFully
import com.example.trackies.isSignedIn.homeScreen.ui.loadedSuccessfully.lowerPart.monthlyRegularityChartLoadedSuccessfully
import com.example.trackies.isSignedIn.homeScreen.ui.loadedSuccessfully.lowerPart.yearlyRegularityChartLoadedSuccessfully
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.Dimensions

@Composable
fun homeScreen(
    sharedViewModelUiState: SharedViewModelViewState,
    homeScreenUiState: HomeScreenViewState,
    onOpenSettings: () -> Unit,
    onDisplayAllTrackies: () -> Unit,
    onAddNewTrackie: () -> Unit,
    onDisplayDetailedTrackie: (TrackieModel) -> Unit,
    onUpdateHeightOfLazyColumn: (Int) -> Unit,
    onSwitchChart: (HomeScreenChartToDisplay) -> Unit,
    onMarkAsIngested: (TrackieModel) -> Unit
) {

    Box(

        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundColor),

        content = {

            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
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

                            iconButtonToNavigateBetweenActivities(icon = Icons.Rounded.Person) { onOpenSettings() }

                            verticalSpacerL()

                            when (sharedViewModelUiState) {

                                SharedViewModelViewState.Loading -> {

                                    loadingText()

                                    verticalSpacerS()

                                    previewOfListOfTrackiesLoading()

                                    verticalSpacerS()

                                    loadingButtonShowAllTrackies()

                                    verticalSpacerS()

                                    loadingButtonAddAnotherTrackie()

                                    verticalSpacerL()

                                }

                                is SharedViewModelViewState.LoadedSuccessfully -> {

                                    onUpdateHeightOfLazyColumn(sharedViewModelUiState.trackiesForToday.count())

                                    textHeadlineMedium(content = "Your today's trackies")

                                    previewOfListOfTrackies(
                                        homeScreenUiState = homeScreenUiState,
                                        sharedViewModelUiState = sharedViewModelUiState,
                                        onMarkAsIngested = {
                                            onMarkAsIngested(it)
                                        },
                                        onDisplayDetails = {
                                            onDisplayDetailedTrackie(it)
                                        }
                                    )

                                    verticalSpacerS()

                                    buttonDisplayAllTrackies {
                                        onDisplayAllTrackies()
                                    }

                                    verticalSpacerS()

                                    buttonAddAnotherTrackie {
                                        onAddNewTrackie()
                                    }

                                }

                                SharedViewModelViewState.FailedToLoadData -> {

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(Dimensions.heightOfUpperFragment),

                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Bottom,

                                        content = { textHeadlineLarge(content = "Whoops...") }
                                    )

                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize(),

                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.SpaceAround,

                                        content = {

                                            textHeadlineSmall(
                                                content = "An error occurred while loading your data. Try again later."
                                            )
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

                                    rowWithRadioButtonsLoading()

                                    verticalSpacerS()

                                    regularityChartLoading()

                                }

                                is SharedViewModelViewState.LoadedSuccessfully -> {

                                    textHeadlineMedium(content = "Regularity")

                                    verticalSpacerS()

                                    rowWithRadioButtonsLoadedSuccessfully(
                                        graphToDisplay = homeScreenUiState.typeOfHomeScreenChart,
                                        switchChart = {
                                            onSwitchChart(it)
                                        }
                                    )

                                    verticalSpacerS()

                                    when (homeScreenUiState.typeOfHomeScreenChart) {

                                        HomeScreenChartToDisplay.Weekly -> {

                                            homeScreenWeeklyRegularityChartLoadedSuccessFully(
                                                sharedViewModelUiState = sharedViewModelUiState
                                            )
                                        }

                                        HomeScreenChartToDisplay.Monthly -> {

                                            monthlyRegularityChartLoadedSuccessfully()
                                        }

                                        HomeScreenChartToDisplay.Yearly -> {

                                            yearlyRegularityChartLoadedSuccessfully()
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