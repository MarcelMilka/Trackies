package com.example.trackies.isSignedIn.addNewTrackie.ui.segments.dailyDose.ui

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackies.isSignedIn.addNewTrackie.buisness.AddNewTrackieSegments
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.dailyDose.buisness.EnumMeasuringUnits
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.dailyDose.staticValues.DailyDoseHintOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.dailyDose.staticValues.DailyDoseHeightOptions
import com.example.trackies.isSignedIn.addNewTrackie.vm.AddNewTrackieViewModel
import com.example.trackies.isSignedIn.constantValues.MeasuringUnits
import com.example.trackies.ui.sharedUI.customButtons.mediumRadioTextButton
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.theme.Dimensions
import com.example.trackies.ui.theme.SecondaryColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun dailyDose(addNewTrackieViewModel: AddNewTrackieViewModel) {

//  user's data
    var measuringUnit: EnumMeasuringUnits? by remember { mutableStateOf(null) }
    var totalDailyDose by remember { mutableIntStateOf(0) }

    var mlIsChosen by remember { mutableStateOf(false) }
    var gIsChosen by remember { mutableStateOf(false) }
    var pcsIsChosen by remember { mutableStateOf(false) }

    var targetHeightOfTheColumn by remember {
        mutableIntStateOf(
            DailyDoseHeightOptions.displayUnactivatedComponent
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
            DailyDoseHeightOptions.displayUnactivatedComponent
        )
    }
    val heightOfTheSurface = animateIntAsState(
        targetValue = targetHeightOfTheColumn,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 50,
            easing = LinearOutSlowInEasing
        ),
        label = "",
    )

    var displayFieldWithInsertedDose by remember { mutableStateOf(false) }
    var displayFieldWithMeasuringUnits by remember { mutableStateOf(false) }
    var displayFieldWithTextField by remember { mutableStateOf(false) }

    var hint by remember {
        mutableStateOf(
            DailyDoseHintOptions.insertDailyDosage
        )
    }

    val focusRequester = remember { FocusRequester() }

//  Control whether this segment is active
    var thisSegmentIsActive by remember {

        mutableStateOf(false)
    }
    CoroutineScope(Dispatchers.Default).launch {

        addNewTrackieViewModel.activityStatesOfSegments.collect { statesOfSegments ->
            thisSegmentIsActive = statesOfSegments.dailyDoseIsActive

            when(thisSegmentIsActive) {
                true -> {
                    when(measuringUnit) {
                        null -> addNewTrackieViewModel.dailyDoseDisplayMeasuringUnitsToChoose()
                        else -> addNewTrackieViewModel.dailyDoseDisplayTextField()
                    }
                }
                false -> {
                    when(measuringUnit) {
                        null -> addNewTrackieViewModel.dailyDoseDisplayCollapsed()
                        else -> addNewTrackieViewModel.dailyDoseDisplayInsertedValue()
                    }
                }
            }
        }
    }

//  Update values
    CoroutineScope(Dispatchers.Default).launch {

        addNewTrackieViewModel.dailyDoseViewState.collect {
            measuringUnit = it.measuringUnit
            totalDailyDose = it.totalDailyDose

            mlIsChosen = it.mlIsChosen
            gIsChosen = it.gIsChosen
            pcsIsChosen = it.pcsIsChosen

            targetHeightOfTheColumn = it.targetHeightOfTheColumn
            targetHeightOfTheSurface = it.targetHeightOfTheSurface

            displayFieldWithInsertedDose = it.displayFieldWithInsertedDose
            displayFieldWithMeasuringUnits = it.displayFieldWithMeasuringUnits
            displayFieldWithTextField = it.displayFieldWithTextField
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
                    .height(heightOfTheSurface.value.dp),

                color = SecondaryColor,
                shape = RoundedCornerShape(Dimensions.roundedCornersOfBigElements),

                onClick = {

                    when (thisSegmentIsActive) {

//                      Collapse this segment
                        true -> {
                            addNewTrackieViewModel.deactivateSegment(
                                segmentToDeactivate = AddNewTrackieSegments.DailyDose
                            )

                            addNewTrackieViewModel.updateMeasuringUnitAndDose(
                                totalDose = totalDailyDose,
                                measuringUnit = measuringUnit.toString()
                            )

                            addNewTrackieViewModel.dailyDoseDisplayInsertedValue()
                        }

//                      Expand this segment
                        false -> {

                            addNewTrackieViewModel.activateSegment(
                                segmentToActivate = AddNewTrackieSegments.DailyDose
                            )

                            if (measuringUnit == null) {
                                addNewTrackieViewModel.dailyDoseDisplayMeasuringUnitsToChoose()
                            }

                            else {
                                addNewTrackieViewModel.dailyDoseDisplayTextField()
                            }
                        }
                    }
                },

                content = {

//                  Sets padding
                    Column(

                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),

                        content = {

//                          Displays what takes, may also display the premium logo
                            Column(

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp),

                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.SpaceBetween,

                                content = {
                                    textTitleMedium(content = "Daily dose")
                                    textTitleSmall(content = hint)
                                }
                            )

//                          Display inserted dose and measuring unit
                            AnimatedVisibility(

                                visible = displayFieldWithInsertedDose,
                                enter = fadeIn(animationSpec = tween(250)),
                                exit = fadeOut(animationSpec = tween(250)),

                                content = {

                                    Column(

                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(30.dp),

                                        horizontalAlignment = Alignment.Start,
                                        verticalArrangement = Arrangement.Center,

                                        content = {
                                            textTitleMedium(content = "$totalDailyDose $measuringUnit")
                                        }
                                    )
                                }
                            )

//                          Display field with measuring units
                            AnimatedVisibility(

                                visible = displayFieldWithMeasuringUnits,
                                enter = fadeIn(animationSpec = tween(250)),
                                exit = fadeOut(animationSpec = tween(250)),

                                content = {

                                    Column(

                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(76.dp),

                                        horizontalAlignment = Alignment.Start,
                                        verticalArrangement = Arrangement.SpaceBetween,

                                        content = {

//                                            Divider()

                                            Row(

                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(20.dp),

                                                horizontalArrangement = Arrangement.Start,
                                                verticalAlignment = Alignment.Bottom,

                                                content = {
                                                    textTitleSmall(content = "choose the measuring unit")
                                                }
                                            )

                                            Row(

                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(56.dp),

                                                horizontalArrangement = Arrangement.Start,
                                                verticalAlignment = Alignment.CenterVertically,

                                                content = {

                                                    mediumRadioTextButton(text = MeasuringUnits.ml, isSelected = mlIsChosen) {

                                                        addNewTrackieViewModel.dailyDoseInsertMeasuringUnit(measuringUnit = EnumMeasuringUnits.ml)
                                                        addNewTrackieViewModel.dailyDoseDisplayTextField()
                                                    }

                                                    Spacer(Modifier.width(5.dp))

                                                    mediumRadioTextButton(text = MeasuringUnits.g, isSelected = gIsChosen) {

                                                        addNewTrackieViewModel.dailyDoseInsertMeasuringUnit(measuringUnit = EnumMeasuringUnits.g)
                                                        addNewTrackieViewModel.dailyDoseDisplayTextField()
                                                    }

                                                    Spacer(Modifier.width(5.dp))

                                                    mediumRadioTextButton(text = MeasuringUnits.pcs, isSelected = pcsIsChosen) {

                                                        addNewTrackieViewModel.dailyDoseInsertMeasuringUnit(measuringUnit = EnumMeasuringUnits.pcs)
                                                        addNewTrackieViewModel.dailyDoseDisplayTextField()
                                                    }
                                                }
                                            )
                                        }
                                    )
                                }
                            )

//                          Display field with text field
                            AnimatedVisibility(

                                visible = displayFieldWithTextField,
                                enter = fadeIn(animationSpec = tween(250)),
                                exit = fadeOut(animationSpec = tween(250)),

                                content = {

                                    Column(

                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(76.dp),

                                        horizontalAlignment = Alignment.Start,
                                        verticalArrangement = Arrangement.SpaceBetween,

                                        content = {

//                                            Divider()

                                            Row(

                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(20.dp),

                                                horizontalArrangement = Arrangement.Start,
                                                verticalAlignment = Alignment.Bottom,

                                                content = {
                                                    textTitleSmall(content = "choose the total daily dose")
                                                }
                                            )

                                            TextField(

                                                value = if (totalDailyDose == 0) "" else "$totalDailyDose $measuringUnit",
                                                onValueChange = {
                                                    val newValue = it.filter { char -> char.isDigit() }
                                                    addNewTrackieViewModel.dailyDoseInsertTotalDose(totalDose = newValue.toIntOrNull() ?: 0 )
                                                },

                                                singleLine = true,
                                                enabled = true,

                                                colors = TextFieldDefaults.textFieldColors(

//                                                    textColor = White,
                                                    cursorColor = White,
                                                    unfocusedLabelColor = Color.Transparent,
                                                    focusedLabelColor = Color.Transparent,

                                                    containerColor = Color.Transparent,

                                                    unfocusedIndicatorColor = Color.Transparent,
                                                    focusedIndicatorColor = Color.Transparent
                                                ),

                                                textStyle = TextStyle.Default.copy(fontSize = 20.sp),

                                                keyboardOptions = KeyboardOptions(
                                                    keyboardType = KeyboardType.Number
                                                ),

                                                modifier = Modifier
                                                    .focusRequester(focusRequester)
                                                    .onGloballyPositioned { focusRequester.requestFocus() }
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
    )
}