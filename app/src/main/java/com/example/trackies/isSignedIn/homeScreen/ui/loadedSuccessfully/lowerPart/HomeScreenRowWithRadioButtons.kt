package com.example.trackies.isSignedIn.homeScreen.ui.loadedSuccessfully.lowerPart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.trackies.isSignedIn.homeScreen.buisness.HomeScreenChartToDisplay
import com.example.trackies.ui.sharedUI.customButtons.mediumRadioTextButton

@Composable
fun rowWithRadioButtons(
    graphToDisplay: HomeScreenChartToDisplay,
    switchChart: (HomeScreenChartToDisplay) -> Unit
) {

    var displayWeeklyChart by remember { mutableStateOf(true) }
    var displayMonthlyChart by remember { mutableStateOf(false) }
    var displayYearlyChart by remember { mutableStateOf(false) }

    when (graphToDisplay) {

        HomeScreenChartToDisplay.Weekly -> {

            displayWeeklyChart = true
            displayMonthlyChart = false
            displayYearlyChart = false
        }

        HomeScreenChartToDisplay.Monthly -> {

            displayWeeklyChart = false
            displayMonthlyChart = true
            displayYearlyChart = false
        }

        HomeScreenChartToDisplay.Yearly -> {

            displayWeeklyChart = false
            displayMonthlyChart = false
            displayYearlyChart = true
        }
    }

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .testTag(tag = "rowWithRadioButtons"),

        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        mediumRadioTextButton(text = "weekly", isSelected = displayWeeklyChart) {

            displayWeeklyChart = it
            displayMonthlyChart = !it
            displayYearlyChart = !it

            switchChart(HomeScreenChartToDisplay.Weekly)
        }

        mediumRadioTextButton(text = "monthly", isSelected = displayMonthlyChart) {

            displayMonthlyChart = it
            displayWeeklyChart = !it
            displayYearlyChart = !it

            switchChart(HomeScreenChartToDisplay.Monthly)
        }

        mediumRadioTextButton(text = "yearly", isSelected = displayYearlyChart) {

            displayYearlyChart = it
            displayWeeklyChart = !it
            displayMonthlyChart = !it

            switchChart(HomeScreenChartToDisplay.Yearly)
        }
    }
}