package com.example.trackies.isSignedIn.addNewTrackie.ui.segments.nameOfTrackie.ui

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.nameOfTrackie.staticValues.NameOfTrackieHeightOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.nameOfTrackie.staticValues.NameOfTrackieHintOptions
import com.example.trackies.isSignedIn.addNewTrackie.vm.AddNewTrackieViewModel
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.theme.Dimensions
import com.example.trackies.ui.theme.SecondaryColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun nameOfTrackie(
    addNewTrackieViewModel: AddNewTrackieViewModel,
    updateName: (String) -> Unit,
    activate: () -> Unit,
    deactivate: () -> Unit,
) {

//  AddNewTrackieModel-data:
    var name by remember {
        mutableStateOf("")
    }

//  NameOfTrackieViewState-data:
    var targetHeightOfTheSurface by remember {
        mutableIntStateOf(NameOfTrackieHeightOptions.displayUnactivatedComponent)
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

    var displayFieldWithInsertedName by remember {
        mutableStateOf(false)
    }
    var displayFieldWithTextField by remember {
        mutableStateOf(false)
    }
    var hint by remember {
        mutableStateOf(NameOfTrackieHintOptions.insertNewName)
    }

//  Segment-specific values
    val keyboardController = LocalSoftwareKeyboardController.current
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

                name = addNewTrackieModel.name
            }
        }

//      Collect changes from 'nameOfTrackieViewState'
        this.launch {

            addNewTrackieViewModel.nameOfTrackieViewState.collect { nameOfTrackieViewState ->

                targetHeightOfTheSurface = nameOfTrackieViewState.targetHeightOfTheSurface
                displayFieldWithInsertedName = nameOfTrackieViewState.displayFieldWithInsertedName
                displayFieldWithTextField = nameOfTrackieViewState.displayFieldWithTextField
                hint = nameOfTrackieViewState.hint
            }
        }

//      Control whether this segment is active, or not
        this.launch {

            addNewTrackieViewModel.activityStatesOfSegments.collect {

                thisSegmentIsActive = when (it.nameOfTrackieIsActive) {

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

//  Background of the composable, adjusts height of the whole composable and displays appropriate data (hint, inserted/chosen values)
    Surface(

        modifier = Modifier
            .fillMaxWidth()
            .height(heightOfTheSurface.dp),

        color = SecondaryColor,
        shape = RoundedCornerShape(Dimensions.roundedCornersOfBigElements),

        onClick = {

            when (thisSegmentIsActive) {

//              Collapse
                true -> {

                    deactivate()
                    keyboardController?.hide()
                }

//              Expand
                false -> {

                    activate()
                    keyboardController?.show()
                }
            }
        },

        content = {

//          This column sets padding
            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimensions.roundedCornersOfMediumElements)

            ) {

//              This column displays "Name of trackie" and an appropriate hint
                Column(

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),

                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween,

                    content = {

                        textTitleMedium(content = "Name of trackie")
                        textTitleSmall(content = hint)
                    }
                )

//              Depending on a value of displayFieldWithInsertedName, displays inserted name of the trackie
                AnimatedVisibility(

                    visible = displayFieldWithInsertedName,
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
                                textTitleMedium(content = name)
                            }
                        )
                    }
                )

//              Depending on a value of displayFieldWithTextField, display a text field
                AnimatedVisibility(

                    visible = displayFieldWithTextField,
                    enter = fadeIn(animationSpec = tween(250)),
                    exit = fadeOut(animationSpec = tween(250)),

                    content = {

                        Column(

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),

                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.SpaceBetween,

                            content = {

                                TextField(

                                    value = name,
                                    onValueChange = {

                                        updateName(it)
                                    },

                                    singleLine = true,

                                    colors = TextFieldDefaults.textFieldColors(

//                                      textColor = if (error) {Red} else {White},
                                        cursorColor = White,
                                        unfocusedLabelColor = White,
                                        focusedLabelColor = Transparent,

                                        errorCursorColor = Red,

                                        containerColor = Transparent,

                                        unfocusedIndicatorColor = Transparent,
                                        focusedIndicatorColor = Transparent,
                                        errorIndicatorColor = Transparent
                                    ),

                                    textStyle = TextStyle.Default.copy(fontSize = 20.sp),

                                    modifier = Modifier
                                        .focusRequester(focusRequester)
                                        .onGloballyPositioned {
                                            focusRequester.requestFocus()
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