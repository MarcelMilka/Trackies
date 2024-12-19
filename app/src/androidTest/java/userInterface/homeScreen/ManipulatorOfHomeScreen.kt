package userInterface.homeScreen

import com.example.globalConstants.DaysOfWeek
import com.example.trackies.isSignedIn.homeScreen.buisness.HeightOfLazyColumn
import com.example.trackies.isSignedIn.homeScreen.buisness.HomeScreenChartToDisplay
import com.example.trackies.isSignedIn.homeScreen.viewState.HomeScreenViewState
import com.example.trackies.isSignedIn.user.buisness.LicenseModel
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelErrors
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelViewState
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// This class is responsible for simplifying tests of the composable homeScreen and its elements
// as its content mostly depends on SharedViewModelViewState.

class ManipulatorOfHomeScreen {

    private var _sharedViewModelViewState = MutableStateFlow<SharedViewModelViewState>(
        value = SharedViewModelViewState.Loading
    )
    val sharedViewModelViewState = _sharedViewModelViewState.asStateFlow()

    private var _homeScreenViewState = MutableStateFlow<HomeScreenViewState>(
        value = HomeScreenViewState()
    )
    val homeScreenViewState = _homeScreenViewState.asStateFlow()

    fun setLoading() {

        _sharedViewModelViewState.update {

            SharedViewModelViewState.Loading
        }
    }

    fun setLoadedSuccessfully_NoTrackies() {

        _sharedViewModelViewState.update {

            SharedViewModelViewState.LoadedSuccessfully(

                license = LicenseModel(
                    active = false,
                    validUntil = null,
                    totalAmountOfTrackies = 0
                ),

                trackiesForToday = listOf<TrackieModel>(),

                statesOfTrackiesForToday = mapOf<String, Boolean>(),

                weeklyRegularity = mapOf<String, Map<Int, Int>>(
                    DaysOfWeek.monday to mapOf(0 to 0),
                    DaysOfWeek.tuesday to mapOf(0 to 0),
                    DaysOfWeek.wednesday to mapOf(0 to 0),
                    DaysOfWeek.thursday to mapOf(0 to 0),
                    DaysOfWeek.friday to mapOf(0 to 0),
                    DaysOfWeek.saturday to mapOf(0 to 0),
                    DaysOfWeek.sunday to mapOf(0 to 0),
                ),

                namesOfAllTrackies = listOf(),

                allTrackies = null
            )
        }

        _homeScreenViewState.update {

            HomeScreenViewState(
                heightOfLazyColumn = HeightOfLazyColumn.noTrackies,
            )
        }
    }

    fun setLoadedSuccessfully_OneTrackie() {

        _sharedViewModelViewState.update {

            SharedViewModelViewState.LoadedSuccessfully(

                license = LicenseModel(
                    active = false,
                    validUntil = null,
                    totalAmountOfTrackies = 1
                ),

                trackiesForToday = listOf<TrackieModel>(Models.wholeWeekTrackieModel1),

                statesOfTrackiesForToday = mapOf<String, Boolean>(
                    "A" to false
                ),

                weeklyRegularity = mapOf<String, Map<Int, Int>>(
                    DaysOfWeek.monday to mapOf(1 to 0),
                    DaysOfWeek.tuesday to mapOf(1 to 0),
                    DaysOfWeek.wednesday to mapOf(1 to 0),
                    DaysOfWeek.thursday to mapOf(1 to 0),
                    DaysOfWeek.friday to mapOf(1 to 0),
                    DaysOfWeek.saturday to mapOf(1 to 0),
                    DaysOfWeek.sunday to mapOf(1 to 0),
                ),

                namesOfAllTrackies = listOf("A"),

                allTrackies = null
            )
        }

        _homeScreenViewState.update {

            HomeScreenViewState(
                heightOfLazyColumn = HeightOfLazyColumn.oneTrackie,
            )
        }
    }

    fun setLoadedSuccessfully_TwoTrackies() {

        _sharedViewModelViewState.update {

            SharedViewModelViewState.LoadedSuccessfully(

                license = LicenseModel(
                    active = false,
                    validUntil = null,
                    totalAmountOfTrackies = 2
                ),

                trackiesForToday = listOf<TrackieModel>(
                    Models.wholeWeekTrackieModel1,
                    Models.wholeWeekTrackieModel2,
                ),

                statesOfTrackiesForToday = mapOf<String, Boolean>(
                    "A" to false,
                    "B" to true
                ),

                weeklyRegularity = mapOf<String, Map<Int, Int>>(
                    DaysOfWeek.monday to mapOf(2 to 1),
                    DaysOfWeek.tuesday to mapOf(2 to 0),
                    DaysOfWeek.wednesday to mapOf(2 to 0),
                    DaysOfWeek.thursday to mapOf(2 to 0),
                    DaysOfWeek.friday to mapOf(2 to 0),
                    DaysOfWeek.saturday to mapOf(2 to 0),
                    DaysOfWeek.sunday to mapOf(2 to 0),
                ),

                namesOfAllTrackies = listOf("A", "B"),

                allTrackies = null
            )
        }

        _homeScreenViewState.update {

            HomeScreenViewState(
                heightOfLazyColumn = HeightOfLazyColumn.twoTrackies,
            )
        }
    }

    fun setLoadedSuccessfully_ThreeTrackies() {

        _sharedViewModelViewState.update {

            SharedViewModelViewState.LoadedSuccessfully(

                license = LicenseModel(
                    active = false,
                    validUntil = null,
                    totalAmountOfTrackies = 3
                ),

                trackiesForToday = listOf<TrackieModel>(
                    Models.wholeWeekTrackieModel1,
                    Models.wholeWeekTrackieModel2,
                    Models.wholeWeekTrackieModel3
                ),

                statesOfTrackiesForToday = mapOf<String, Boolean>(
                    "A" to false,
                    "B" to true,
                    "C" to false
                ),

                weeklyRegularity = mapOf<String, Map<Int, Int>>(
                    DaysOfWeek.monday to mapOf(3 to 1),
                    DaysOfWeek.tuesday to mapOf(3 to 0),
                    DaysOfWeek.wednesday to mapOf(3 to 0),
                    DaysOfWeek.thursday to mapOf(3 to 0),
                    DaysOfWeek.friday to mapOf(3 to 0),
                    DaysOfWeek.saturday to mapOf(3 to 0),
                    DaysOfWeek.sunday to mapOf(3 to 0),
                ),

                namesOfAllTrackies = listOf("A", "B", "C"),

                allTrackies = null
            )
        }

        _homeScreenViewState.update {

            HomeScreenViewState(
                heightOfLazyColumn = HeightOfLazyColumn.threeTrackies,
            )
        }
    }

    fun setLoadedSuccessfully_FourTrackies() {

        _sharedViewModelViewState.update {

            SharedViewModelViewState.LoadedSuccessfully(

                license = LicenseModel(
                    active = false,
                    validUntil = null,
                    totalAmountOfTrackies = 4
                ),

                trackiesForToday = listOf<TrackieModel>(
                    Models.wholeWeekTrackieModel1,
                    Models.wholeWeekTrackieModel2,
                    Models.wholeWeekTrackieModel3,
                    Models.wholeWeekTrackieModel4
                ),

                statesOfTrackiesForToday = mapOf<String, Boolean>(
                    "A" to false,
                    "B" to true,
                    "C" to false,
                    "D" to true
                ),

                weeklyRegularity = mapOf<String, Map<Int, Int>>(
                    DaysOfWeek.monday to mapOf(4 to 2),
                    DaysOfWeek.tuesday to mapOf(4 to 0),
                    DaysOfWeek.wednesday to mapOf(4 to 0),
                    DaysOfWeek.thursday to mapOf(4 to 0),
                    DaysOfWeek.friday to mapOf(4 to 0),
                    DaysOfWeek.saturday to mapOf(4 to 0),
                    DaysOfWeek.sunday to mapOf(4 to 0),
                ),

                namesOfAllTrackies = listOf("A", "B", "C", "D"),

                allTrackies = null
            )
        }

        _homeScreenViewState.update {

            HomeScreenViewState(
                heightOfLazyColumn = HeightOfLazyColumn.moreThanThreeTrackies,
            )
        }
    }

    fun setLoadedSuccessfully_FiveTrackies() {

        _sharedViewModelViewState.update {

            SharedViewModelViewState.LoadedSuccessfully(

                license = LicenseModel(
                    active = false,
                    validUntil = null,
                    totalAmountOfTrackies = 5
                ),

                trackiesForToday = listOf<TrackieModel>(
                    Models.wholeWeekTrackieModel1,
                    Models.wholeWeekTrackieModel2,
                    Models.wholeWeekTrackieModel3,
                    Models.wholeWeekTrackieModel4,
                    Models.wholeWeekTrackieModel5
                ),

                statesOfTrackiesForToday = mapOf<String, Boolean>(
                    "A" to false,
                    "B" to true,
                    "C" to false,
                    "D" to true,
                    "E" to false,
                ),

                weeklyRegularity = mapOf<String, Map<Int, Int>>(
                    DaysOfWeek.monday to mapOf(5 to 2),
                    DaysOfWeek.tuesday to mapOf(5 to 0),
                    DaysOfWeek.wednesday to mapOf(5 to 0),
                    DaysOfWeek.thursday to mapOf(5 to 0),
                    DaysOfWeek.friday to mapOf(5 to 0),
                    DaysOfWeek.saturday to mapOf(5 to 0),
                    DaysOfWeek.sunday to mapOf(5 to 0),
                ),

                namesOfAllTrackies = listOf("A", "B", "C", "D", "E"),

                allTrackies = null
            )
        }

        _homeScreenViewState.update {

            HomeScreenViewState(
                heightOfLazyColumn = HeightOfLazyColumn.moreThanThreeTrackies,
            )
        }
    }

    fun setFailedToLoadData_isFirstTimeInTheAppReturnedError() {

        _sharedViewModelViewState.update {

            SharedViewModelViewState.FailedToLoadData(
                errorMessage = SharedViewModelErrors.isFirstTimeInTheAppReturnedNull
            )
        }
    }

    fun setFailedToLoadData_needToResetPastWeekRegularityReturnedError() {

        _sharedViewModelViewState.update {

            SharedViewModelViewState.FailedToLoadData(
                errorMessage = SharedViewModelErrors.needToResetPastWeekRegularityReturnedNull
            )
        }
    }

    fun setFailedToLoadData_resetWeeklyRegularityReturnedError() {

        _sharedViewModelViewState.update {

            SharedViewModelViewState.FailedToLoadData(
                errorMessage = SharedViewModelErrors.resetWeeklyRegularityReturnedNull
            )
        }
    }

    fun setFailedToLoadData_atLeastOneFinalMethodReturnedError() {

        _sharedViewModelViewState.update {

            SharedViewModelViewState.FailedToLoadData(
                errorMessage = SharedViewModelErrors.atLeastOneFinalMethodReturnedNull
            )
        }
    }

    fun setFailedToLoadData_unidentifiedError() {

        _sharedViewModelViewState.update {

            SharedViewModelViewState.FailedToLoadData(
                errorMessage = SharedViewModelErrors.unidentifiedError
            )
        }
    }

    fun setChartToDisplay(homeScreenChartToDisplay: HomeScreenChartToDisplay) {

        _homeScreenViewState.update {

            HomeScreenViewState(
                heightOfLazyColumn = it.heightOfLazyColumn,
                typeOfHomeScreenChart = homeScreenChartToDisplay
            )
        }
    }
}