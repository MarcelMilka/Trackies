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
import androidx.compose.foundation.layout.Row
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
import com.example.trackies.isSignedIn.addNewTrackie.buisness.AddNewTrackieSegments
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.scheduleDays.staticValues.ScheduleDaysHintOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.scheduleDays.staticValues.ScheduleDaysHeightOptions
import com.example.trackies.isSignedIn.addNewTrackie.vm.AddNewTrackieViewModel
import com.example.globalConstants.DaysOfWeek
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.theme.Dimensions
import com.example.trackies.ui.theme.SecondaryColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun scheduleDays(addNewTrackieViewModel: AddNewTrackieViewModel) {

    var repeatOn by remember { mutableStateOf(mutableSetOf<String>()) }

    var mondayIsSelected by remember { mutableStateOf(false) }
    var tuesdayIsSelected by remember { mutableStateOf(false) }
    var wednesdayIsSelected by remember { mutableStateOf(false) }
    var thursdayIsSelected by remember { mutableStateOf(false) }
    var fridayIsSelected by remember { mutableStateOf(false) }
    var saturdayIsSelected by remember { mutableStateOf(false) }
    var sundayIsSelected by remember { mutableStateOf(false) }

    var targetHeightOfTheColumn by remember {
        mutableIntStateOf(
            ScheduleDaysHeightOptions.displayUnactivatedComponent
        )
    }
    val heightOfTheColumn by animateIntAsState(
        targetValue = targetHeightOfTheColumn,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 50,
            easing = LinearOutSlowInEasing
        ),
        label = "",
    )

    var targetHeightOfTheSurface by remember {
        mutableIntStateOf(
            ScheduleDaysHeightOptions.displayUnactivatedComponent
        )
    }
    val heightOfTheSurface by animateIntAsState(
        targetValue = targetHeightOfTheColumn,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 50,
            easing = LinearOutSlowInEasing
        ),
        label = "adjust height of the surface which displays the content",
    )

    var displayFieldWithChosenDaysOfWeek by remember { mutableStateOf(false) }
    var displayFieldWithSelectableButtons by remember { mutableStateOf(false) }

    var hint by remember {
        mutableStateOf(
            ScheduleDaysHintOptions.selectDaysOfWeek
        )
    }

//    var areExpanded by remember { mutableStateOf(false) }

//  Control whether this segment is active
    var thisSegmentIsActive by remember { mutableStateOf(false) }
    CoroutineScope(Dispatchers.Default).launch {

        addNewTrackieViewModel.activityStatesOfSegments.collect { statesOfSegments ->
            thisSegmentIsActive = statesOfSegments.scheduleDaysIsActive

            when (thisSegmentIsActive) {
                true -> addNewTrackieViewModel.scheduleDaysDisplayDaysToSchedule()

                false -> {
                    if (repeatOn.isEmpty()) {
                        addNewTrackieViewModel.scheduleDaysDisplayCollapsed()
                    }
                    else {
                        addNewTrackieViewModel.scheduleDaysDisplayScheduledDays()
                    }
                }
            }
        }
    }

//  Update values
    CoroutineScope(Dispatchers.Default).launch {

        addNewTrackieViewModel.scheduleDaysViewState.collect {
            repeatOn = it.repeatOn

            mondayIsSelected = it.mondayIsSelected
            tuesdayIsSelected = it.tuesdayIsSelected
            wednesdayIsSelected = it.wednesdayIsSelected
            thursdayIsSelected = it.thursdayIsSelected
            fridayIsSelected = it.fridayIsSelected
            saturdayIsSelected = it.saturdayIsSelected
            sundayIsSelected = it.sundayIsSelected

            targetHeightOfTheColumn = it.targetHeightOfTheColumn
            targetHeightOfTheSurface = it.targetHeightOfTheSurface

            displayFieldWithChosenDaysOfWeek = it.displayFieldWithChosenDaysOfWeek
            displayFieldWithSelectableButtons = it.displayFieldWithSelectableButtons

            hint = it.hint
        }
    }

//  Holder of the surface and the supporting text
    Column(

        modifier = Modifier
            .fillMaxWidth()
            .height(heightOfTheColumn.dp),

        content = {

//          Background of the composable, adjusts height of the whole composable and displays appropriate data
            Surface(

                modifier = Modifier
                    .fillMaxWidth()
                    .height(heightOfTheSurface.dp),

                color = SecondaryColor,
                shape = RoundedCornerShape(Dimensions.roundedCornersOfBigElements),

                onClick = {

                    when (thisSegmentIsActive) {

//                      Collapse this segment
                        true -> {

                            addNewTrackieViewModel.deactivateSegment(segmentToDeactivate = AddNewTrackieSegments.ScheduleDays)

                            if (repeatOn.isEmpty()) {

                                addNewTrackieViewModel.updateRepeatOn(repeatOn = repeatOn)
//                                viewModel.scheduleDaysDisplayCollapsed()
                            }

                            else {
                                addNewTrackieViewModel.updateRepeatOn(repeatOn = repeatOn)
                                addNewTrackieViewModel.scheduleDaysDisplayScheduledDays()
                            }
                        }

//                      Expand this segment
                        false -> {

                            addNewTrackieViewModel.activateSegment(segmentToActivate = AddNewTrackieSegments.ScheduleDays)
                            addNewTrackieViewModel.scheduleDaysDisplayDaysToSchedule()
                        }
                    }
                },

                content = {

//                  Sets padding
                    Column(

                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dimensions.roundedCornersOfMediumElements)
                    ) {

                        Column( // displays what takes, may also display the premium logo

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

                        // display inserted name
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

//                                        val abbreviatedDays = repeatOn.map { it.take(3) }
//                                        TextMedium(abbreviatedDays.joinToString(separator = ", "))
                                    }
                                )
                            }
                        )

                        // display a text field
                        AnimatedVisibility(

                            visible = displayFieldWithSelectableButtons,
                            enter = fadeIn(animationSpec = tween(250)),
                            exit = fadeOut(animationSpec = tween(250)),

                            content = {

                                Row(

                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),

                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,

                                    content = {

                                        mediumSelectableTextButton(
                                            text = "mon",
                                            isSelected = mondayIsSelected,
                                            onAddToScheduledDays = {
                                                addNewTrackieViewModel.scheduleDaysInsertDayOfWeek(dayOfWeek = DaysOfWeek.monday)
                                                addNewTrackieViewModel
                                            },
                                            onRemoveFromScheduledDays = {
                                                addNewTrackieViewModel.scheduleDaysExtractDayOfWeek(
                                                    dayOfWeek = DaysOfWeek.monday
                                                )
                                            }
                                        )

                                        mediumSelectableTextButton(
                                            text = "tue",
                                            isSelected = tuesdayIsSelected,
                                            onAddToScheduledDays = { addNewTrackieViewModel.scheduleDaysInsertDayOfWeek(dayOfWeek = DaysOfWeek.tuesday) },
                                            onRemoveFromScheduledDays = {
                                                addNewTrackieViewModel.scheduleDaysExtractDayOfWeek(
                                                    dayOfWeek = DaysOfWeek.tuesday
                                                )
                                            }
                                        )

                                        mediumSelectableTextButton(
                                            text = "wed",
                                            isSelected = wednesdayIsSelected,
                                            onAddToScheduledDays = { addNewTrackieViewModel.scheduleDaysInsertDayOfWeek(dayOfWeek = DaysOfWeek.wednesday) },
                                            onRemoveFromScheduledDays = {
                                                addNewTrackieViewModel.scheduleDaysExtractDayOfWeek(
                                                    dayOfWeek = DaysOfWeek.wednesday
                                                )
                                            }
                                        )

                                        mediumSelectableTextButton(
                                            text = "thu",
                                            isSelected = thursdayIsSelected,
                                            onAddToScheduledDays = { addNewTrackieViewModel.scheduleDaysInsertDayOfWeek(dayOfWeek = DaysOfWeek.thursday) },
                                            onRemoveFromScheduledDays = {
                                                addNewTrackieViewModel.scheduleDaysExtractDayOfWeek(
                                                    dayOfWeek = DaysOfWeek.thursday
                                                )
                                            }
                                        )

                                        mediumSelectableTextButton(
                                            text = "fri",
                                            isSelected = fridayIsSelected,
                                            onAddToScheduledDays = { addNewTrackieViewModel.scheduleDaysInsertDayOfWeek(dayOfWeek = DaysOfWeek.friday) },
                                            onRemoveFromScheduledDays = {
                                                addNewTrackieViewModel.scheduleDaysExtractDayOfWeek(
                                                    dayOfWeek = DaysOfWeek.friday
                                                )
                                            }
                                        )

                                        mediumSelectableTextButton(
                                            text = "sat",
                                            isSelected = saturdayIsSelected,
                                            onAddToScheduledDays = { addNewTrackieViewModel.scheduleDaysInsertDayOfWeek(dayOfWeek = DaysOfWeek.saturday) },
                                            onRemoveFromScheduledDays = {
                                                addNewTrackieViewModel.scheduleDaysExtractDayOfWeek(
                                                    dayOfWeek = DaysOfWeek.saturday
                                                )
                                            }
                                        )

                                        mediumSelectableTextButton(
                                            text = "sun",
                                            isSelected = sundayIsSelected,
                                            onAddToScheduledDays = { addNewTrackieViewModel.scheduleDaysInsertDayOfWeek(dayOfWeek = DaysOfWeek.sunday) },
                                            onRemoveFromScheduledDays = {
                                                addNewTrackieViewModel.scheduleDaysExtractDayOfWeek(
                                                    dayOfWeek = DaysOfWeek.sunday
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                        )
                    }
                }
            )
        }
    )
}