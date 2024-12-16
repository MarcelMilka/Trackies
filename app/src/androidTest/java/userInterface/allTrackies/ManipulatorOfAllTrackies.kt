package userInterface.allTrackies

import com.example.globalConstants.DaysOfWeek
import com.example.trackies.isSignedIn.allTrackies.buisness.ListOfTrackiesToDisplay
import com.example.trackies.isSignedIn.user.buisness.LicenseModel
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelViewState
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import userInterface.homeScreen.Models
import kotlin.Int
import kotlin.String
import kotlin.collections.Map

class ManipulatorOfAllTrackiesScreen {

    private var _listOfTrackiesToDisplay = MutableStateFlow<ListOfTrackiesToDisplay>(ListOfTrackiesToDisplay.Today)
    val listOfTrackiesToDisplay = _listOfTrackiesToDisplay.asStateFlow()

    private var _sharedViewModelViewState = MutableStateFlow<SharedViewModelViewState>(SharedViewModelViewState.Loading)
    val sharedViewModelViewState = this._sharedViewModelViewState.asStateFlow()

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

                namesOfAllTrackies = null,

                allTrackies = null
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

                namesOfAllTrackies = null,

                allTrackies = null
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

                namesOfAllTrackies = null,

                allTrackies = null
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

                namesOfAllTrackies = null,

                allTrackies = null
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

                namesOfAllTrackies = null,

                allTrackies = null
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

                namesOfAllTrackies = null,

                allTrackies = null
            )
        }
    }

    fun setLoadedSuccessfully_TrackiesForWholeWeek() {

        _sharedViewModelViewState.update {

            SharedViewModelViewState.LoadedSuccessfully(

                license = LicenseModel(
                    active = false,
                    validUntil = null,
                    totalAmountOfTrackies = 7
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
                    DaysOfWeek.friday to mapOf(4 to 0),
                    DaysOfWeek.saturday to mapOf(6 to 0),
                    DaysOfWeek.sunday to mapOf(6 to 0),
                ),

                namesOfAllTrackies = mutableListOf(
                    "A", "B", "C", "Aaa", "Bbb", "Ccc", "Ddd"
                ),

                allTrackies = listOf(
                    Models.wholeWeekTrackieModel1,
                    Models.wholeWeekTrackieModel2,
                    Models.wholeWeekTrackieModel3,
                    Models.weekendTrackieModel1,
                    Models.weekendTrackieModel2,
                    Models.weekendTrackieModel3,
                    Models.fridayTrackieModel
                )
            )
        }
    }

    fun setListOfTrackiesToDisplay(listOfTrackiesToDisplay: ListOfTrackiesToDisplay) {

        _listOfTrackiesToDisplay.update {

            listOfTrackiesToDisplay
        }
    }
}