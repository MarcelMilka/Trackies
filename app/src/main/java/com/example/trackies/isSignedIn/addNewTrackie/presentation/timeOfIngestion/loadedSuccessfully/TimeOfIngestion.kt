package com.example.trackies.isSignedIn.addNewTrackie.presentation.timeOfIngestion.loadedSuccessfully

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.isSignedIn.addNewTrackie.buisness.AddNewTrackieSegments
import com.example.trackies.isSignedIn.addNewTrackie.buisness.IngestionTime
import com.example.trackies.isSignedIn.addNewTrackie.vm.AddNewTrackieViewModel
import com.example.trackies.isSignedIn.trackie.trackiesPremiumLogo
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
    viewModel: AddNewTrackieViewModel,
    onScheduleTimeAndAssignDose: () -> Unit,
) {

    var targetHeightOfTheSurface by remember {

        mutableIntStateOf(
            TimeOfIngestionSetOfHeights.displayUnactivatedComponent
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

        mutableStateOf(TimeOfIngestionHints.clickToInsertTimeOfIngestion)
    }

    var ingestionTime: IngestionTime? by remember {
        mutableStateOf(null)
    }

    var displayContentInTimeComponent by remember {

        mutableStateOf(false)
    }

//  Control whether this segment is active.
    var thisSegmentIsActive by remember {

        mutableStateOf(false)
    }

    CoroutineScope(Dispatchers.Default).launch {

        viewModel.statesOfSegments.collect { statesOfSegments ->

            thisSegmentIsActive = statesOfSegments.timeOfIngestionIsActive

            when (thisSegmentIsActive) {

//              Expand this segment
                true -> {
                    viewModel.scheduleTimeDisplayActivatedTimeComponent()
                }

//              Collapse this segment
                false -> {

                    if (ingestionTime != null) {

                        viewModel.scheduleTimeDisplayUnactivatedTimeComponent()
                    }

                    else {

                        viewModel.scheduleTimeDisplayUnactivated()
                    }
                }
            }
        }
    }

//  Update values
    CoroutineScope(Dispatchers.Default).launch {

        viewModel.scheduleTimeViewState.collect {
            targetHeightOfTheSurface = it.targetHeightOfTheSurface
            hint = it.hint
            ingestionTime = it.ingestionTime
            displayContentInTimeComponent = it.displayContentInTimeComponent
        }
    }

    LaunchedEffect(targetHeightOfTheSurface) {
        Log.d("Halla!", "$targetHeightOfTheSurface")
    }

    Surface(

        modifier = Modifier
            .fillMaxWidth()
            .height(height = heightOfTheSurface.dp),

        color = SecondaryColor,
        shape = RoundedCornerShape(Dimensions.roundedCornersOfBigElements),

        onClick = {

            when (thisSegmentIsActive) {

//              Collapse this segment
                true -> {

                    viewModel.deactivateSegment(
                        segmentToDeactivate = AddNewTrackieSegments.TimeOfIngestion
                    )

                    if (ingestionTime == null) {

                        viewModel.deactivateSegment(
                            segmentToDeactivate = AddNewTrackieSegments.TimeOfIngestion
                        )
                    }
                }

//              Expand this segment
                false -> {

                    if (ingestionTime == null) {
                        onScheduleTimeAndAssignDose()
                    }

                    viewModel.activateSegment(
                        segmentToActivate = AddNewTrackieSegments.TimeOfIngestion
                    )
                }
            }

            thisSegmentIsActive = !thisSegmentIsActive
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
                                    textTitleSmall(content = TimeOfIngestionHints.clickToInsertTimeOfIngestion)
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

                            timeComponent(

                                displayButtons = displayContentInTimeComponent,

                                hour = ingestionTime?.hour ?: "",

                                minute = ingestionTime?.minute ?: "",

                                onClick = {

                                    when (thisSegmentIsActive) {

                                        true -> {
                                            viewModel.deactivateSegment(
                                                segmentToDeactivate = AddNewTrackieSegments.TimeOfIngestion
                                            )
                                        }

                                        false -> {
                                            viewModel.activateSegment(
                                                segmentToActivate = AddNewTrackieSegments.TimeOfIngestion
                                            )
                                        }

                                    }
                                },

                                onEdit = {
                                    onScheduleTimeAndAssignDose()
                                },

                                onDelete = {

                                    viewModel.updateIngestionTime(
                                        ingestionTimeEntity = null
                                    )

                                    viewModel.deactivateSegment(
                                        segmentToDeactivate = AddNewTrackieSegments.TimeOfIngestion
                                    )
                                }

                            )
                        }
                    )
                }
            )
        }
    )
}