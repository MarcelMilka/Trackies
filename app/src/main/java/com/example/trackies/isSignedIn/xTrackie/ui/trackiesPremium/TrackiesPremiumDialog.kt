package com.example.trackies.isSignedIn.xTrackie.ui.trackiesPremium

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AllInclusive
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackies.ui.sharedUI.customSpacers.horizontalSpacerS
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.PrimaryColor
import com.example.trackies.ui.theme.White50
import com.example.trackies.ui.theme.quickSandBold

@Composable
fun trackiesPremiumDialog(onReturn: () -> Unit) {

    Column(

        modifier = Modifier.fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

        content = {

            Column(

                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.7f)
                    .background(BackgroundColor, RoundedCornerShape(20.dp)),

                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,

                content = {

                    Column(

                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.3f)
                            .background(PrimaryColor, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),

                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top,

                        content = {

                            Column(

                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),

                                content = {

                                    Row(

                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(28.dp),

                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,

                                        content = {

                                            Row(

                                                modifier = Modifier
                                                    .height(30.dp),

                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically,

                                                content = {

                                                    Text(
                                                        text = "Trackies",
                                                        fontFamily = quickSandBold,
                                                        fontSize = 25.sp,
                                                        color = White
                                                    )

                                                    horizontalSpacerS()

                                                    trackiesPremiumLogo()
                                                }
                                            )

                                            IconButton(

                                                onClick = {

                                                    onReturn()
                                                },

                                                content = {
                                                    Icon(
                                                        imageVector = Icons.Rounded.Close,
                                                        contentDescription = null,
                                                        tint = White,
                                                    )
                                                }
                                            )
                                        }
                                    )

                                    verticalSpacerS()

                                    Column(

                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f, true),

                                        horizontalAlignment = Alignment.Start,
                                        verticalArrangement = Arrangement.Top,

                                        content = {

                                            textTitleMedium(content = "Access the full potential of trackies for " +
                                                    "just $9.99 + tax/month, or with annual subscription."
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )

//                  Content on the blackish space
                    Column (

                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),

                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,

                        content = {

                            feature(
                                feature = "",
                                freeVersion = {

                                    Text(
                                        text = "free",
                                        fontFamily = quickSandBold,
                                        fontSize = 10.sp,
                                        color = White
                                    )
                                },
                                premiumVersion = {

                                    trackiesPremiumLogo()
                                }
                            )

                            verticalSpacerS()

                            Divider(color = White50)

                            verticalSpacerS()

                            feature(
                                feature = "Limit of trackies",
                                freeVersion = { textTitleMedium("1") },
                                premiumVersion = { Icon(Icons.Rounded.AllInclusive, null, tint = White) }
                            )

                            verticalSpacerS()

                            feature(
                                feature = "Notifications",
                                freeVersion = { Icon(Icons.Rounded.Remove, null, tint = White) },
                                premiumVersion = { Icon(Icons.Rounded.Check, null, tint = White) }
                            )

                            verticalSpacerS()

                            feature(
                                feature = "Widgets",
                                freeVersion = { Icon(Icons.Rounded.Remove, null, tint = White) },
                                premiumVersion = { Icon(Icons.Rounded.Check, null, tint = White) }
                            )
                            verticalSpacerS()

                            feature(
                                feature = "Ingestion time",
                                freeVersion = { Icon(Icons.Rounded.Remove, null, tint = White) },
                                premiumVersion = { Icon(Icons.Rounded.Check, null, tint = White) }
                            )
                        }
                    )
                }
            )
        }
    )
}