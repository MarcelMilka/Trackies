package com.example.trackies.isSignedIn.addNewTrackie.presentation.timeOfIngestion

import android.util.Log
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
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.trackies.isSignedIn.addNewTrackie.buisness.TimeOfIngestion
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.Dimensions
import com.example.trackies.ui.theme.PrimaryColor
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dialTimePicker(
    onConfirm: (TimeOfIngestion) -> Unit,
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

//          Upper half of the screen
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
                            .fillMaxWidth()
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

                                    textTitleMedium(content = "Choose ingestion time.")

                                    verticalSpacerS()

                                    TimeInput(
                                        state = timePickerState,
                                    )

                                    Row (
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,

                                        content = {

                                            Button(

                                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),

                                                content = {
                                                    textTitleMedium(content = "Decline")
                                                },

                                                onClick = {
                                                    onDecline()
                                                }
                                            )

                                            Button(

                                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),

                                                content = {
                                                    textTitleMedium(content = "Confirm")
                                                },

                                                onClick = {
                                                    onConfirm(
                                                        TimeOfIngestion(
                                                            hour = timePickerState.hour,
                                                            minute = timePickerState.minute
                                                        )
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
    )
}