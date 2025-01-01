package com.example.trackies.isSignedOut.presentation.ui.signIn.signIn

import androidx.compose.animation.AnimatedVisibility
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
import com.example.trackies.auth.buisness.Credentials
import com.example.trackies.ui.sharedUI.customButtons.smallStaticSecondaryButton
import com.example.trackies.ui.sharedUI.customButtons.buttonChangingColor
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerM
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textHeadlineLarge
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.sharedUI.customTextFields.emailInputTextField
import com.example.trackies.ui.sharedUI.customTextFields.passwordInputTextField
import com.example.trackies.ui.theme.BackgroundColor
import com.example.globalConstants.Dimensions

@Composable
fun signIn(
    emailRelatedError: Boolean,
    emailRelatedErrorDescription: String,

    passwordRelatedError: Boolean,
    passwordRelatedErrorDescription: String,

    onHideError: (SignInErrors) -> Unit,

    onSignIn: (Credentials) -> Unit,
    onRecoverPassword: () -> Unit
) {

//  focus requesters responsible for switching between text fields
    var emailFocusRequester = FocusRequester()
    var emailTextFieldIsActive by remember { mutableStateOf(false) }

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

                            Column(
                                modifier = Modifier,
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center,
                                content = {

                                    emailInputTextField(
                                        insertedValue = {
                                            email = it

                                            if (emailRelatedError) {
                                                onHideError(SignInErrors.EmailRelatedError)
                                            }
                                        },
                                        focusRequester = emailFocusRequester,
                                        onFocusChanged = {emailTextFieldIsActive = it},
                                        onDone = {
                                            passwordFocusRequester.requestFocus()
                                            passwordTextFieldIsActive = true
                                        }
                                    )

                                    AnimatedVisibility(
                                        visible = emailRelatedError
                                    ) {
                                        textTitleSmall(content = emailRelatedErrorDescription)
                                    }
                                }
                            )

                            verticalSpacerS()

                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center,
                                content = {

                                    passwordInputTextField(
                                        insertedValue = {
                                            password = it

                                            if (passwordRelatedError) {
                                                onHideError(SignInErrors.PasswordRelatedError)
                                            }
                                        },
                                        assignedFocusRequester = passwordFocusRequester,
                                        onFocusChanged = {},
                                        onDone = {
                                            passwordFocusRequester.freeFocus()
                                            passwordTextFieldIsActive = false
                                        }
                                    )

                                    AnimatedVisibility(
                                        visible = passwordRelatedError
                                    ) {
                                        textTitleSmall(content = passwordRelatedErrorDescription)
                                    }
                                }
                            )

                            verticalSpacerM()

                            buttonChangingColor(
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

                            smallStaticSecondaryButton(
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