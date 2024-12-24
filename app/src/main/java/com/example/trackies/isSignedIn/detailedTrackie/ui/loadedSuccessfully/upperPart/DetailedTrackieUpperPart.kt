package com.example.trackies.isSignedIn.detailedTrackie.ui.loadedSuccessfully.upperPart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.ui.sharedUI.customButtons.iconButton
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.boldTextTitleMedium
import com.example.trackies.ui.sharedUI.customText.textHeadlineMedium
import com.example.trackies.ui.sharedUI.customText.textTitleMedium

@Composable
fun detailedTrackieUpperPart (
    trackieModel: TrackieModel,
    onDelete: () -> Unit
) {

    Column(

        modifier = Modifier
            .fillMaxWidth()
            .testTag("detailedTrackieUpperPart"),

        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,

        content = {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,

                content = {

                    textHeadlineMedium(content = trackieModel.name)

                    iconButton(
                        icon = Icons.Rounded.Delete,
                        onClick = {
                            onDelete()
                        }
                    )
                }
            )

            verticalSpacerS()

            boldTextTitleMedium(content = "Scheduled days")
            textTitleMedium(content = abbreviateDaysOfWeek(trackieModel.repeatOn))

            verticalSpacerS()

            boldTextTitleMedium(content = "Daily dose")
            textTitleMedium(content = "${trackieModel.totalDose} ${trackieModel.measuringUnit}")

        }
    )
}

private fun abbreviateDaysOfWeek(list: List<String>): String =

    list
        .map { dayOfWeek ->

            dayOfWeek.substring(0..2)
        }
        .joinToString { abbreviatedDayOfWeek ->

            abbreviatedDayOfWeek
        }