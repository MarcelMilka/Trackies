package com.example.trackies.isSignedIn.homeScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackies.isSignedIn.homeScreen.buisness.HeightOfLazyColumn
import com.example.trackies.isSignedIn.homeScreen.buisness.HomeScreenChartToDisplay
import com.example.trackies.isSignedIn.homeScreen.viewState.HomeScreenViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(): ViewModel(){

    private var _uiState = MutableStateFlow<HomeScreenViewState>(HomeScreenViewState())
    val uiState = _uiState.asStateFlow()

    fun updateHeightOfLazyColumn(totalAmountOfTrackiesForToday: Int) {

        val copyOfUiState = _uiState
        val updatedHeightOfLazyColumn = when (totalAmountOfTrackiesForToday) {

            0 -> {
                HeightOfLazyColumn.noTrackies
            }
            1 -> {
                HeightOfLazyColumn.oneTrackie
            }
            2 -> {
                HeightOfLazyColumn.twoTrackies
            }
            3 -> {
                HeightOfLazyColumn.threeTrackies
            }
            else -> {
                HeightOfLazyColumn.moreThanThreeTrackies
            }
        }

        viewModelScope.launch {

            delay(250)

            _uiState.update {

                HomeScreenViewState(
                    heightOfLazyColumn = updatedHeightOfLazyColumn,
                    typeOfHomeScreenChart = copyOfUiState.value.typeOfHomeScreenChart
                )
            }
        }
    }

    fun updateTypeOfHomeScreenChart(homeScreenChartToDisplay: HomeScreenChartToDisplay) {

        val copyOfUiState = _uiState

        _uiState.update {

            HomeScreenViewState(
                heightOfLazyColumn = copyOfUiState.value.heightOfLazyColumn,
                typeOfHomeScreenChart = homeScreenChartToDisplay
            )
        }
    }

//  Used to set values to default when a user switches account.
    fun resetHomeScreenViewState() {

        _uiState.update {
            HomeScreenViewState()
        }
    }
}