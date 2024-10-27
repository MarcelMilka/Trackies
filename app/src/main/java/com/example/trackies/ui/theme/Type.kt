package com.example.trackies.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.trackies.R

val quickSandBold = FontFamily(
    Font(R.font.quicksand_bold)
)

val quickSandMedium = FontFamily(
    Font(R.font.quicksand_medium)
)

val quickSandRegular = FontFamily(
    Font(R.font.quicksand_regular)
)

val quickSandLight = FontFamily(
    Font(R.font.quicksand_light)
)

val fonts = Typography(

//  Use this to mark the most important information.
    headlineLarge = TextStyle(
        fontFamily = quickSandBold,
        fontSize = 40.sp,
        color = White
    ),

//  Use this to describe particular parts of the app e.g. "Settings" or "Your today's trackies".
    headlineMedium = TextStyle(
        fontFamily = quickSandBold,
        fontWeight = FontWeight.W700,
        fontSize = 25.sp,
        color = White
    ),

//  Use this to express additional information under large headline.
    headlineSmall = TextStyle(
        fontFamily = quickSandLight,
        fontSize = 20.sp,
        color = White
    ),

//  Use this in buttons, as text buttons
    titleLarge = TextStyle(
        fontFamily = quickSandBold,
        fontSize = 15.sp,
        color = White
    ),

//  Use this in buttons, as text buttons
    titleMedium = TextStyle(
        fontFamily = quickSandLight,
        fontSize = 15.sp,
        color = White
    ),

    titleSmall = TextStyle(
        fontFamily = quickSandLight,
        fontSize = 10.sp,
        color = White
    ),
)