package com.example.trackies.isSignedIn.homeScreen.ui.loading.lowerPart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.theme.Dimensions
import com.example.trackies.ui.theme.SecondaryColor

@Composable
fun loadingRowWithRadioButtons() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .testTag(tag = "loadingRowWithRadioButtons"),

        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        repeat(3) {

            Spacer(Modifier.width(10.dp))

            Box(

                modifier = Modifier
                    .width(45.dp)
                    .height(15.dp)
                    .background(

                        color = SecondaryColor,
                        shape = RoundedCornerShape(size = Dimensions.roundedCornersOfSmallElements)
                    )
            )
        }
    }
}