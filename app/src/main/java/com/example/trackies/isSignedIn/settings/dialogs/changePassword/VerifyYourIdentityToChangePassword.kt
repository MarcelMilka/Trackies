package com.example.trackies.isSignedIn.settings.dialogs.changePassword

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import com.example.trackies.ui.sharedUI.customButtons.mediumButtonChangingColor
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerS
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.sharedUI.customTextFields.PasswordInputTextField
import com.example.trackies.ui.theme.BackgroundColor
import com.example.trackies.ui.theme.Dimensions
import com.example.trackies.ui.theme.PrimaryColor

@Composable
fun verifyYourIdentityToChangePassword(
    passwordIsIncorrect: Boolean,
    onConfirm: (String) -> Unit,
    onDecline: () -> Unit,
    onHideNotification: () -> Unit
) {

    var currentPassword by remember { mutableStateOf("") }
    var focusRequester = remember { FocusRequester() }

    LaunchedEffect(true) {

        focusRequester.requestFocus()
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
                            .fillMaxHeight(0.7f)
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
                                        textTitleMedium(content = "Verify your identity to change the password.")
                                    }

                                    Column {

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(),

                                            verticalArrangement = Arrangement.Top,
                                            horizontalAlignment = Alignment.Start,
                                            content = {

                                                PasswordInputTextField(
                                                    insertedValue = {
                                                        currentPassword = it

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

                                        AnimatedVisibility(
                                            visible = passwordIsIncorrect
                                        ) {
                                            textTitleSmall(content = "You inserted wrong password.")
                                        }
                                    }

                                    Row (
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        mediumButtonChangingColor(
                                            textToDisplay = "Verify",
                                            isEnabled = currentPassword != "",
                                            onClick = {
                                                onConfirm(currentPassword)
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