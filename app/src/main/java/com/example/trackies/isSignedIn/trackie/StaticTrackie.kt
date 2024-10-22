package com.example.trackies.isSignedIn.trackie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.sharedUI.customButtons.iconButtonDetails
import com.example.trackies.ui.sharedUI.customSpacers.horizontalSpacerS
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.theme.SecondaryColor

@Composable
fun staticTrackie(
    name: String,
    totalDose: Int,
    measuringUnit: String,
    repeatOn: List<String>,
    ingestionTime: Map<String, Int>?,

    onDisplayDetails: () -> Unit
) {

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

                    textTitleMedium(content = name)

                    verticalSpacerS()

                    Row(
                        modifier = Modifier
                            .height(12.dp)
                            .fillMaxWidth(),

                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Bottom,

                        content = {

                            trackieProgressBar(progress = 0)

                            textTitleSmall(content = " 0/$totalDose $measuringUnit")

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

                    iconButtonDetails(
                        icon = Icons.Rounded.Search,
                        onClick = {
                            onDisplayDetails()
                        }
                    )
                }
            )

            horizontalSpacerS()

        }
    )
}