package com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.ui

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.buisness.TimeOfIngestion
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.staticValues.TimeOfIngestionHintOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.staticValues.TimeOfIngestionHeightOptions
import com.example.trackies.isSignedIn.addNewTrackie.vm.AddNewTrackieViewModel
import com.example.trackies.isSignedIn.xTrackie.ui.trackiesPremium.trackiesPremiumLogo
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.theme.Dimensions
import com.example.trackies.ui.theme.SecondaryColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun timeOfIngestion(
    enabledToUseThisFeature: Boolean,
    addNewTrackieViewModel: AddNewTrackieViewModel,
    update: () -> Unit,
    delete: () -> Unit,
    activate: () -> Unit,
    deactivate: () -> Unit,
    displayTrackiesPremiumDialog: () -> Unit
) {

//  AddNewTrackieModel-data:
    var timeOfIngestion: TimeOfIngestion? by remember {
        mutableStateOf(null)
    }


//  TimeOfIngestionViewState-data:
    var targetHeightOfTheSurface by remember {

        mutableIntStateOf(
            TimeOfIngestionHeightOptions.displayUnactivatedComponent
        )
    }
    val heightOfTheSurface by animateIntAsState(
        targetValue = targetHeightOfTheSurface,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 50,
            easing = LinearOutSlowInEasing
        ),
        label = "",
    )

    var hint by remember {

        mutableStateOf(TimeOfIngestionHintOptions.clickToInsertTimeOfIngestion)
    }

    var displayContentInTimeComponent by remember {

        mutableStateOf(false)
    }


//  Control whether this segment is active.
    var thisSegmentIsActive by remember {

        mutableStateOf(false)
    }


//  'Collector' of changes
    CoroutineScope(Dispatchers.Default).launch {

//      Collect changes from 'addNewTrackieModel'
        this.launch {

            addNewTrackieViewModel.addNewTrackieModel.collect { addNewTrackieModel ->

                timeOfIngestion = addNewTrackieModel.ingestionTime
            }
        }

//      Collect changes from 'nameOfTrackieViewState'
        this.launch {

            addNewTrackieViewModel.timeOfIngestionViewState.collect {
                targetHeightOfTheSurface = it.targetHeightOfTheSurface
                hint = it.hint
                displayContentInTimeComponent = it.displayContentInTimeComponent
            }
        }

//      Control whether this segment is active, or not
        this.launch {

            addNewTrackieViewModel.activityStatesOfSegments.collect {

                thisSegmentIsActive = when (it.timeOfIngestionIsActive) {

                    true -> {
                        true
                    }

                    false -> {
                        false
                    }
                }
            }
        }
    }


    Surface(

        modifier = Modifier
            .fillMaxWidth()
            .height(height = heightOfTheSurface.dp),

        color = SecondaryColor,
        shape = RoundedCornerShape(Dimensions.roundedCornersOfBigElements),

        onClick = {

            when (thisSegmentIsActive) {

//              Collapse
                true -> {

                    deactivate()
                }

//              Expand
                false -> {

                    when (enabledToUseThisFeature) {

                        true -> {

                            activate()
                        }

                        false -> {

                            displayTrackiesPremiumDialog()
                        }
                    }
                }
            }
        },

        content = {

//          Sets padding
            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimensions.roundedCornersOfMediumElements),

                content = {

//                  Displays "Time of ingestion", hints and depending on the license: logo of Trackies Premium
                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp),

                        horizontalArrangement = Arrangement.SpaceBetween,

                        content = {

                            Column(

                                modifier = Modifier
                                    .height(30.dp),

                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.SpaceBetween,

                                content = {

                                    textTitleMedium(content = "Time of ingestion")
                                    textTitleSmall(content = TimeOfIngestionHintOptions.clickToInsertTimeOfIngestion)
                                }
                            )

                            Column(

                                modifier = Modifier
                                    .height(30.dp),

                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,

                                content = {

                                    trackiesPremiumLogo()
                                }
                            )
                        }
                    )

                    Row(

                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(top = 10.dp),

                        horizontalArrangement = Arrangement.SpaceBetween,

                        content = {

                            scheduledTimeComponent(

                                displayButtons = displayContentInTimeComponent,

                                hour = timeOfIngestion?.hour ?: "",

                                minute = timeOfIngestion?.minute ?: "",

                                onClick = {

                                    when (thisSegmentIsActive) {

                                        true -> {

                                            deactivate()
                                        }

                                        false -> {

                                            activate()
                                        }

                                    }
                                },

                                onEdit = {

                                    update()
                                },

                                onDelete = {

                                    delete()
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}