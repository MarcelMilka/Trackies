package com.example.trackies.isSignedIn.homeScreen.viewState

import com.example.trackies.isSignedIn.homeScreen.buisness.HeightOfLazyColumn
import com.example.trackies.isSignedIn.homeScreen.buisness.HomeScreenChartToDisplay

data class HomeScreenViewState(
    var heightOfLazyColumn: Int = HeightOfLazyColumn.threeTrackies,
    var typeOfHomeScreenChart: HomeScreenChartToDisplay = HomeScreenChartToDisplay.Weekly
)