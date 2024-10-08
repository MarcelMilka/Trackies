package com.example.trackies.isSignedOut.presentation.ui.signUp

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
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerM
import com.example.trackies.ui.sharedUI.customText.textHeadlineLarge
import com.example.trackies.ui.sharedUI.customText.textHeadlineSmall
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.Dimensions

@Composable
fun authenticate(onNavigate: () -> Unit) {

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
                            .fillMaxHeight(Dimensions.heightOfUpperFragment),

                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,

                        content = { textHeadlineLarge(content = "Check your e-mail") }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),

                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround,

                        content = {

                            textHeadlineSmall(
                                content = "We have sent you an email with a verification link. Click the link to confirm your e-mail address and return to the app."
                            )

                            verticalSpacerM()

                            BigPrimaryButton(textToDisplay = "Okay!") { onNavigate() }
                        }
                    )
                }
            )
        }
    )
}