package com.example.trackies.isSignedIn.addNewTrackie.ui.segments.scheduleDays.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.scheduleDays.staticValues.ScheduleDaysHintOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.scheduleDays.staticValues.ScheduleDaysHeightOptions
import com.example.trackies.isSignedIn.addNewTrackie.vm.AddNewTrackieViewModel
import com.example.globalConstants.DaysOfWeek
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.globalConstants.Dimensions
import com.example.trackies.ui.theme.SecondaryColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun scheduleDays(
    enabledToAddNewTrackie: Boolean,
    addNewTrackieViewModel: AddNewTrackieViewModel,
    updateRepeatOn: (MutableSet<String>) -> Unit,
    activate: () -> Unit,
    deactivate: () -> Unit,
    displayTrackiesPremiumDialog: () -> Unit
) {

//  AddNewTrackieModel-data:
    var scheduledDaysOfWeek by remember {
        mutableStateOf(
            mutableSetOf<String>()
        )
    }


//  ScheduleDaysViewState-data:
    var targetHeightOfTheSurface by remember {
        mutableIntStateOf(
            ScheduleDaysHeightOptions.displayDeactivated
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

    var displayFieldWithChosenDaysOfWeek by remember {
        mutableStateOf(false)
    }
    var displayFieldWithSelectableButtons by remember {
        mutableStateOf(false)
    }

    var hint by remember {
        mutableStateOf(
            ScheduleDaysHintOptions.scheduleDaysOfIngestion
        )
    }

//  Control whether this segment is active
    var thisSegmentIsActive by remember {
        mutableStateOf(false)
    }


//  'Collector' of changes
    CoroutineScope(Dispatchers.Default).launch {

//      Collect changes from 'addNewTrackieModel'
        this.launch {

            addNewTrackieViewModel.addNewTrackieModel.collect { addNewTrackieModel ->

                scheduledDaysOfWeek = addNewTrackieModel.repeatOn.toMutableSet()
            }
        }

//      Collect changes from 'scheduleDaysTrackieViewState'
        this.launch {

            addNewTrackieViewModel.scheduleDaysViewState.collect { scheduleDaysViewState ->

                targetHeightOfTheSurface = scheduleDaysViewState.targetHeightOfTheSurface

                displayFieldWithChosenDaysOfWeek = scheduleDaysViewState.displayScheduledDaysOfWeek
                displayFieldWithSelectableButtons = scheduleDaysViewState.displayChips

                hint = scheduleDaysViewState.hint
            }
        }

//      Control whether this segment is active, or not
        this.launch {

            addNewTrackieViewModel.activityStatesOfSegments.collect {

                thisSegmentIsActive = when (it.scheduleDaysIsActive) {

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


//  Background of the composable, adjusts height of the whole composable and displays appropriate data
    Surface(

        modifier = Modifier
            .fillMaxWidth()
            .height(heightOfTheSurface.dp)
            .testTag("scheduleDays"),

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

                    when (enabledToAddNewTrackie) {

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

//                  This column displays "Scheduled days" and an appropriate hint
                    Column(

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp),

                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween,

                        content = {
                            textTitleMedium(content = "Schedule days")
                            textTitleSmall(content = hint)
                        }
                    )

                    AnimatedVisibility(

                        visible = displayFieldWithChosenDaysOfWeek,
                        enter = fadeIn(animationSpec = tween(500)),
                        exit = fadeOut(animationSpec = tween(500)),

                        content = {

                            Column(

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp),

                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center,

                                content = {

                                    val abbreviatedChosenDaysOfWeek = scheduledDaysOfWeek.map {
                                        it.take(3)
                                    }
                                    textTitleMedium(
                                        content = abbreviatedChosenDaysOfWeek.joinToString(separator = ", ")
                                    )
                                }
                            )
                        }
                    )

                    AnimatedVisibility(

                        visible = displayFieldWithSelectableButtons,
                        enter = fadeIn(animationSpec = tween(50)),
                        exit = fadeOut(animationSpec = tween(50)),

                        content = {

                            LazyRow(

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),

                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,

                                content = {

                                    val daysOfWeek = listOf(
                                        DaysOfWeek.monday,
                                        DaysOfWeek.tuesday,
                                        DaysOfWeek.wednesday,
                                        DaysOfWeek.thursday,
                                        DaysOfWeek.friday,
                                        DaysOfWeek.saturday,
                                        DaysOfWeek.sunday
                                    )

                                    this.items(daysOfWeek) { dayOfWeek ->

                                        mediumInputChip(

                                            text = dayOfWeek.substring(0, 3),

                                            isSelected = scheduledDaysOfWeek.contains(dayOfWeek),

                                            onClick = {

                                                scheduledDaysOfWeek = scheduledDaysOfWeek
                                                    .toMutableSet()
                                                    .apply {


                                                        if (contains(dayOfWeek)) {

                                                            remove(dayOfWeek)
                                                        }

                                                        else {

                                                            add(dayOfWeek)
                                                        }
                                                    }
                                                    .sortedBy {

                                                        daysOfWeek.indexOf(it)
                                                    }
                                                    .toMutableSet()

                                                updateRepeatOn(scheduledDaysOfWeek)
                                            }
                                        )
                                    }
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}