package com.example.trackies.isSignedIn.settings.dialogs.changePassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.trackies.ui.sharedUI.customButtons.mediumButtonChangingColor
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.dynamicTextTitleSmallWhiteToGreen
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customTextFields.passwordInputTextField
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.Dimensions
import com.example.trackies.ui.theme.PrimaryColor

@Composable
fun insertNewPassword(
    passwordIsIncorrect: Boolean,
    onConfirm: (String) -> Unit,
    onDecline: () -> Unit,
    onHideNotification: () -> Unit
) {

    var newPassword by remember { mutableStateOf("") }
    var focusRequester = remember { FocusRequester() }

    LaunchedEffect(true) {

        focusRequester.requestFocus()
    }

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
    LaunchedEffect(newPassword) {

        val specialCharacters = listOf("!", "@", "#", "$", "%", "^", "&", "*", "(", ")")

        amountOfLetters = newPassword.length
        sufficientAmountOfLetters = newPassword.length >= 12

        amountOfSpecialCharacters = newPassword.count {specialCharacters.contains(it.toString())}
        sufficientAmountOfSpecialCharacters = newPassword.count {specialCharacters.contains(it.toString())} >= 1

        amountOfBigLetters = newPassword.count { it.isUpperCase() }
        sufficientAmountOfBigLetters = newPassword.count { it.isUpperCase() } >= 1

        amountOfDigits = newPassword.count {it.isDigit()}
        sufficientAmountOfDigits = newPassword.count {it.isDigit()} >= 1

        buttonContinueIsEnabled =
                amountOfLetters >= 12 &&
                amountOfSpecialCharacters >= 1 &&
                amountOfBigLetters >= 1 &&
                amountOfDigits >= 1
    }

//  Whole screen
    Column(

        modifier = Modifier.fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,

        content = {

//          Upper half of screen
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

                content = {

//                  Main part
                    Box(

                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .fillMaxHeight()
                            .background(BackgroundColor, RoundedCornerShape(Dimensions.roundedCornersOfBigElements)),

                        content = {

                            Column(

                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(Dimensions.padding),

                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.SpaceBetween,

                                content = {

                                    Column {
                                        textTitleMedium(content = "Insert your new password.")
                                    }

                                    Column {

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(),

                                            verticalArrangement = Arrangement.Top,
                                            horizontalAlignment = Alignment.Start,
                                            content = {

                                                passwordInputTextField(
                                                    insertedValue = {
                                                        newPassword = it

                                                        if (passwordIsIncorrect) {
                                                            onHideNotification()
                                                        }
                                                    },
                                                    assignedFocusRequester = focusRequester,
                                                    onFocusChanged = {},
                                                    onDone = {}
                                                )
                                            }
                                        )

                                        verticalSpacerS()

                                        textTitleMedium(content = "Password requirements:")
                                        dynamicTextTitleSmallWhiteToGreen(isGreen = sufficientAmountOfLetters, content = "$amountOfLetters/12 characters")
                                        dynamicTextTitleSmallWhiteToGreen(isGreen = sufficientAmountOfSpecialCharacters, content = "$amountOfSpecialCharacters/1 special character (! @ # $ % ^ & * ( ) )")
                                        dynamicTextTitleSmallWhiteToGreen(isGreen = sufficientAmountOfBigLetters, content = "$amountOfBigLetters/1 big letter")
                                        dynamicTextTitleSmallWhiteToGreen(isGreen = sufficientAmountOfDigits, content = "$amountOfDigits/1 digit")
                                    }

                                    Row (
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        mediumButtonChangingColor(
                                            textToDisplay = "Continue",
                                            isEnabled = buttonContinueIsEnabled,
                                            onClick = {
                                                onConfirm(newPassword)
                                            }
                                        )

                                        Button(

                                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                                            content = {
                                                textTitleMedium(content = "Decline")
                                            },
                                            onClick = {
                                                onDecline()
                                            }
                                        )
                                    }
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}