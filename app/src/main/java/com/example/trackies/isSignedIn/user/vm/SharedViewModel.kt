package com.example.trackies.isSignedIn.user.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.globalConstants.CurrentTime
import com.example.globalConstants.DaysOfWeek
import com.example.globalConstants.annotationClasses.Tested
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.isSignedIn.user.buisness.LicenseModel
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelViewState
import com.example.trackies.isSignedIn.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.String
import kotlin.collections.Map

@HiltViewModel
class SharedViewModel @Inject constructor(
    var repository: UserRepository
): ViewModel() {

    private var _uiState = MutableStateFlow<SharedViewModelViewState>(value = SharedViewModelViewState.Loading)
    val uiState = _uiState.asStateFlow()

    init {

        viewModelScope.launch {

//          1: Checking if the user is first time in the app. If so, the method automatically adds new user to the database:
            if (repository.isFirstTimeInTheApp() != null) {

//              2: Checking if it's required to reset weekly regularity: (true == it's required to reset regularity)
                val needToResetPastWeekRegularity = repository.needToResetPastWeekRegularity()

                if (needToResetPastWeekRegularity != null) {

//                  3.1: Reset of the regularity from the past week: and fetching user's data:
                    if (needToResetPastWeekRegularity) {

                        val resetOfRegularityIsSuccessful = repository.resetWeeklyRegularity()

                        when (resetOfRegularityIsSuccessful) {

                            true -> {

                                val licenseInformation = repository.fetchUsersLicense()
                                val trackiesForToday = repository.fetchTrackiesForToday()
                                val statesOfTrackiesForToday = repository.fetchStatesOfTrackiesForToday()
                                val weeklyRegularity = repository.fetchWeeklyRegularity()

                                if (
                                    licenseInformation != null &&
                                    trackiesForToday != null &&
                                    statesOfTrackiesForToday != null &&
                                    weeklyRegularity != null
                                ) {

                                    _uiState.update {

                                        SharedViewModelViewState.LoadedSuccessfully(
                                            license = licenseInformation,
                                            trackiesForToday = trackiesForToday,
                                            statesOfTrackiesForToday = statesOfTrackiesForToday,
                                            weeklyRegularity = weeklyRegularity,
                                            namesOfAllTrackies = null,
                                            allTrackies = null
                                        )
                                    }
                                }

                                else {

                                    _uiState.update {
                                        SharedViewModelViewState.FailedToLoadData
                                    }
                                }
                            }

                            false -> {

                                _uiState.update {
                                    SharedViewModelViewState.FailedToLoadData
                                }
                            }
                        }
                    }

//                  3.2: Fetching user's data:
                    else {

                        val licenseInformation = repository.fetchUsersLicense()
                        val trackiesForToday = repository.fetchTrackiesForToday()
                        val statesOfTrackiesForToday = repository.fetchStatesOfTrackiesForToday()
                        val weeklyRegularity = repository.fetchWeeklyRegularity()

                        if (
                            licenseInformation != null &&
                            trackiesForToday != null &&
                            statesOfTrackiesForToday != null &&
                            weeklyRegularity != null
                        ) {

                            _uiState.update {

                                SharedViewModelViewState.LoadedSuccessfully(
                                    license = licenseInformation,
                                    trackiesForToday = trackiesForToday,
                                    statesOfTrackiesForToday = statesOfTrackiesForToday,
                                    weeklyRegularity = weeklyRegularity,
                                    namesOfAllTrackies = null,
                                    allTrackies = null
                                )
                            }
                        }

                        else {

                            _uiState.update {
                                SharedViewModelViewState.FailedToLoadData
                            }
                        }
                    }
                }

                else {
                    _uiState.update {
                        SharedViewModelViewState.FailedToLoadData
                    }
                }
            }

            else {

                _uiState.update {
                    SharedViewModelViewState.FailedToLoadData
                }
            }
        }
    }

    @Tested
    fun addNewTrackie(trackieModel: TrackieModel) {

//      Database:
        viewModelScope.launch {

            repository.addNewTrackie(trackieModel = trackieModel)
        }

//      UI:
        val copyOfViewState = _uiState.value as SharedViewModelViewState.LoadedSuccessfully

//      This method is responsible for updating information contained by LicenseViewState and increasing the amount of trackies by one.
        fun updateLicenseViewState(): LicenseModel {

            val copyOfLicenseViewState = (_uiState.value as SharedViewModelViewState.LoadedSuccessfully).license
            val newAmountOfTrackies = copyOfViewState.license.totalAmountOfTrackies + 1

            return LicenseModel(
                active = copyOfLicenseViewState.active,
                validUntil = copyOfLicenseViewState.validUntil,
                totalAmountOfTrackies = newAmountOfTrackies
            )
        }

        fun updateTrackiesForToday(): MutableList<TrackieModel> {

            val currentDayOfWeek = CurrentTime.getCurrentDayOfWeek()
            var copyOfTrackiesForToday = copyOfViewState.trackiesForToday.toMutableList()

            return if (trackieModel.repeatOn.contains(element = currentDayOfWeek)) {

                copyOfTrackiesForToday.add(element = trackieModel)
                copyOfTrackiesForToday
            }

            else {
                copyOfTrackiesForToday
            }
        }

        fun updateStatesOfTrackiesForToday(): Map<String, Boolean> {

            return if (trackieModel.repeatOn.contains(CurrentTime.getCurrentDayOfWeek())) {

                val newStatesOfTrackiesForToday: MutableMap<String, Boolean> = mutableMapOf()

                copyOfViewState.statesOfTrackiesForToday.forEach {

                    newStatesOfTrackiesForToday[it.key] = it.value
                }
                newStatesOfTrackiesForToday[trackieModel.name] = false

                newStatesOfTrackiesForToday
            }

            else {

                copyOfViewState.statesOfTrackiesForToday
            }
        }

//      This method is responsible for adding name of the new Trackie to the mutableList which contains names of all trackies owned by a User.
        fun updateNamesOfAllTrackies(): MutableList<String> {

            var namesOfAllTrackies: MutableList<String>? = copyOfViewState.namesOfAllTrackies

            return if (namesOfAllTrackies != null) {

                namesOfAllTrackies.add(trackieModel.name)
                namesOfAllTrackies
            }

            else {
                mutableListOf(trackieModel.name)
            }
        }

//      This method is responsible for adding Trackie to the list of all trackies (when the parameter 'allTrackies' is not equal to null.)
        fun updateListOfAllTrackies(): MutableList<TrackieModel>? {

            return if (copyOfViewState.allTrackies != null) {

                var listOfAllTrackies = copyOfViewState.allTrackies!!.toMutableList()
                listOfAllTrackies.add(element = trackieModel)

                listOfAllTrackies
            }

            else {
                null
            }
        }

        fun updateWeeklyRegularity(): Map<String, Map<Int, Int>> {

            var newWeeklyRegularity = mutableMapOf<String, Map<Int, Int>>()

            copyOfViewState.weeklyRegularity.forEach { array ->

                if (trackieModel.repeatOn.contains(array.key)) {

                    val total = array.value.keys.toIntArray()[0] + 1
                    val ingested = array.value.values.toIntArray()[0]

                    val value = mapOf(total to ingested)

                    newWeeklyRegularity.put(
                        key = array.key,
                        value = value)
                }

                else {

                    val total = array.value.keys.toIntArray()[0]
                    val ingested = array.value.values.toIntArray()[0]

                    val value = mapOf(total to ingested)

                    newWeeklyRegularity.put(
                        key = array.key,
                        value = value
                    )
                }
            }

            return newWeeklyRegularity
        }

        val updatedLicense = updateLicenseViewState()
        val trackiesForToday = updateTrackiesForToday()
        val updatedStatesOfTrackiesForToday = updateStatesOfTrackiesForToday()
        val updatedNamesOfAllTrackies = updateNamesOfAllTrackies()
        val updatedListOfAllTrackies = updateListOfAllTrackies()
        val updatedWeeklyRegularity = updateWeeklyRegularity()

        _uiState.update {

            SharedViewModelViewState.LoadedSuccessfully(
                license = updatedLicense,
                trackiesForToday = trackiesForToday,
                statesOfTrackiesForToday = updatedStatesOfTrackiesForToday,
                weeklyRegularity = updatedWeeklyRegularity,
                namesOfAllTrackies = updatedNamesOfAllTrackies,
                allTrackies = updatedListOfAllTrackies
            )
        }
    }

    fun deleteTrackie(trackieViewState: TrackieModel) {

//      Firebase
        viewModelScope.launch {

            repository.deleteTrackie(trackieModel = trackieViewState)
        }

//      UI
        val copyOfViewState = _uiState.value as SharedViewModelViewState.LoadedSuccessfully

//      This method is responsible for updating information contained by LicenseViewState and decreasing the amount of trackies by one.
        fun updateLicenseViewState(): LicenseModel {

            val copyOfLicenseViewState = (_uiState.value as SharedViewModelViewState.LoadedSuccessfully).license
            val newAmountOfTrackies = copyOfViewState.license.totalAmountOfTrackies - 1

            return LicenseModel(
                active = copyOfLicenseViewState.active,
                validUntil = copyOfLicenseViewState.validUntil,
                totalAmountOfTrackies = newAmountOfTrackies
            )
        }

        fun updateTrackiesForToday(): MutableList<TrackieModel> {

            var copyOfTrackiesForToday = copyOfViewState.trackiesForToday.toMutableList()
            copyOfTrackiesForToday.remove(element = trackieViewState)

            return copyOfTrackiesForToday
        }

        fun updateStatesOfTrackiesForToday(): Map<String, Boolean> {

            val newStatesOfTrackiesForToday: MutableMap<String, Boolean> = mutableMapOf()

            copyOfViewState.statesOfTrackiesForToday.forEach {

                if (it.key != trackieViewState.name) {
                    newStatesOfTrackiesForToday[it.key] = it.value
                }
            }

            return newStatesOfTrackiesForToday
        }

//      This method is responsible for adding name of the new Trackie to the mutableList which contains names of all trackies owned by a User.
        fun updateNamesOfAllTrackies(): MutableList<String>? {

            var namesOfAllTrackies: MutableList<String>? = copyOfViewState.namesOfAllTrackies

            return if (namesOfAllTrackies != null) {

                namesOfAllTrackies.remove(element = trackieViewState.name)
                namesOfAllTrackies
            }

            else {
                null
            }
        }

//      This method is responsible for removing Trackie from the list of all trackies (when the parameter 'allTrackies' is not equal to null.)
        fun updateListOfAllTrackies(): MutableList<TrackieModel>? {

            return if (copyOfViewState.allTrackies != null) {

                var listOfAllTrackies = copyOfViewState.allTrackies!!.toMutableList()
                listOfAllTrackies.remove(element = trackieViewState)

                listOfAllTrackies
            }

            else {
                null
            }
        }

        fun updateWeeklyRegularity(): Map<String, Map<Int, Int>> {

            val currentDayOfWeek = CurrentTime.getCurrentDayOfWeek()
            var passedCurrentDayOfWeek = false

            var newWeeklyRegularity = mutableMapOf<String, Map<Int, Int>>()

            setOf(
                DaysOfWeek.monday,
                DaysOfWeek.tuesday,
                DaysOfWeek.wednesday,
                DaysOfWeek.thursday,
                DaysOfWeek.friday,
                DaysOfWeek.saturday,
                DaysOfWeek.sunday,
            ).forEach { dayOfWeek ->

                if (!passedCurrentDayOfWeek) {

                    if (trackieViewState.repeatOn.contains(dayOfWeek)) {

                        val dayOfWeek = dayOfWeek

//                          getting total amount of Trackies assigned for this day and decreasing it by one:
                        val total = copyOfViewState.weeklyRegularity[dayOfWeek]!!.keys.toIntArray()[0] - 1

//                          getting amount of ingested Trackies assigned for this day and ... todo
                        val ingested =
                            if (currentDayOfWeek == dayOfWeek) {

                                copyOfViewState.statesOfTrackiesForToday[trackieViewState.name].let { state ->

                                    if (state!!) {
                                        copyOfViewState.weeklyRegularity[dayOfWeek]!!.values.toIntArray()[0] - 1
                                    }

                                    else {
                                        copyOfViewState.weeklyRegularity[dayOfWeek]!!.values.toIntArray()[0]
                                    }
                                }
                            }

                            else {
                                copyOfViewState.weeklyRegularity[dayOfWeek]!!.values.toIntArray()[0] - 1
                            }

                        newWeeklyRegularity[dayOfWeek] = mapOf(total to ingested)
                    }

                    else {

                        val dayOfWeek = dayOfWeek

//                          getting total amount of Trackies assigned for this day.
                        val total = copyOfViewState.weeklyRegularity[dayOfWeek]!!.keys.toIntArray()[0]

//                          getting amount of ingested Trackies assigned for this day.
                        val ingested = copyOfViewState.weeklyRegularity[dayOfWeek]!!.values.toIntArray()[0]

                        newWeeklyRegularity[dayOfWeek] = mapOf(total to ingested)
                    }
                }

                else {

                    newWeeklyRegularity[dayOfWeek] = mapOf(1 to 0)
                }

                passedCurrentDayOfWeek = currentDayOfWeek == dayOfWeek

            }

            return newWeeklyRegularity
        }

        val updatedLicense = updateLicenseViewState()
        val trackiesForToday = updateTrackiesForToday()
        val updatedStatesOfTrackiesForToday = updateStatesOfTrackiesForToday()
        val updatedWeeklyRegularity = updateWeeklyRegularity()
        val updatedNamesOfAllTrackies = updateNamesOfAllTrackies()
        val updatedListOfAllTrackies = updateListOfAllTrackies()

        _uiState.update {

            SharedViewModelViewState.LoadedSuccessfully(
                license = updatedLicense,
                trackiesForToday = trackiesForToday,
                statesOfTrackiesForToday = updatedStatesOfTrackiesForToday,
                weeklyRegularity = updatedWeeklyRegularity,
                namesOfAllTrackies = updatedNamesOfAllTrackies,
                allTrackies = updatedListOfAllTrackies
            )
        }
    }

    fun fetchListOfAllTrackies() {

        viewModelScope.launch {

            val allTrackies = repository.fetchAllTrackies(
                onSuccess = {},
                onFailure = {
                    Log.d("SharedViewModel-firebase", "fetchAllUsersTrackies - $it")
                }
            )

            if (allTrackies != null) {

                val copyOfViewState = _uiState.value as SharedViewModelViewState.LoadedSuccessfully

                _uiState.update {

                    SharedViewModelViewState.LoadedSuccessfully(
                        license = copyOfViewState.license,
                        trackiesForToday = copyOfViewState.trackiesForToday,
                        statesOfTrackiesForToday = copyOfViewState.statesOfTrackiesForToday,
                        weeklyRegularity = copyOfViewState.weeklyRegularity,
                        namesOfAllTrackies = copyOfViewState.namesOfAllTrackies,
                        allTrackies = allTrackies
                    )
                }
            }
        }
    }

    fun markTrackieAsIngested(trackieModel: TrackieModel) {

        if (_uiState.value is SharedViewModelViewState.LoadedSuccessfully) {

            Log.d("Halla!", "marked trackie as ingested")
//          Firebase
            viewModelScope.launch {

                repository.markTrackieAsIngested(
                    currentDayOfWeek = CurrentTime.getCurrentDayOfWeek(),
                    trackieModel = trackieModel,
                    onSuccess = {

                    },
                    onFailure = {
                        Log.d("SharedViewModel-firebase", "method 'markTrackieAsIngested' - $it")
                    }
                )
            }

//          UI
            val copyOfViewState = _uiState.value as SharedViewModelViewState.LoadedSuccessfully

            fun updateStatesOfTrackiesForToday(): Map<String, Boolean> {

                val updatedMap = copyOfViewState.statesOfTrackiesForToday.toMutableMap()

                updatedMap[trackieModel.name] = true

                return updatedMap
            }

            fun updateWeeklyRegularity(): Map<String, Map<Int, Int>> {

                val currentDayOfWeek = CurrentTime.getCurrentDayOfWeek()

                var updatedWeeklyRegularity = mutableMapOf<String, MutableMap<Int, Int>>()

                copyOfViewState.weeklyRegularity.forEach {

                    if (it.key == currentDayOfWeek) {

                        val total = it.value.keys.toIntArray()[0]
                        var ingested = it.value.values.toIntArray()[0]
                        ingested = ingested + 1

                        val value = mutableMapOf(total to ingested)

                        updatedWeeklyRegularity.put(
                            key = it.key,
                            value = value
                        )
                    }

                    else {

                        val total = it.value.keys.toIntArray()[0]
                        var ingested = it.value.values.toIntArray()[0]

                        val value = mutableMapOf(total to ingested)

                        updatedWeeklyRegularity.put(
                            key = it.key,
                            value = value
                        )
                    }
                }

                return updatedWeeklyRegularity
            }

            val updatedStatesOfTrackiesForToday = updateStatesOfTrackiesForToday()
            val updatedWeeklyRegularity = updateWeeklyRegularity()

            _uiState.update {

                SharedViewModelViewState.LoadedSuccessfully(
                    license = copyOfViewState.license,
                    trackiesForToday = copyOfViewState.trackiesForToday,
                    statesOfTrackiesForToday = updatedStatesOfTrackiesForToday,
                    weeklyRegularity = updatedWeeklyRegularity,
                    namesOfAllTrackies = copyOfViewState.namesOfAllTrackies,
                    allTrackies = copyOfViewState.allTrackies
                )
            }
        }
    }

    fun deleteUsersData() {

        viewModelScope.launch {

            repository.deleteUsersData()
        }
    }

    fun resetViewModel() {

        viewModelScope.launch {

            _uiState.update {

                SharedViewModelViewState.Loading
            }
        }
    }
}