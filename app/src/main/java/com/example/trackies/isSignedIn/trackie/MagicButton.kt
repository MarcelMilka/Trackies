package com.example.trackies.isSignedIn.trackie

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.theme.PrimaryColor
import com.example.trackies.ui.theme.TrackieMarkedAsIngested
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@Composable
fun magicButton(
    totalDose: Int,
    measuringUnit: String,
    onMarkAsIngested: () -> Unit
) {

    var isChecked by remember { mutableStateOf(false) }

    var targetWidthOfButton by remember { mutableIntStateOf(110) } // 70 or 110
    val animatedWidthOfButton by animateIntAsState(
        targetValue = targetWidthOfButton,
        animationSpec = tween(
            durationMillis = 250,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ),
        label = "",
    )

    val targetColorOfButton = if (isChecked) TrackieMarkedAsIngested else PrimaryColor
    val animatedColorOfButton by animateColorAsState(targetValue = targetColorOfButton)

    var buttonIsHeld by remember { mutableStateOf(false) }

    var widthOfSurface by remember { mutableIntStateOf(0) }

    var heightOfSurface by remember { mutableIntStateOf(0) }

    Surface(

        color = animatedColorOfButton,
        shape = RoundedCornerShape(18.dp),

        modifier = Modifier
            .width(animatedWidthOfButton.dp)
            .height(50.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {

                        if (animatedWidthOfButton != 70) {

                            buttonIsHeld = true
                            val startTime = System.currentTimeMillis()

                            val coroutineScope = CoroutineScope(Dispatchers.Default).launch {

                                while (buttonIsHeld) {

                                    if ((System.currentTimeMillis() - startTime) >= 2000L) {

                                        isChecked = true
                                        targetWidthOfButton = 70

                                        launch {
                                            onMarkAsIngested()
                                        }.join()

                                        cancel()
                                    }

                                    else {

                                        widthOfSurface = (((System.currentTimeMillis() - startTime) * 70) / 2000L).toInt()
                                        heightOfSurface = (((System.currentTimeMillis() - startTime) * 50) / 2000L).toInt()
                                    }
                                }

                                if (!buttonIsHeld) {

                                    widthOfSurface = 0
                                    heightOfSurface = 0
                                }
                            }

                            tryAwaitRelease()
                            coroutineScope.cancel()
                            buttonIsHeld = false
                        }
                    }
                )
            },

        content = {

            Box(

                modifier = Modifier.fillMaxSize(),

                contentAlignment = Alignment.Center,

                content = {

//                  Orange surface which expands while holding the button
                    Surface(

                        modifier = Modifier
                            .width(widthOfSurface.dp)
                            .height(heightOfSurface.dp),

                        color = TrackieMarkedAsIngested,

                        shape = RoundedCornerShape(18.dp),

                        content = {}
                    )

//                  Dose for today
                    AnimatedVisibility(

                        visible = !isChecked,
                        enter = fadeIn(animationSpec = tween(250)),
                        exit = fadeOut(animationSpec = tween(250)),

                        content = {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),

                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.Bottom,

                                content = {

                                    textTitleMedium(content = "+$totalDose")

                                    textTitleSmall(content = measuringUnit)

                                }
                            )
                        }
                    )

//                  Icon which shows that the goal has been achieved for today
                    AnimatedVisibility(

                        visible = isChecked,
                        enter = fadeIn(animationSpec = tween(250)),
                        exit = fadeOut(animationSpec = tween(250)),

                        content = {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),

                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.Bottom,

                                content = {

                                    Icon(

                                        imageVector = Icons.Rounded.EmojiEvents,
                                        tint = White,
                                        contentDescription = null
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