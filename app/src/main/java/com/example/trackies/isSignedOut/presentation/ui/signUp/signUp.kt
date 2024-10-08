package com.example.trackies.isSignedOut.presentation.ui.signUp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.sharedUI.customButtons.BigDynamicButton
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerM
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textHeadlineLarge
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.sharedUI.customTextFields.EmailInputTextField
import com.example.trackies.ui.sharedUI.customTextFields.PasswordInputTextField
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.Dimensions

@Composable
fun signUp(onNavigate: () -> Unit) {

//  focus requesters responsible for switching between text fields
    var emailTextFieldIsActive by remember { mutableStateOf(false) }

    var passwordTextFieldIsActive by remember { mutableStateOf(false) }

//  registration credentials
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

//  counters of letters, characters, digits and big letters
    var amountOfLetters by remember { mutableIntStateOf(0) }
    var amountOfSpecialCharacters by remember { mutableIntStateOf(0) }
    var amountOfBigLetters by remember { mutableIntStateOf(0) }
    var amountOfDigits by remember { mutableIntStateOf(0) }

//  enable/disable the button responsible for signing in
    var buttonContinueIsEnabled by remember { mutableStateOf(false) }
    LaunchedEffect(email, password) {

        val specialCharacters = listOf("!", "@", "#", "$", "%", "^", "&", "*", "(", ")")

        amountOfBigLetters = password.count { it.isUpperCase() }
        amountOfSpecialCharacters = password.count { specialCharacters.contains(it.toString()) }
        amountOfLetters = password.length
        amountOfDigits = password.count {it.isDigit()}

        buttonContinueIsEnabled = email.isNotEmpty() &&
                amountOfLetters >= 12 &&
                amountOfSpecialCharacters >= 1 &&
                amountOfBigLetters >= 1 &&
                amountOfDigits >= 1
    }

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

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

                        content = { textHeadlineLarge(content = "Sign up") }
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
                                insertedValue = { password = it },
                                assignedFocusRequester = passwordFocusRequester,
                                onFocusChanged = {passwordTextFieldIsActive = it},
                                onDone = {
                                    passwordFocusRequester.freeFocus()
                                    passwordTextFieldIsActive = false
                                }
                            )

                            verticalSpacerS()

                            AnimatedVisibility(
                                visible = passwordTextFieldIsActive,
                            ) {

                                Column (

                                    modifier = Modifier
                                        .width(300.dp),

                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.Start,

                                    content = {

                                        textTitleMedium( content = "Password requirements:")
                                        textTitleSmall( content = "$amountOfLetters/12 characters")
                                        textTitleSmall( content = "$amountOfSpecialCharacters/1 special character (! @ # $ % ^ & * ( ) )")
                                        textTitleSmall( content = "$amountOfBigLetters/1 big letter")
                                        textTitleSmall( content = "$amountOfDigits/1 digit")
                                    }
                                )
                            }

                            verticalSpacerM()

                            BigDynamicButton(
                                textToDisplay = "Continue",
                                isEnabled = buttonContinueIsEnabled,
                                onClick = {onNavigate()}
                            )
                        }
                    )
                }
            )
        }
    )
}
