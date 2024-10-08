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
import com.example.trackies.isSignedOut.buisness.Credentials
import com.example.trackies.ui.sharedUI.customButtons.BigDynamicButton
import com.example.trackies.ui.sharedUI.customButtons.SmallStaticSeondaryButton
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerM
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textHeadlineLarge
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customTextFields.EmailInputTextField
import com.example.trackies.ui.sharedUI.customTextFields.PasswordInputTextField
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.Dimensions

@Composable
fun signIn(
    onSignIn: (Credentials) -> Unit,
    onRecoverPassword: () -> Unit
) {

//  focus requesters responsible for switching between text fields
    var emailTextFieldIsActive by remember { mutableStateOf(false) }
    var emailFocusRequester = FocusRequester()

    var passwordFocusRequester = FocusRequester()
    var passwordTextFieldIsActive by remember { mutableStateOf(false) }

//  registration credentials
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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

                        content = { textHeadlineLarge(content = "Sign in") }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),

                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,

                        content = {

                            verticalSpacerM()

                            EmailInputTextField(
                                insertedValue = {email = it},
                                focusRequester = emailFocusRequester,
                                onFocusChanged = {emailTextFieldIsActive = it},
                                onDone = {
                                    passwordFocusRequester.requestFocus()
                                    passwordTextFieldIsActive = true
                                }
                            )

                            verticalSpacerS()

                            PasswordInputTextField(
                                insertedValue = {password = it},
                                assignedFocusRequester = passwordFocusRequester,
                                onFocusChanged = {},
                                onDone = {
                                    passwordFocusRequester.freeFocus()
                                    passwordTextFieldIsActive = false
                                }
                            )

                            verticalSpacerM()

                            BigDynamicButton(
                                textToDisplay = "Sign in",
                                isEnabled = email.isNotEmpty() && password.isNotEmpty(),
                                onClick = {

                                    onSignIn(
                                        Credentials(
                                            email = email,
                                            password = password
                                        )
                                    )
                                }
                            )

                            verticalSpacerM()

                            textTitleMedium("Have you forgotten the password?")

                            verticalSpacerS()

                            SmallStaticSeondaryButton(
                                textToDisplay = "Recover the password",
                                onClick = { onRecoverPassword() }
                            )
                        }
                    )
                }
            )
        }
    )
}