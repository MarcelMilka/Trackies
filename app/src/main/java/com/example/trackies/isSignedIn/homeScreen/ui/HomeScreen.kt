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
import com.example.trackies.isSignedIn.homeScreen.ui.loading.lowerPart.loadingText
import com.example.trackies.isSignedIn.homeScreen.ui.loading.lowerPart.regularityChartLoading
import com.example.trackies.isSignedIn.homeScreen.ui.loading.lowerPart.rowWithRadioButtonsLoading
import com.example.trackies.isSignedIn.homeScreen.ui.loading.upperPart.loadingButtonAddAnotherTrackie
import com.example.trackies.isSignedIn.homeScreen.ui.loading.upperPart.loadingButtonShowAllTrackies
import com.example.trackies.isSignedIn.homeScreen.ui.loading.upperPart.previewOfListOfTrackiesLoading
import com.example.trackies.ui.sharedUI.customButtons.IconButtonToNavigateBetweenActivities
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerL
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
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

//                  Upper part
                    Column(

                        modifier = Modifier
                            .fillMaxWidth(),

                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top,

                        content = {

                            IconButtonToNavigateBetweenActivities(icon = Icons.Rounded.Person) { onOpenSettings() }

                            verticalSpacerL()

                            loadingText()

                            verticalSpacerS()

                            previewOfListOfTrackiesLoading()

                            verticalSpacerS()

                            loadingButtonShowAllTrackies()

                            verticalSpacerS()

                            loadingButtonAddAnotherTrackie()

                            verticalSpacerL()
                        }
                    )

//                  Lower part
                    Column(

                        modifier = Modifier
                            .fillMaxWidth(),

                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Bottom,

                        content = {

                            loadingText()

                            verticalSpacerS()

                            rowWithRadioButtonsLoading()

                            verticalSpacerS()

                            regularityChartLoading()
                        }
                    )
                }
            )
        }
    )
}