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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.globalConstants.MeasuringUnit
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.dailyDose.staticValues.DailyDoseHintOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.dailyDose.staticValues.DailyDoseHeightOptions
import com.example.trackies.isSignedIn.addNewTrackie.vm.AddNewTrackieViewModel
import com.example.trackies.ui.sharedUI.customButtons.mediumRadioTextButton
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.theme.Dimensions
import com.example.trackies.ui.theme.SecondaryColor
import com.example.trackies.ui.theme.White50
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun dailyDose(
    enabledToAddNewTrackie: Boolean,
    addNewTrackieViewModel: AddNewTrackieViewModel,

    updateMeasuringUnit: (MeasuringUnit) -> Unit,
    updateDose: (Int) -> Unit,

    activate: () -> Unit,
    deactivate: () -> Unit,

    displayTrackiesPremiumDialog: () -> Unit
) {

//  AddNewTrackieModel-data:
    var measuringUnit: MeasuringUnit? by remember {

        mutableStateOf(null)
    }
    var dose by remember {

        mutableIntStateOf(0)
    }


//  DailyDoseViewState-data:
    var mlIsChosen by remember {

        mutableStateOf(false)
    }
    var gIsChosen by remember {

        mutableStateOf(false)
    }
    var pcsIsChosen by remember {

        mutableStateOf(false)
    }

    var targetHeightOfTheSurface by remember {

        mutableIntStateOf(
            DailyDoseHeightOptions.displayUnactivatedSegment
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

    var displayFieldWithInsertedDose by remember {

        mutableStateOf(false)
    }
    var displayFieldWithMeasuringUnits by remember {

        mutableStateOf(false)
    }
    var displayFieldWithTextField by remember {

        mutableStateOf(false)
    }

    var hint by remember {

        mutableStateOf(
            DailyDoseHintOptions.insertDailyDose
        )
    }


//  Segment-specific values
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = if (dose == 0) {""} else {"$dose"},
                selection = TextRange.Zero
            )
        )
    }
    val focusRequester = remember {

        FocusRequester()
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

                measuringUnit = addNewTrackieModel.measuringUnit.also { measuringUnit ->

                    when (measuringUnit) {

                        MeasuringUnit.ml -> {

                            mlIsChosen = true
                            gIsChosen = false
                            pcsIsChosen = false
                        }

                        MeasuringUnit.g -> {

                            mlIsChosen = false
                            gIsChosen = true
                            pcsIsChosen = false
                        }

                        MeasuringUnit.pcs -> {

                            mlIsChosen = false
                            gIsChosen = false
                            pcsIsChosen = true
                        }

                        null -> {

                            mlIsChosen = false
                            gIsChosen = false
                            pcsIsChosen = false
                        }
                    }
                }
                dose = addNewTrackieModel.dose
            }
        }

//      Collect changes from 'dailyDoseViewState'
        this.launch {

            addNewTrackieViewModel.dailyDoseViewState.collect { dailyDoseViewState ->

                targetHeightOfTheSurface = dailyDoseViewState.targetHeightOfTheSurface

                displayFieldWithInsertedDose = dailyDoseViewState.displayFieldWithInsertedDose
                displayFieldWithMeasuringUnits = dailyDoseViewState.displayFieldWithMeasuringUnits
                displayFieldWithTextField = dailyDoseViewState.displayFieldWithTextField
                hint = dailyDoseViewState.hint
            }
        }

//      Control whether this segment is active, or not
        this.launch {

            addNewTrackieViewModel.activityStatesOfSegments.collect {

                thisSegmentIsActive = when (it.dailyDoseIsActive) {

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
            .testTag("dailyDose"),

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
                    .padding(10.dp),

                content = {

//                  This column displays "Daily dose" and an appropriate hint
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

//                  Display inserted dose and measuring unit
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
                                    textTitleMedium(content = "$dose $measuringUnit")
                                }
                            )
                        }
                    )

//                  Display field with measuring units
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

                                            mediumRadioTextButton(text = "${MeasuringUnit.ml}", isSelected = mlIsChosen) {

                                                mlIsChosen = true
                                                gIsChosen = false
                                                pcsIsChosen = false

                                                updateMeasuringUnit(MeasuringUnit.ml)
                                            }

                                            verticalSpacerS()

                                            mediumRadioTextButton(text = "${MeasuringUnit.g}", isSelected = gIsChosen) {

                                                mlIsChosen = false
                                                gIsChosen = true
                                                pcsIsChosen = false

                                                updateMeasuringUnit(MeasuringUnit.g)
                                            }

                                            verticalSpacerS()

                                            mediumRadioTextButton(text = "${MeasuringUnit.pcs}", isSelected = pcsIsChosen) {

                                                mlIsChosen = false
                                                gIsChosen = false
                                                pcsIsChosen = true

                                                updateMeasuringUnit(MeasuringUnit.pcs)
                                            }
                                        }
                                    )
                                }
                            )
                        }
                    )

//                  Display field with text field
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

                                    Row(

                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(20.dp),

                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.Bottom,

                                        content = {
                                            textTitleSmall(content = "insert the daily dose")
                                        }
                                    )

                                    TextField(

                                        value = textFieldValue,
                                        onValueChange = {


                                            val filteredText = it.text.filter { it.isDigit() }
                                            textFieldValue = it.copy(text = filteredText)

                                            updateDose(textFieldValue.text.toIntOrNull() ?: 0)
                                        },

                                        singleLine = true,
                                        enabled = true,

                                        colors = TextFieldDefaults.textFieldColors(
                                            cursorColor = White,
                                            unfocusedLabelColor = Color.Transparent,
                                            focusedLabelColor = Color.Transparent,

                                            containerColor = Color.Transparent,

                                            unfocusedIndicatorColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent
                                        ),

                                        textStyle = TextStyle
                                            .Default
                                            .copy(
                                                fontSize = 20.sp,
                                                color = White,
                                                textMotion = TextMotion.Animated,
                                            ),

                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number
                                        ),

                                        modifier = Modifier
                                            .focusRequester(focusRequester)
                                            .onGloballyPositioned {
                                                focusRequester.requestFocus()
                                            }
                                            .testTag("dailyDose - text field")
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