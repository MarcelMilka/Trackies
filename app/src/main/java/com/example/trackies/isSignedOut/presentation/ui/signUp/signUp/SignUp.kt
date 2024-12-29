package com.example.trackies.isSignedOut.presentation.ui.signUp.signUp

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
import com.example.trackies.auth.buisness.Credentials
import com.example.trackies.ui.sharedUI.customButtons.buttonChangingColor
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerM
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.dynamicTextTitleSmallWhiteToGreen
import com.example.trackies.ui.sharedUI.customText.textHeadlineLarge
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.sharedUI.customTextFields.EmailInputTextField
import com.example.trackies.ui.sharedUI.customTextFields.PasswordInputTextField
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.Dimensions

@Composable
fun signUp(
    anErrorOccurred: Boolean,
    errorDescription: String,
    onHideError: () -> Unit,

    onSignUp: (Credentials) -> Unit
) {

//  focus requesters responsible for switching between text fields
    var emailTextFieldIsActive by remember { mutableStateOf(false) }

    var passwordTextFieldIsActive by remember { mutableStateOf(false) }

//  registration credentials
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

//  counters of letters, characters, digits and big letters
    var amountOfLetters by remember { mutableIntStateOf(0) }
    var sufficientAmountOfLetters by remember { mutableStateOf(false) }

    var amountOfSpecialCharacters by remember { mutableIntStateOf(0) }
    var sufficientAmountOfSpecialCharacters by remember { mutableStateOf(false) }

    var amountOfBigLetters by remember { mutableIntStateOf(0) }
    var sufficientAmountOfBigLetters by remember { mutableStateOf(false) }

    var amountOfDigits by remember { mutableIntStateOf(0) }
    var sufficientAmountOfDigits by remember { mutableStateOf(false) }

    var buttonContinueIsEnabled by remember { mutableStateOf(false) }
    LaunchedEffect(password) {

        val specialCharacters = listOf("!", "@", "#", "$", "%", "^", "&", "*", "(", ")")

        amountOfLetters = password.length
        sufficientAmountOfLetters = password.length >= 12

        amountOfSpecialCharacters = password.count {specialCharacters.contains(it.toString())}
        sufficientAmountOfSpecialCharacters = password.count {specialCharacters.contains(it.toString())} >= 1

        amountOfBigLetters = password.count { it.isUpperCase() }
        sufficientAmountOfBigLetters = password.count { it.isUpperCase() } >= 1

        amountOfDigits = password.count {it.isDigit()}
        sufficientAmountOfDigits = password.count {it.isDigit()} >= 1

        buttonContinueIsEnabled =
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

                            Column(
                                modifier = Modifier,
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center,
                                content = {

                                    EmailInputTextField(
                                        insertedValue = {
                                            email = it

                                            if (anErrorOccurred) {
                                                onHideError()
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
                                        visible = anErrorOccurred
                                    ) {
                                        textTitleSmall(content = errorDescription)
                                    }
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
                                        dynamicTextTitleSmallWhiteToGreen(isGreen = sufficientAmountOfLetters, content = "$amountOfLetters/12 characters")
                                        dynamicTextTitleSmallWhiteToGreen(isGreen = sufficientAmountOfSpecialCharacters, content = "$amountOfSpecialCharacters/1 special character (! @ # $ % ^ & * ( ) )")
                                        dynamicTextTitleSmallWhiteToGreen(isGreen = sufficientAmountOfBigLetters, content = "$amountOfBigLetters/1 big letter")
                                        dynamicTextTitleSmallWhiteToGreen(isGreen = sufficientAmountOfDigits, content = "$amountOfDigits/1 digit")
                                    }
                                )
                            }

                            verticalSpacerM()

                            buttonChangingColor(
                                textToDisplay = "Continue",
                                isEnabled = buttonContinueIsEnabled,
                                onClick = {
                                    onSignUp(
                                        Credentials(
                                            email = email,
                                            password = password
                                        )
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
