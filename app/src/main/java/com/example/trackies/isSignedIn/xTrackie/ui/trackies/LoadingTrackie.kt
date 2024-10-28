package com.example.trackies.isSignedIn.xTrackie.ui.trackies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.sharedUI.customSpacers.horizontalSpacerS
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.theme.PrimaryColor
import com.example.trackies.ui.theme.SecondaryColor
import com.example.trackies.ui.theme.White50

@Composable
@Preview
fun loadingTrackie() {

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


                    Box(
                        modifier = Modifier
                            .width(125.dp)
                            .height(18.dp)
                            .background(
                                color = White50,
                                shape = RoundedCornerShape(18.dp)
                            )
                    )

                    verticalSpacerS()

                    Row(
                        modifier = Modifier
                            .height(12.dp)
                            .fillMaxWidth(),

                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Bottom,

                        content = {

                            Box (
                                modifier = Modifier
                                    .width( 80.dp )
                                    .height( 10.dp )

                                    .background(
                                        color = White50,
                                        shape = RoundedCornerShape(5.dp)
                                    ),

                                content = {
                                    Box (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height( 10.dp )
                                            .background(
                                                color = PrimaryColor,
                                                shape = RoundedCornerShape(5.dp)
                                            ),
                                    ) {}
                                }
                            )

                            horizontalSpacerS()

                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(10.dp)
                                    .background(color = White50, shape = RoundedCornerShape(18.dp))
                            )
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

                    Box(
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .background(color = White50, shape = RoundedCornerShape(5.dp))
                    )
                }
            )

            Box(
                modifier = Modifier
                    .width(110.dp)
                    .height(50.dp)
                    .background(color = PrimaryColor, shape = RoundedCornerShape(18.dp)),

                contentAlignment = Alignment.Center,

                content = {

                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(20.dp)
                            .background(color = White50, shape = RoundedCornerShape(20.dp))
                    )
                }
            )

            horizontalSpacerS()
        }
    )
}