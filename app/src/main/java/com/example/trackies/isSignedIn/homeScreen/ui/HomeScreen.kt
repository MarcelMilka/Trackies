package com.example.trackies.isSignedIn.homeScreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.sharedUI.customButtons.IconButtonToNavigateBetweenActivities
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.theme.BackgroundColor

@Composable
fun homeScreen(
//    heightOfHomeScreenLazyColumn: StateFlow<Int>,
//    uiState: HomeScreenViewState,
//    typeOfHomeScreenGraphToDisplay: HomeScreenGraphToDisplay,
    onOpenSettings: () -> Unit,
//    onAddNewTrackie: () -> Unit,
//    onMarkTrackieAsIngestedForToday: (trackieViewState: TrackieViewState) -> Unit,
//    onShowAllTrackies: () -> Unit,
//    onChangeGraph: (HomeScreenGraphToDisplay) -> Unit,
//    onDisplayDetailedTrackie: (trackieViewState: TrackieViewState) -> Unit
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

                    Column(

                        modifier = Modifier
                            .fillMaxWidth(),

                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top,

                        content = {

                            IconButtonToNavigateBetweenActivities(icon = Icons.Rounded.Person) { onOpenSettings() }
//
//                            Spacer40()
//
//                            when (uiState) {
//
//                                HomeScreenViewState.Loading -> {
//
//                                    Box(
//
//                                        modifier = Modifier
//                                            .fillMaxWidth(0.80f)
//                                            .height(30.dp)
//                                            .background(
//
//                                                color = SecondaryColor,
//                                                shape = RoundedCornerShape(20.dp)
//                                            )
//                                    )
//
//                                    Spacer5()
//
//                                    PreviewOfListOfTrackiesLoading()
//
//                                    Spacer5()
//
//                                    LoadingButtonShowAllTrackies()
//
//                                    Spacer5()
//
//                                    LoadingButtonAddAnotherTrackie()
//                                }
//
//                                is HomeScreenViewState.LoadedSuccessfully -> {
//
//                                    MediumHeader(content = "Your today's trackies")
//
//                                    Spacer5()
//
//                                    PreviewOfListOfTrackiesLoadedSuccessfully(
//                                        heightOfHomeScreenLazyColumn = heightOfHomeScreenLazyColumn,
//                                        uiState = uiState,
//
//                                        onCheck = { onMarkTrackieAsIngestedForToday(it) },
//                                        onDisplayDetails = { onDisplayDetailedTrackie(it) }
//                                    )
//
//                                    Spacer5()
//
//                                    ButtonShowAllTrackies { onShowAllTrackies() }
//
//                                    Spacer5()
//
//                                    ButtonAddAnotherTrackie { onAddNewTrackie() }
//                                }
//
//                                HomeScreenViewState.FailedToLoadData -> {}
//                            }
//
//                            Spacer40()
                        }
                    )

                    Column(

                        modifier = Modifier
                            .fillMaxWidth(),

                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Bottom,

                        content = {

                            textTitleMedium("Hello!")

//                            when (uiState) {
//
//                                HomeScreenViewState.Loading -> {
//
//                                    Box(
//
//                                        modifier = Modifier
//                                            .fillMaxWidth(0.80f)
//                                            .height(30.dp)
//                                            .background(
//
//                                                color = SecondaryColor,
//                                                shape = RoundedCornerShape(20.dp)
//                                            )
//                                    )
//
//                                    Spacer5()
//
//                                    RowWithRadioButtonsLoading()
//
//                                    Spacer5()
//
//                                    RegularityChartLoading()
//                                }
//
//                                is HomeScreenViewState.LoadedSuccessfully -> {
//
//                                    MediumHeader(content = "Regularity")
//
//                                    Spacer5()
//
//                                    RowWithRadioButtonsLoadedSuccessfully(graphToDisplay = typeOfHomeScreenGraphToDisplay) { onChangeGraph(it) }
//
//                                    Spacer5()
//
//                                    RegularityChartLoadedSuccessFully(uiState = uiState)
//                                }
//
//                                HomeScreenViewState.FailedToLoadData -> {}
//                            }
                        }
                    )
                }
            )
        }
    )
}