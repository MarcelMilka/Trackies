package com.example.trackies.isSignedIn.addNewTrackie.presentation.timeOfIngestion.loadedSuccessfully

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.trackies.isSignedIn.addNewTrackie.vm.AddNewTrackieViewModel
import com.example.trackies.isSignedIn.trackie.premiumLogo
import com.example.trackies.ui.sharedUI.customButtons.mediumTextButton
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
    viewModel: AddNewTrackieViewModel
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

    LaunchedEffect(heightOfTheSurface) {
        Log.d("Halla!", "$heightOfTheSurface")
    }

    var displayFieldWithButton by remember {

        mutableStateOf(false)
    }

    var hint by remember {

        mutableStateOf(TimeOfIngestionHints.scheduleTimeOfIngestion)
    }

//  Control whether this segment is active.
    var thisSegmentIsActive by remember { mutableStateOf(false) }
    CoroutineScope(Dispatchers.Default).launch {

        viewModel.statesOfSegments.collect { statesOfSegments ->
            thisSegmentIsActive = statesOfSegments.insertTimeOfIngestionIsActive

            when (thisSegmentIsActive) {

                true -> {
                    viewModel.scheduleTimeDisplayButton()
                }

                false -> {
                    viewModel.scheduleTimeDisplayUnactivated()
                }
            }
        }
    }

    //  Update values
    CoroutineScope(Dispatchers.Default).launch {

        viewModel.scheduleTimeViewState.collect {
            targetHeightOfTheSurface = it.targetHeightOfTheSurface
            displayFieldWithButton = it.displayTheButton
            hint = it.hint
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

//              Collapse this segment
                true -> {

                    viewModel.deactivateSegment(
                        segmentToDeactivate = AddNewTrackieSegments.TimeOfIngestion
                    )

                    viewModel.scheduleTimeDisplayUnactivated()
                }

//              Expand this segment
                false -> {

                    viewModel.activateSegment(
                        segmentToActivate = AddNewTrackieSegments.TimeOfIngestion
                    )

                    viewModel.scheduleTimeDisplayButton()
                }
            }

            thisSegmentIsActive = !thisSegmentIsActive
        },

        content = {

//          Sets padding
            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimensions.roundedCornersOfMediumElements)
            ) {

//              Displays "Time of ingestion", hints and depending on the license: logo of Trackies Premium
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
                                textTitleSmall(content = TimeOfIngestionHints.scheduleTimeOfIngestion)
                            }
                        )

                        Column(

                            modifier = Modifier
                                .height(30.dp),

                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,

                            content = {

                                premiumLogo()
                            }
                        )
                    }
                )

                AnimatedVisibility(

                    visible = displayFieldWithButton,
                    enter = fadeIn(animationSpec = tween(250)),
                    exit = fadeOut(animationSpec = tween(250)),

                    content = {

                        Column(

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),

                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start,
                            content = {

                                mediumTextButton(
                                    text = "schedule time",
                                    onClick = {}
                                )
                            }
                        )
                    }
                )
            }
        }
    )
}