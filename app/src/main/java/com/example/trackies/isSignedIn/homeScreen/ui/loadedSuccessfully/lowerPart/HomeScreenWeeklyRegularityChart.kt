package com.example.trackies.isSignedIn.homeScreen.ui.loadedSuccessfully.lowerPart

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.globalConstants.CurrentDateTime
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelViewState
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.theme.PrimaryColor
import com.example.trackies.ui.theme.SecondaryColor
import com.example.trackies.ui.theme.White50
import com.example.trackies.ui.theme.fonts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun homeScreenWeeklyRegularityChart(
    sharedViewModelUiState: SharedViewModelViewState.LoadedSuccessfully
) {

    val currentDayOfWeek = CurrentDateTime.getCurrentDayOfWeek()
    var activatedBar: String? by remember {
        mutableStateOf(currentDayOfWeek)
    }

    Row (

        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .testTag(tag = "homeScreenWeeklyRegularityChart"),

        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top,

        content = {

            sharedViewModelUiState.weeklyRegularity.forEach {

                Column(

                    modifier = Modifier
                        .weight(1f, true)
                        .height(180.dp),

                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,

                    content = {

                        Column(

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),

                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,

                            content = {


                                val total = it.value.keys.toIntArray()[0]
                                val ingested = it.value.values.toIntArray()[0]

                                val targetPercentage =
                                    if (total == 0 || ingested == 0 ) {
                                        0
                                    }
                                    else {
                                        ingested * 100 / total
                                    }

                                val percentage by animateIntAsState(
                                    targetValue = targetPercentage,
                                    animationSpec = tween(
                                        durationMillis = 1000,
                                        delayMillis = 50,
                                        easing = LinearOutSlowInEasing
                                    ),
                                    label = "",
                                )

                                val targetHeight =
                                    if (total == 0 || ingested == 0) { 0 }
                                    else { ingested * 130 / total }

                                val height by animateIntAsState(
                                    targetValue = targetHeight,
                                    animationSpec = tween(
                                        durationMillis = 1000,
                                        delayMillis = 50,
                                        easing = LinearOutSlowInEasing
                                    ),
                                    label = "",
                                )

                                val color = if (activatedBar == it.key) { PrimaryColor } else { SecondaryColor }

                                if (activatedBar == it.key) {

                                    Surface(

                                        modifier = Modifier
                                            .fillMaxWidth(0.95f)
                                            .height(20.dp)
                                            .testTag("activated bar ${it.key}"),

                                        shape = RoundedCornerShape(5.dp),

                                        color = PrimaryColor,

                                        content = {

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth(),

                                                horizontalArrangement = Arrangement.Center,
                                                verticalAlignment = Alignment.Bottom,

                                                content = {

                                                    textTitleMedium(content = "$percentage")

                                                    textTitleSmall(content = "%")

                                                }
                                            )
                                        }
                                    )

                                    Spacer(Modifier.height(10.dp))
                                }

                                Surface(

                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .height(height.dp)
                                        .testTag("bar ${it.key}"),

                                    shape = RoundedCornerShape(5.dp),

                                    color = color,

                                    onClick = { activatedBar = if (activatedBar == it.key) { null } else { it.key } },

                                    content = {}
                                )
                            }
                        )

                        Box(

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp),

                            contentAlignment = Alignment.Center,

                            content = {

                                val color = if (it.key == currentDayOfWeek) { PrimaryColor } else { White50 }

                                Text(
                                    text = "${it.key.subSequence(0, 3)}",
                                    style = fonts.titleMedium,
                                    color = color
                                )
                            }
                        )
                    }
                )
            }
        }
    )
}