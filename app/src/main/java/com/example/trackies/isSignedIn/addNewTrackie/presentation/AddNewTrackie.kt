package com.example.trackies.isSignedIn.addNewTrackie.presentation

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.automirrored.rounded.KeyboardReturn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.isSignedIn.addNewTrackie.presentation.insertDailyDosage.loadedSuccessfully.insertDailyDosage
import com.example.trackies.isSignedIn.addNewTrackie.presentation.insertDailyDosage.loading.insertDailyDosageLoading
import com.example.trackies.isSignedIn.addNewTrackie.presentation.insertNameOfTrackie.loadedSuccessfully.insertNameOfTrackie
import com.example.trackies.isSignedIn.addNewTrackie.presentation.insertNameOfTrackie.loading.insertNameOfTrackieLoading
import com.example.trackies.isSignedIn.addNewTrackie.presentation.scheduleDays.loadedSuccessfully.scheduleDays
import com.example.trackies.isSignedIn.addNewTrackie.presentation.scheduleDays.loading.scheduleDaysLoading
import com.example.trackies.isSignedIn.addNewTrackie.presentation.timeOfIngestion.loadedSuccessfully.timeOfIngestion
import com.example.trackies.isSignedIn.addNewTrackie.vm.AddNewTrackieViewModel
import com.example.trackies.isSignedIn.trackie.TrackieViewState
import com.example.trackies.isSignedIn.user.vm.SharedViewModelViewState
import com.example.trackies.ui.sharedUI.customButtons.iconButtonToNavigateBetweenActivities
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerL
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textHeadlineMedium
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.Dimensions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addNewTrackie(
    sharedViewModelUiState: SharedViewModelViewState,
    addNewTrackieViewModel: AddNewTrackieViewModel,
    onReturn: () -> Unit,
    onClearAll: () -> Unit,
    onAdd: (TrackieViewState) -> Unit
) {

    Scaffold(

        modifier = Modifier.fillMaxSize(),

        bottomBar = {

            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(Dimensions.padding),

                content = {

                    addNewTrackieBottomBar(

                        addNewTrackieViewModel = addNewTrackieViewModel,

                        onClearAll = {
                            onClearAll()
                        },

                        onAdd = {

                            CoroutineScope(Dispatchers.Main).launch {

                                addNewTrackieViewModel.addNewTrackieViewState.collect {

                                    if (it.name != "" &&
                                        it.totalDose != 0 &&
                                        it.measuringUnit != "" &&
                                        it.repeatOn.isNotEmpty()) {

                                        onAdd(
                                            TrackieViewState(

                                                name = it.name,
                                                totalDose = it.totalDose,
                                                measuringUnit = it.measuringUnit,
                                                repeatOn = it.repeatOn,
                                                ingestionTime = it.ingestionTime
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            )
        },

        content = {

            Box(

                modifier = Modifier
                    .fillMaxSize()
                    .background(color = BackgroundColor),

                content = {

                    Column(

                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dimensions.padding)
                            .background(color = BackgroundColor),

                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top,

                        content = {

                            iconButtonToNavigateBetweenActivities(icon = Icons.AutoMirrored.Rounded.KeyboardReturn) { onReturn() }

                            verticalSpacerL()

                            textHeadlineMedium(content = "Add new trackie")

                            verticalSpacerS()

                            when (sharedViewModelUiState) {

                                SharedViewModelViewState.Loading -> {

                                    insertNameOfTrackieLoading()

                                    verticalSpacerS()

                                    insertDailyDosageLoading()

                                    verticalSpacerS()

                                    scheduleDaysLoading()

                                }

                                is SharedViewModelViewState.LoadedSuccessfully -> {

                                    insertNameOfTrackie(
                                        viewModel = addNewTrackieViewModel
                                    )

                                    verticalSpacerS()

                                    insertDailyDosage(
                                        viewModel = addNewTrackieViewModel
                                    )

                                    verticalSpacerS()

                                    scheduleDays(
                                        viewModel = addNewTrackieViewModel
                                    )

                                    verticalSpacerS()

                                    timeOfIngestion(
                                        viewModel = addNewTrackieViewModel
                                    )
                                }

                                SharedViewModelViewState.FailedToLoadData -> {
                                    textTitleMedium("Failed to load data")
                                }

                            }
                        }
                    )
                }
            )
        }
    )
}