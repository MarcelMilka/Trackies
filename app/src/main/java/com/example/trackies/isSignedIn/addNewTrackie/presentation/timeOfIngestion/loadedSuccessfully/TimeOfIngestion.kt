package com.example.trackies.isSignedIn.addNewTrackie.presentation.timeOfIngestion.loadedSuccessfully

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.isSignedIn.addNewTrackie.presentation.scheduleDays.loadedSuccessfully.mediumSelectableTextButton
import com.example.trackies.isSignedIn.addNewTrackie.vm.AddNewTrackieViewModel
import com.example.trackies.isSignedIn.constantValues.DaysOfWeek
import com.example.trackies.isSignedIn.trackie.premiumLogo
import com.example.trackies.isSignedIn.user.vm.SharedViewModelViewState
import com.example.trackies.ui.sharedUI.customSpacers.horizontalSpacerS
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.theme.Dimensions
import com.example.trackies.ui.theme.SecondaryColor

@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun timeOfIngestion(
    sharedViewModelViewState: SharedViewModelViewState,
    viewModel: AddNewTrackieViewModel
) {

//  Holder of the surface and the supporting text
    Column(

        modifier = Modifier
            .fillMaxWidth()
            .height(height = TimeOfIngestionSetOfHeights.displayUnactivatedComponent.dp),

        content = {

//          Background of the composable, adjusts height of the whole composable and displays appropriate data
            Surface(

                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = TimeOfIngestionSetOfHeights.displayUnactivatedComponent.dp),

                color = SecondaryColor,
                shape = RoundedCornerShape(Dimensions.roundedCornersOfBigElements),

                onClick = {},

                content = {

//                  Sets padding
                    Column(

                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dimensions.roundedCornersOfMediumElements)
                    ) {

//                      Displays "Time of ingestion", hints and depending on the license: logo of Trackies Premium
                        Row(

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp),

                            horizontalArrangement = Arrangement.SpaceBetween,

                            content = {

                                Column(

                                    modifier = Modifier
                                        .height(30.dp),

                                    horizontalAlignment = Alignment.Start,
                                    verticalArrangement = Arrangement.SpaceBetween,

                                    content = {

                                        textTitleMedium(content = "Time of ingestion")
                                        textTitleSmall(content = TimeOfIngestionHints.scheduleTimeOfIngestion)
                                    }
                                )

                                Column(

                                    modifier = Modifier
                                        .height(30.dp),

                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,

                                    content = {

                                        premiumLogo()
                                    }
                                )
                            }
                        )

//                        AnimatedVisibility(
//
//                            visible = ,
//                            enter = fadeIn(animationSpec = tween(250)),
//                            exit = fadeOut(animationSpec = tween(250)),
//
//                            content = {
//
//                                Row(
//
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .height(56.dp),
//
//                                    horizontalArrangement = Arrangement.SpaceBetween,
//                                    verticalAlignment = Alignment.CenterVertically,
//
//                                    content = {
//
//                                    }
//                                )
//                            }
//                        )
                    }
                }
            )
        }
    )
}