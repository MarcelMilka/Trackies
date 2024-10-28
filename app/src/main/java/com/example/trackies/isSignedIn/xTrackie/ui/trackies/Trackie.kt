package com.example.trackies.isSignedIn.xTrackie.ui.trackies

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.isSignedIn.xTrackie.ui.componentsOfTrackies.magicButton
import com.example.trackies.isSignedIn.xTrackie.ui.componentsOfTrackies.magicButtonMarkedAsIngested
import com.example.trackies.isSignedIn.xTrackie.ui.componentsOfTrackies.trackieProgressBar
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.ui.sharedUI.customButtons.iconButton
import com.example.trackies.ui.sharedUI.customSpacers.horizontalSpacerS
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.theme.SecondaryColor

@Composable fun trackie(
    trackieViewState: TrackieModel,
    stateOfTheTrackie: Boolean,
    onMarkAsIngested: () -> Unit,
    onDisplayDetails: () -> Unit
) {

    var targetValueOfProgressBar by remember { mutableIntStateOf(0) }
    var targetValueOfCurrentDose by remember { mutableIntStateOf(0) }

    when (stateOfTheTrackie) {

        true -> {

            targetValueOfProgressBar = 100
            targetValueOfCurrentDose = trackieViewState.totalDose
        }
        false -> {

            targetValueOfProgressBar = 0
            targetValueOfCurrentDose = 0
        }
    }

    val progressOfTheProgressBar by animateIntAsState(
        targetValue = targetValueOfProgressBar,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 50,
            easing = LinearOutSlowInEasing
        ),
        label = "",
    )

    val progressOfTheCurrentDose by animateIntAsState(
        targetValue = targetValueOfCurrentDose,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 50,
            easing = LinearOutSlowInEasing
        ),
        label = "",
    )


    Row(

        modifier = Modifier

            .fillMaxWidth()
            .height(60.dp)
            .background(
                color = SecondaryColor,
                shape = RoundedCornerShape(20.dp)
            ),

        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,

        content = {

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 10.dp, top = 5.dp, bottom = 5.dp)
                    .weight(2f, true),

                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,

                content = {

                    textTitleMedium(content = trackieViewState.name)

                    verticalSpacerS()

                    Row(
                        modifier = Modifier
                            .height(12.dp)
                            .fillMaxWidth(),

                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Bottom,

                        content = {

                            trackieProgressBar(progress = progressOfTheProgressBar)

                            textTitleSmall(content = " $progressOfTheCurrentDose/${trackieViewState.totalDose} ${trackieViewState.measuringUnit}")
                        }
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(50.dp),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

                content = {
                    iconButton(
                        icon = Icons.Rounded.Search,
                        onClick = {
                            onDisplayDetails()
                        }
                    )
                }
            )

            when (stateOfTheTrackie) {

                true -> {

                    magicButtonMarkedAsIngested()

                    horizontalSpacerS()

                }

                false -> {

                    magicButton(
                        totalDose = trackieViewState.totalDose,
                        measuringUnit = trackieViewState.measuringUnit,
                        onMarkAsIngested = {
                            onMarkAsIngested()
                        }
                    )

                    horizontalSpacerS()

                }
            }
        }
    )
}