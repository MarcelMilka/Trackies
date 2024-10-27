package com.example.trackies.ui.sharedUI.customText

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import com.example.trackies.ui.theme.fonts

// Use this to mark the most important information.
@Composable fun textHeadlineLarge(content: String) {

    Text(
        text = content,
        style = fonts.headlineLarge,
        textAlign = TextAlign.Center,
    )
}

// Use this to describe particular parts of the app e.g. "Settings" or "Your today's trackies".
@Composable fun textHeadlineMedium(content: String) {

    Text(
        text = content,
        style = fonts.headlineMedium
    )
}

// Use this to express additional information under large headline.
@Composable fun textHeadlineSmall(content: String) {

    Text(
        text = content,
        style = fonts.headlineSmall,
        textAlign = TextAlign.Center,
    )
}

// Use this in buttons, as text buttons
@Composable fun textTitleMedium(content: String) {

    Text(
        text = content,
        style = fonts.titleMedium
    )
}

@Composable fun boldTextTitleMedium(content: String) {

    Text(
        text = content,
        style = fonts.titleLarge
    )
}

@Composable fun textTitleSmall(content: String) {

    Text(
        text = content,
        style = fonts.titleSmall
    )
}