package com.example.trackies.isSignedIn.detailedTrackie.ui

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.Dimensions
import com.example.trackies.ui.theme.PrimaryColor

@Composable
fun confirmDeletionOfTheTrackie(
    onConfirm: () -> Unit,
    onDecline: () -> Unit
) {

    Column(

        modifier = Modifier.fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

        content = {

            Box(

                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.2f)
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

                                textTitleMedium(content = "Delete the Trackie?")

                                verticalSpacerS()

                                textTitleSmall(content = "It will not be possible to retrieve it back.")
                            }

                            Row (

                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,

                                content = {

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
                            )
                        }
                    )
                }
            )
        }
    )
}