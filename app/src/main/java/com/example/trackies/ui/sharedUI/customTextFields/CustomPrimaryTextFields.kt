package com.example.trackies.ui.sharedUI.customTextFields

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.theme.PrimaryColor
import com.example.trackies.ui.theme.fonts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailInputTextField (
    insertedValue: (String) -> Unit,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    onDone: () -> Unit
) {

    var insertedText by remember { mutableStateOf("") }

    TextField(

        value = insertedText,
        onValueChange = { newValue ->
            insertedValue( newValue )
            insertedText = newValue
        },


        label = { Text(text = "Email", style = fonts.titleMedium) },

        modifier = Modifier
            .width(300.dp)
            .height(60.dp)
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (it.isFocused) {
                    onFocusChanged(true)
                }

                else if (!it.isFocused){
                    onFocusChanged(false)
                }
            },

        colors = TextFieldDefaults.textFieldColors(

            unfocusedLabelColor = White,
            focusedLabelColor = White,

            containerColor = PrimaryColor,

            cursorColor = White,

            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),


        shape = RoundedCornerShape( 20.dp ),

        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email,
            capitalization = KeyboardCapitalization.None
        ),

        keyboardActions = KeyboardActions(
            onDone = {onDone()},
        ),


        singleLine = true,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInputTextField (
    insertedValue: (String) -> Unit,
    assignedFocusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    onDone: () -> Unit ){

    var insertedText by remember { mutableStateOf("") }

    var passwordIsVisible by remember { mutableStateOf( false ) }

    var icon by remember { mutableStateOf(Icons.Rounded.Visibility) }
    LaunchedEffect( passwordIsVisible ) {
        icon = when (passwordIsVisible) {
            true -> {
                Icons.Rounded.Visibility
            }

            false -> {
                Icons.Rounded.VisibilityOff
            }
        }
    }

    TextField(

        value = insertedText,
        onValueChange = { newValue ->
            insertedValue( newValue )
            insertedText = newValue
        },

        label = { Text(text = "Password", style = fonts.titleMedium) },

        modifier = Modifier
            .width(300.dp)
            .height(60.dp)
            .focusRequester( assignedFocusRequester )
            .onFocusChanged {
                if (it.isFocused) {
                    onFocusChanged(true)
                }

                else if (!it.isFocused){
                    onFocusChanged(false)
                }
            },

        colors = TextFieldDefaults.textFieldColors(

//            textColor = White,

            unfocusedLabelColor = White,
            focusedLabelColor = White,

            containerColor = PrimaryColor,

            cursorColor = White,

            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),

        shape = RoundedCornerShape( 20.dp ),

        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password,
            capitalization = KeyboardCapitalization.None
        ),

        keyboardActions = KeyboardActions(onDone = {onDone()}),

        trailingIcon = {

            IconButton(

                onClick = {
                    passwordIsVisible = !passwordIsVisible
                },

                content = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = White
                    )
                },
            )
        },

        singleLine = true,

        visualTransformation = if (passwordIsVisible) VisualTransformation.None else PasswordVisualTransformation(),
    )
}