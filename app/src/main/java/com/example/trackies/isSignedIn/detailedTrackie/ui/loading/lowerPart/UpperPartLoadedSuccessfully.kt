package com.example.trackies.isSignedIn.detailedTrackie.ui.loading.lowerPart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.trackies.isSignedIn.trackie.TrackieViewState
import com.example.trackies.ui.sharedUI.customButtons.iconButton
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerL
import com.example.trackies.ui.sharedUI.customText.textHeadlineMedium
import com.example.trackies.ui.sharedUI.customText.textTitleMedium

@Composable
fun upperPartLoadedSuccessfully (
    trackieViewState: TrackieViewState,
    onDelete: () -> Unit
) {

    Column(

        modifier = Modifier
            .fillMaxWidth(),

        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,

        content = {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,

                content = {

                    textHeadlineMedium(content = trackieViewState.name)

                    iconButton(
                        icon = Icons.Rounded.Delete,
                        onClick = {
                            onDelete()
                        }
                    )
                }
            )

            verticalSpacerL()

            textHeadlineMedium(content = "Scheduled days")
            textTitleMedium(content = abbreviateDaysOfWeek(trackieViewState.repeatOn))

            verticalSpacerL()

            textHeadlineMedium(content = "Daily dose")
            textTitleMedium(content = "${trackieViewState.totalDose} ${trackieViewState.measuringUnit}")

        }
    )
}

private fun abbreviateDaysOfWeek(list: List<String>): String {

    return list
        .map { dayOfWeek ->

            dayOfWeek.substring(0..2)
        }
        .joinToString {abbreviatedDayOfWeek ->

            abbreviatedDayOfWeek
        }
}