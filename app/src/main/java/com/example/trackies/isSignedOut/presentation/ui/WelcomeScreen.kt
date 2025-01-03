package com.example.trackies.isSignedOut.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.trackies.ui.sharedUI.customButtons.BigPrimaryButton
import com.example.trackies.ui.sharedUI.customButtons.smallStaticSecondaryButton
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerM
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textHeadlineLarge
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.theme.BackgroundColor
import com.example.globalConstants.Dimensions
import com.example.globalConstants.Dimensions.heightOfUpperFragment

@Composable
fun welcomeScreen(
    onNavigateSignUp:() -> Unit,
    onNavigateSignIn:() -> Unit,
    onContinueAsGuest:() -> Unit
) {

//  Holds everything
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundColor),

        content = {

//          Sets padding holds components
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimensions.padding),

                verticalArrangement = Arrangement.SpaceBetween,

                content = {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(heightOfUpperFragment),

                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,

                        content = {

                            textHeadlineLarge(content = "Hey there!")
                        }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),

                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,

                        content = {

                            BigPrimaryButton(textToDisplay = "Sign up") {

                                onNavigateSignUp()
                            }

                            verticalSpacerS()

                            BigPrimaryButton(textToDisplay = "Sign in") {

                                onNavigateSignIn()
                            }

                            verticalSpacerM()

                            textTitleMedium(content = "or...")

                            verticalSpacerM()

                            smallStaticSecondaryButton(textToDisplay = "continue as guest") {

                                onContinueAsGuest()
                            }
                        }
                    )
                }
            )
        }
    )
}