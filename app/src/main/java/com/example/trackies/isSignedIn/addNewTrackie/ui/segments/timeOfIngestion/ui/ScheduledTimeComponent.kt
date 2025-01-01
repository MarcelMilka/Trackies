package com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.sharedUI.customButtons.iconButton
import com.example.trackies.ui.sharedUI.customButtons.smallIconButton
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.globalConstants.Dimensions
import com.example.trackies.ui.theme.SecondaryColor

@Composable
fun scheduledTimeComponent(
    displayButtons: Boolean,
    hour: String,
    minute: String,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

//  Holder of the whole content
    Surface(

        modifier = Modifier
            .fillMaxSize(),

        shape = RoundedCornerShape(size = Dimensions.roundedCornersOfBigElements),

        color = SecondaryColor,

        shadowElevation = 10.dp,

        onClick = {
            onClick()
        },

        content = {

//          Sets padding, holds elements of UI
            Row(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 10.dp,
                        top = 5.dp,
                        bottom = 5.dp,
                        end = 10.dp
                    ),

                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,

                content = {

//                  Displays scheduled time and icon button to edit time
                    Row(
                        modifier = Modifier
                            .fillMaxHeight(),

                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,

                        content = {

                            textTitleMedium(content = "$hour:$minute")

                            AnimatedVisibility(
                                visible = displayButtons,
                                enter = fadeIn(animationSpec = tween(10)),
                                exit = fadeOut(animationSpec = tween(10)),
                            ) {

                                smallIconButton(
                                    icon = Icons.Rounded.Edit,
                                    onClick = {
                                        onEdit()
                                    }
                                )
                            }
                        }
                    )

//                  Displays icon button to delete scheduled time
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(),

                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,

                        content = {

                            AnimatedVisibility(
                                visible = displayButtons,
                                enter = fadeIn(animationSpec = tween(10)),
                                exit = fadeOut(animationSpec = tween(10)),
                                content = {

                                    iconButton(
                                        icon = Icons.Rounded.Delete,
                                        onClick = {
                                            onDelete()
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