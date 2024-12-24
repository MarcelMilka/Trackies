package com.example.trackies.isSignedIn.detailedTrackie.ui.loading.upperPart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.theme.Dimensions
import com.example.trackies.ui.theme.SecondaryColor

@Composable
fun loadingUpperPartOfDetailedTrackie () {

    Column(

        modifier = Modifier
            .fillMaxWidth()
            .testTag("loadingUpperPartOfDetailedTrackie"),

        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,

        content = {

//          Header which displays name of the trackie.
            Row(
                modifier = Modifier
                    .fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,

                content = {

//                  Medium header which displays name of the trackie
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .height(30.dp),

                        shape = RoundedCornerShape(20.dp),

                        color = SecondaryColor
                    ) {}

//                  IconButton
                    Surface(
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp),

                        shape = RoundedCornerShape(30.dp),

                        color = SecondaryColor
                    ) {}
                }
            )

            verticalSpacerS()

//          Text "Scheduled days" and days of week, on which a trackie should be ingested
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .height(35.dp),

                shape = RoundedCornerShape(size = Dimensions.roundedCornersOfMediumElements),

                color = SecondaryColor,

                content = {}
            )

            verticalSpacerS()


//          Text "Daily dose" and daily dose of the trackie
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .height(35.dp),

                shape = RoundedCornerShape(size = Dimensions.roundedCornersOfMediumElements),

                color = SecondaryColor,

                content = {}
            )
        }
    )
}