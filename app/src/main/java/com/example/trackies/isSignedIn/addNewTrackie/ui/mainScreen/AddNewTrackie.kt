package com.example.trackies.isSignedIn.addNewTrackie.ui.mainScreen

import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.ui.timeOfIngestion
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.nameOfTrackie.ui.nameOfTrackie
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.scheduleDays.ui.scheduleDays
import com.example.trackies.ui.sharedUI.customButtons.iconButtonToNavigateBetweenActivities
import com.example.trackies.isSignedIn.addNewTrackie.ui.scaffold.addNewTrackieBottomBar
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.dailyDose.ui.dailyDose
import com.example.trackies.isSignedIn.addNewTrackie.buisness.convertIntoTrackieModel
import com.example.trackies.isSignedIn.addNewTrackie.buisness.AddNewTrackieSegments
import com.example.trackies.isSignedIn.addNewTrackie.vm.AddNewTrackieViewModel
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelViewState
import androidx.compose.material.icons.automirrored.rounded.KeyboardReturn
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerL
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textHeadlineMedium
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import com.example.trackies.ui.theme.BackgroundColor
import com.example.globalConstants.TrackiesPremium
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import com.example.globalConstants.MeasuringUnit
import com.example.globalConstants.Dimensions
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import android.annotation.SuppressLint
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addNewTrackie(
    sharedViewModelUiState: SharedViewModelViewState,
    addNewTrackieViewModel: AddNewTrackieViewModel,

    importNamesOfAllTrackies: (List<String>) -> Unit,

    onReturn: () -> Unit,

    onUpdateName: (String) -> Unit,
    onUpdateMeasuringUnit: (MeasuringUnit) -> Unit,
    onUpdateDose: (Int) -> Unit,
    onUpdateRepeatOn: (MutableSet<String>) -> Unit,

    onUpdateTimeOfIngestion: () -> Unit,
    onDeleteTimeOfIngestion: () -> Unit,

    onActivate: (AddNewTrackieSegments) -> Unit,
    onDeactivate: (AddNewTrackieSegments) -> Unit,

    onClearAll: () -> Unit,
    onAdd: (TrackieModel) -> Unit,

    onDisplayTrackiesPremiumDialog: () -> Unit
) {

    LaunchedEffect(true) {

        importNamesOfAllTrackies(
            (sharedViewModelUiState as SharedViewModelViewState.LoadedSuccessfully).namesOfAllTrackies
        )
    }

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

                            val addNewTrackieModel =
                                addNewTrackieViewModel.addNewTrackieModel.value
                                .convertIntoTrackieModel()

                            onAdd(addNewTrackieModel)
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

                                SharedViewModelViewState.Loading -> {}

                                is SharedViewModelViewState.LoadedSuccessfully -> {

                                    val licenseIsActive = sharedViewModelUiState.license.active

                                    val enabledToAddAnotherTrackie =
                                        sharedViewModelUiState.license.totalAmountOfTrackies < TrackiesPremium.totalAmountOfTrackiesNonPremiumAccount

                                    val enabledToAddNewTrackie = when (licenseIsActive) {

                                        true -> {

                                            true
                                        }

                                        false -> {

                                            enabledToAddAnotherTrackie
                                        }
                                    }

                                    nameOfTrackie(
                                        enabledToAddNewTrackie = enabledToAddNewTrackie,

                                        addNewTrackieViewModel = addNewTrackieViewModel,

                                        updateName = {

                                            onUpdateName(it)
                                        },

                                        activate = {

                                            onActivate(
                                                AddNewTrackieSegments.NameOfTrackie
                                            )
                                        },

                                        deactivate = {
                                            onDeactivate(
                                                AddNewTrackieSegments.NameOfTrackie
                                            )
                                        },

                                        displayTrackiesPremiumDialog = {

                                            onDisplayTrackiesPremiumDialog()
                                        }
                                    )

                                    verticalSpacerS()

                                    dailyDose(
                                        enabledToAddNewTrackie = enabledToAddNewTrackie,
                                        addNewTrackieViewModel = addNewTrackieViewModel,

                                        updateMeasuringUnit = {
                                            onUpdateMeasuringUnit(it)
                                        },
                                        updateDose = {
                                            onUpdateDose(it)
                                        },

                                        activate = {
                                            onActivate(
                                                AddNewTrackieSegments.DailyDose
                                            )
                                        },
                                        deactivate = {
                                            onDeactivate(
                                                AddNewTrackieSegments.DailyDose
                                            )
                                        },
                                        displayTrackiesPremiumDialog = {

                                            onDisplayTrackiesPremiumDialog()
                                        }
                                    )

                                    verticalSpacerS()

                                    scheduleDays(
                                        enabledToAddNewTrackie = enabledToAddNewTrackie,
                                        addNewTrackieViewModel = addNewTrackieViewModel,

                                        updateRepeatOn = {

                                            onUpdateRepeatOn(it)
                                        },

                                        activate = {
                                            onActivate(
                                                AddNewTrackieSegments.ScheduleDays
                                            )
                                        },
                                        deactivate = {
                                            onDeactivate(
                                                AddNewTrackieSegments.ScheduleDays
                                            )
                                        },
                                        displayTrackiesPremiumDialog = {

                                            onDisplayTrackiesPremiumDialog()
                                        }
                                    )

                                    verticalSpacerS()

                                    timeOfIngestion(
                                        enabledToUseThisFeature = licenseIsActive,
                                        addNewTrackieViewModel = addNewTrackieViewModel,
                                        update = {

                                            onUpdateTimeOfIngestion()
                                        },
                                        delete = {

                                            onDeleteTimeOfIngestion()
                                        },
                                        activate = {
                                            onActivate(
                                                AddNewTrackieSegments.TimeOfIngestion
                                            )
                                        },
                                        deactivate = {
                                            onDeactivate(
                                                AddNewTrackieSegments.TimeOfIngestion
                                            )
                                        },
                                        displayTrackiesPremiumDialog = {

                                            onDisplayTrackiesPremiumDialog()
                                        }
                                    )
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