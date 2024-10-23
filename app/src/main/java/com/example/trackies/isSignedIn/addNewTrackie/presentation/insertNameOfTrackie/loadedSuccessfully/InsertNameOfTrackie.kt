package com.example.trackies.isSignedIn.addNewTrackie.presentation.insertNameOfTrackie.loadedSuccessfully

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
import com.example.trackies.isSignedIn.addNewTrackie.buisness.AddNewTrackieSegments
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
@Composable fun insertNameOfTrackie(viewModel: AddNewTrackieViewModel) {

    var nameOfTrackie by remember { mutableStateOf("") }

    var targetHeightOfTheColumn by remember {
        mutableIntStateOf(InsertNameOfTrackieFixedHeightValues.displayUnactivatedComponent)
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

    var targetHeightOfTheSurface by remember { mutableIntStateOf(50) }
    val heightOfTheSurface by animateIntAsState(
        targetValue = targetHeightOfTheSurface,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 50,
            easing = LinearOutSlowInEasing
        ),
        label = "",
    )

    var displayFieldWithInsertedName by remember { mutableStateOf(false) }
    var displayFieldWithTextField by remember { mutableStateOf(false) }
    var hint by remember {
        mutableStateOf(NameOfTrackieHints.insertNewName)
    }

    var error by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember {
        FocusRequester()
    }

//  Control whether this segment is active
    var thisSegmentIsActive by remember { mutableStateOf(false) }
    CoroutineScope(Dispatchers.Default).launch {

        viewModel.statesOfSegments.collect { statesOfSegments ->
            thisSegmentIsActive = statesOfSegments.insertNameIsActive

            when(thisSegmentIsActive) {

                true -> {viewModel.nameOfTrackieDisplayTextField(nameOfTrackie)}

                false -> {

                    if (nameOfTrackie != "") {
                        viewModel.nameOfTrackieDisplayInsertedValue(nameOfTrackie = nameOfTrackie)
                    }

                    else {
                        viewModel.nameOfTrackieDisplayCollapsed()
                    }
                }
            }
        }
    }

//  Update values
    CoroutineScope(Dispatchers.Default).launch {

        viewModel.insertNameOfTrackieViewState.collect {

            nameOfTrackie = it.nameOfTrackie

            targetHeightOfTheColumn = it.targetHeightOfTheColumn
            targetHeightOfTheSurface = it.targetHeightOfTheSurface

            displayFieldWithInsertedName = it.displayFieldWithInsertedName
            displayFieldWithTextField = it.displayFieldWithTextField

            hint = it.hint

            error = it.error
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

//                      Collapse
                        true -> {

                            viewModel.deactivateSegment(segmentToDeactivate = AddNewTrackieSegments.NameOfTrackie)
                            viewModel.updateName(nameOfTheNewTrackie = nameOfTrackie)

//                          Collapse completely
                            if (nameOfTrackie == "") {

                                viewModel.nameOfTrackieDisplayCollapsed()
                            }

//                          Collapse to display inserted name of trackie
                            else {

                                viewModel.nameOfTrackieDisplayInsertedValue(nameOfTrackie = nameOfTrackie)
                            }

                            keyboardController?.hide()
                        }

//                      Expand to display text field
                        false -> {

                            viewModel.activateSegment(segmentToActivate = AddNewTrackieSegments.NameOfTrackie)

                            viewModel.nameOfTrackieDisplayTextField(nameOfTrackie)

                            keyboardController?.show()
                        }
                    }

                    thisSegmentIsActive = !thisSegmentIsActive
                },
                content = {

//                  This column sets padding
                    Column(

                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dimensions.roundedCornersOfMediumElements)

                    ) {

//                      This column displays what is requested
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

//                      Depending on a value of displayFieldWithInsertedName, displays inserted name of the trackie
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
                                        textTitleMedium(content = nameOfTrackie)
                                    }
                                )
                            }
                        )

//                      Depending on a value of displayFieldWithTextField, display a text field
                        AnimatedVisibility(

                            modifier = Modifier,

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

                                            value = nameOfTrackie,
                                            onValueChange = {

                                                viewModel.nameOfTrackieInsertNewName(nameOfTrackie = it)
                                            },

                                            isError = error,

                                            singleLine = true,

                                            colors = TextFieldDefaults.textFieldColors(

//                                                textColor = if (error) {Red} else {White},
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
                                                .onGloballyPositioned { focusRequester.requestFocus() }
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