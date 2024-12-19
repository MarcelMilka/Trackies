package userInterface.allTrackies

import com.example.globalConstants.DaysOfWeek
import com.example.trackies.isSignedIn.allTrackies.buisness.ListOfTrackiesToDisplay
import com.example.trackies.isSignedIn.user.buisness.LicenseModel
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelViewState
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
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