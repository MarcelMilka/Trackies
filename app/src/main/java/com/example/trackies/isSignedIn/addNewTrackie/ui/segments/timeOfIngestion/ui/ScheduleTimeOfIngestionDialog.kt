package com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.theme.BackgroundColor
import com.example.globalConstants.Dimensions
import com.example.trackies.ui.theme.PrimaryColor
import com.example.trackies.ui.theme.SecondaryColor
import com.example.trackies.ui.theme.White50
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun scheduleTimeOfIngestionDialog(
    onConfirm: () -> Unit,
    onDecline: () -> Unit,
) {

    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )

//  Whole screen
    Column(

        modifier = Modifier.fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,

        content = {

//          Upper half of screen
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

                content = {

//                  Main part
                    Box(

                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .fillMaxHeight(0.7f)
                            .background(BackgroundColor, RoundedCornerShape(Dimensions.roundedCornersOfBigElements)),

                        content = {

                            Column(

                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(Dimensions.padding),

                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.SpaceBetween,

                                content = {

                                    Column {

                                        textTitleMedium(content = "Schedule time of ingestion.")
                                    }

                                    TimeInput(
                                        state = timePickerState,
                                        colors = TimePickerColors(
                                            clockDialColor = SecondaryColor, // Defines the background color of the clock dial where hours or minutes are displayed and selectable.
                                            selectorColor = White, // The color of the main selector element (handle/knob) used to select the time on the clock dial.
                                            containerColor = PrimaryColor, // Sets the main background container color of the entire time picker component.
                                            periodSelectorBorderColor = Transparent, // The color of the border around the AM/PM selector, used in 12-hour format time pickers.
                                            clockDialSelectedContentColor = White, // Color of the numbers or content on the clock dial when selected by the user.
                                            clockDialUnselectedContentColor = White50, // Color of the unselected numbers or content on the clock dial.
                                            periodSelectorSelectedContainerColor = Transparent, // Background color of the container when AM/PM period is selected.
                                            periodSelectorUnselectedContainerColor = Transparent, // Background color of the container when AM/PM period is not selected.
                                            periodSelectorSelectedContentColor = Transparent, // The text or content color for the selected AM/PM period, to indicate selection.
                                            periodSelectorUnselectedContentColor = Transparent, // The color of the text or content in the unselected AM/PM period.
                                            timeSelectorSelectedContainerColor = PrimaryColor, // Background color of the selected time segment container (e.g., hours or minutes).
                                            timeSelectorUnselectedContainerColor = PrimaryColor, // Background color of the unselected time segment container.
                                            timeSelectorSelectedContentColor = White, // Color of the text or content in the selected time segment for visibility.
                                            timeSelectorUnselectedContentColor = White, // The color of the text or content in unselected time segments.
                                        )
                                    )

                                    Row (
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        Button(

                                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                                            content = {

                                                textTitleMedium(content = "Confirm")
                                            },
                                            onClick = {

                                                onConfirm()
                                            }
                                        )

                                        Button(

                                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                                            content = {

                                                textTitleMedium(content = "Decline")
                                            },
                                            onClick = {

                                                onDecline()
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