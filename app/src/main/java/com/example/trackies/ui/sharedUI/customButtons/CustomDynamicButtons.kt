package com.example.trackies.ui.sharedUI.customButtons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.theme.PrimaryColor
import com.example.trackies.ui.theme.SecondaryColor
import com.example.trackies.ui.theme.White50
import com.example.trackies.ui.theme.fonts

@Composable
fun BigDynamicButton(textToDisplay: String, isEnabled: Boolean, onClick: () -> Unit ) {

    val animateEnabling by animateColorAsState(

        targetValue = if (isEnabled) {
            PrimaryColor
        }
        else {
            SecondaryColor
        },
        animationSpec = tween(250, 0, easing = EaseIn), label = ""
    )

    Button(

        onClick = { onClick() },

        modifier = Modifier
            .width(250.dp)
            .height(50.dp),

        shape = RoundedCornerShape(20.dp),

        colors = ButtonDefaults.buttonColors(
            containerColor = animateEnabling,
            contentColor = Color.White,

            disabledContentColor = White50
        ),

        enabled = isEnabled

    ) {
        Text(text = textToDisplay, style = fonts.titleMedium)
    }
}