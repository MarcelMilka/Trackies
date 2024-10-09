package com.example.trackies.isSignedOut.presentation.ui.signIn

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import com.example.trackies.ui.sharedUI.customButtons.buttonChangingColor
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerM
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textHeadlineLarge
import com.example.trackies.ui.sharedUI.customText.textHeadlineSmall
import com.example.trackies.ui.sharedUI.customTextFields.EmailInputTextField
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.Dimensions

@Composable
fun recoverPassword(onClick: () -> Unit) {

    var email by remember { mutableStateOf("") }

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

                        content = { textHeadlineLarge(content = "Recover the password") }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),

                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,

                        content = {

                            verticalSpacerM()

                            textHeadlineSmall(content = "Type the email address you used to sign in to the app.")

                            verticalSpacerM()
                            verticalSpacerM()

                            EmailInputTextField(
                                insertedValue = {email = it},
                                focusRequester = FocusRequester(),
                                onFocusChanged = {},
                                onDone = {}
                            )

                            verticalSpacerS()

                            buttonChangingColor(
                                textToDisplay = "Recover the password",
                                isEnabled = email.isNotEmpty(),
                                onClick = {onClick()}
                            )
                        }
                    )
                }
            )
        }
    )
}