package com.example.trackies.isSignedIn.xTrackie.ui.trackiesPremium

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.sharedUI.customText.textTitleMedium

@Composable
fun feature(
    feature: String,
    freeVersion: @Composable () -> Unit,
    premiumVersion: @Composable () -> Unit
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

        content = {

            Row(

                modifier = Modifier
                    .weight(5f, true)
                    .fillMaxHeight(),

                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,

                content = {

                    textTitleMedium(content = feature)
                }
            )

            Row(

                modifier = Modifier
                    .weight(2f, true)
                    .fillMaxHeight(),

                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,

                content = { freeVersion() }
            )

            Row(
                modifier = Modifier
                    .width(60.dp)
                    .fillMaxHeight(),

                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,

                content = { premiumVersion() }
            )
        }
    )
}