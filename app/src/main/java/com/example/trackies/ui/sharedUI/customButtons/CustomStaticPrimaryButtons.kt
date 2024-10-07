package com.example.trackies.ui.sharedUI.customButtons

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.theme.PrimaryColor

@Composable
fun BigPrimaryButton(textToDisplay: String, onClick: () -> Unit) {

    Button(

        onClick = { onClick() },

        modifier = Modifier
            .width(250.dp)
            .height(50.dp),

        shape = RoundedCornerShape(20.dp),

        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),

        content = {textTitleMedium(content = textToDisplay)}
    )
}