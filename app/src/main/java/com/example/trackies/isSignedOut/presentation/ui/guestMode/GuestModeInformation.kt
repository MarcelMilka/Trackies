package com.example.trackies.isSignedOut.presentation.ui.guestMode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerM
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.boldTextTitleMedium
import com.example.trackies.ui.sharedUI.customText.textHeadlineLarge
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.Dimensions
import com.example.trackies.ui.theme.PrimaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun guestModeInformation(
    onContinue: () -> Unit,
    onMoveBack: () -> Unit,
) {

    Box(

        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundColor),

        content = {

            Column(

                modifier = Modifier
                    .padding(all = Dimensions.padding),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,

                content = {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(3f, true),

                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,

                        content = {

                            textHeadlineLarge(content = "Before you continue as a guest.")
                        }
                    )

                    Column(

                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(7f, true),

                        verticalArrangement = Arrangement.SpaceBetween,

                        content = {

                            Column(

                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.SpaceBetween,
                                content = {

                                    verticalSpacerM()

                                    textTitleMedium(
                                        content = "Guest mode skips the account creation process, allowing you to start using the app right away. However, there are a few limitations in guest mode:"
                                    )

                                    verticalSpacerS()

                                    boldTextTitleMedium(
                                        content = "No access to database."
                                    )
                                    textTitleSmall(
                                        content = "Your data is not stored in the database, so it can only be accessed from THIS device and will be lost if you uninstall Trackies."
                                    )

                                    verticalSpacerS()

                                    boldTextTitleMedium(
                                        content = "Limited amount of substances."
                                    )
                                    textTitleSmall(
                                        content = "You have a limited number of substances you can add."
                                    )

                                    verticalSpacerS()

                                    boldTextTitleMedium(
                                        content = "Only basic functionalities."
                                    )
                                    textTitleSmall(
                                        content = "You cannot use widgets or receive notifications to remind you about substances left to ingest for today."
                                    )

                                    verticalSpacerS()

                                    textTitleMedium(
                                        content = "You can always turn your account to normal mode with possibility to unlock full potential of Trackies."
                                    )
                                }
                            )

                            Row(

                                Modifier
                                    .fillMaxWidth(),

                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom,

                                content = {

                                    Button(

                                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                                        content = {

                                            textTitleMedium(content = "Move back")
                                        },
                                        onClick = {

                                            onMoveBack()
                                        }
                                    )

                                    Button(

                                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                                        content = {

                                            textTitleMedium(content = "Continue as a guest")
                                        },
                                        onClick = {


                                            onContinue()
                                        }
                                    )
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}