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
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textHeadlineLarge
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.Dimensions
import com.example.trackies.ui.theme.Dimensions.heightOfUpperFragment

@Composable
fun welcomeScreen(onNavigate: (String) -> Unit) {

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

                        content = { textHeadlineLarge(content = "Hey there!") }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),

                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,

                        content = {

                            BigPrimaryButton(textToDisplay = "Sign up") { onNavigate("SignUpRoute") }

                            verticalSpacerS()

                            BigPrimaryButton(textToDisplay = "Sign in") { onNavigate("SignInRoute") }
                        }
                    )
                }
            )
        }
    )
}