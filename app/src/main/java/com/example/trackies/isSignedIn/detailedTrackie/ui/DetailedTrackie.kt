package com.example.trackies.isSignedIn.detailedTrackie.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardReturn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.trackies.isSignedIn.detailedTrackie.ui.loading.lowerPart.upperPartLoadedSuccessfully
import com.example.trackies.isSignedIn.detailedTrackie.ui.loading.upperPart.upperPartLoading
import com.example.trackies.isSignedIn.trackie.TrackieViewState
import com.example.trackies.ui.sharedUI.customButtons.iconButtonToNavigateBetweenActivities
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerL
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.Dimensions

@Composable
fun detailedTrackie(
    uiState: TrackieViewState?,
    onReturn: () -> Unit,
    onDelete: () -> Unit
) {

    Box(

        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundColor),

        content = {

            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = Dimensions.padding)
                    .background(color = BackgroundColor),

                verticalArrangement = Arrangement.SpaceBetween,

                content = {

//                  Upper part
                    Column(

                        modifier = Modifier
                            .fillMaxWidth(),

                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top,

                        content = {

                            iconButtonToNavigateBetweenActivities(icon = Icons.Rounded.KeyboardReturn) {
                                onReturn()
                            }

                            verticalSpacerL()

                            when (uiState) {

                                null -> {

                                    upperPartLoading()
                                }

                                else -> {

                                    upperPartLoadedSuccessfully(
                                        trackieViewState = uiState,
                                        onDelete = { onDelete() }
                                    )
                                }
                            }
                        }
                    )

//                  Lower part
                    Column(

                        modifier = Modifier
                            .fillMaxWidth(),

                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top,

                        content = {}
                    )
                }
            )
        }
    )
}