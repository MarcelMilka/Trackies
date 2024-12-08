package com.example.trackies.isSignedIn.user.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.globalConstants.CurrentDateTime
import com.example.globalConstants.DaysOfWeek
import com.example.globalConstants.annotationClasses.Tested
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.isSignedIn.user.buisness.LicenseModel
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelErrors
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelViewState
import com.example.trackies.isSignedIn.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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

    var error: SharedViewModelErrors? = null

//  @Tested
    init {

        Log.d("Halla!", "init!")

        viewModelScope.launch {

//          1: Checking if the user is first time in the app. If so, the method automatically adds new user to the database:
            if (repository.isFirstTimeInTheApp() != null) {

//              2: Checking if it's required to reset weekly regularity: (true == it's required to reset regularity)
                val needToResetPastWeekRegularity = repository.needToResetPastWeekRegularity(
                    currentDayOfWeek = CurrentDateTime.getCurrentDayOfWeek()
                )

                if (needToResetPastWeekRegularity != null) {

//                  3.1: Reset of the regularity from the past week: and fetching user's data:
                    if (needToResetPastWeekRegularity) {

                        val resetOfRegularityIsSuccessful = repository.resetWeeklyRegularity(
                            currentDayOfWeek = CurrentDateTime.getCurrentDayOfWeek()
                        )

                        when (resetOfRegularityIsSuccessful) {

                            true -> {

                                val licenseInformation = repository.fetchUsersLicense()

                                val trackiesForToday = repository.fetchTrackiesForToday(
                                    currentDayOfWeek = CurrentDateTime.getCurrentDayOfWeek()
                                )

                                val statesOfTrackiesForToday = repository.fetchStatesOfTrackiesForToday(
                                    currentDayOfWeek = CurrentDateTime.getCurrentDayOfWeek()
                                )

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

                                    error = SharedViewModelErrors.AtLeastOneFinalMethodReturnedError

                                    _uiState.update {
                                        SharedViewModelViewState.FailedToLoadData
                                    }
                                }
                            }

                            false -> {

                                error = SharedViewModelErrors.ResetWeeklyRegularityReturnedError

                                _uiState.update {
                                    SharedViewModelViewState.FailedToLoadData
                                }
                            }
                        }
                    }

//                  3.2: Fetching user's data:
                    else {

                        val licenseInformation = repository.fetchUsersLicense()

                        val trackiesForToday = repository.fetchTrackiesForToday(
                            currentDayOfWeek = CurrentDateTime.getCurrentDayOfWeek()
                        )

                        val statesOfTrackiesForToday = repository.fetchStatesOfTrackiesForToday(
                            currentDayOfWeek = CurrentDateTime.getCurrentDayOfWeek()
                        )

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

                            error = SharedViewModelErrors.AtLeastOneFinalMethodReturnedError

                            _uiState.update {
                                SharedViewModelViewState.FailedToLoadData
                            }
                        }
                    }
                }

                else {

                    error = SharedViewModelErrors.NeedToResetPastWeekRegularityReturnedError

                    _uiState.update {
                        SharedViewModelViewState.FailedToLoadData
                    }
                }
            }

            else {

                error = SharedViewModelErrors.IsFirstTimeInTheAppReturnedError

                _uiState.update {
                    SharedViewModelViewState.FailedToLoadData
                }
            }
        }
    }

    @Tested
    fun addNewTrackie(
        trackieModel: TrackieModel,
        currentDayOfWeek: String,
        onFailedToAddNewTrackie: () -> Unit
    ) {

        viewModelScope.launch {

            val properlyAddedNewTrackie = async {

                repository
                    .addNewTrackie(
                        trackieModel = trackieModel
                    )
            }.await()

            when (properlyAddedNewTrackie) {

                true -> {

                    val copyOfViewState = _uiState.value as SharedViewModelViewState.LoadedSuccessfully

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

                        return if (trackieModel.repeatOn.contains(currentDayOfWeek)) {

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

                    fun updateNamesOfAllTrackies(): MutableList<String>? {

                        var namesOfAllTrackies: MutableList<String>? = copyOfViewState.namesOfAllTrackies

                        return if (namesOfAllTrackies != null) {

                            namesOfAllTrackies.add(trackieModel.name)
                            namesOfAllTrackies
                        }

                        else {
                            null
                        }
                    }

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

                false -> {

                    onFailedToAddNewTrackie()
                }
            }
        }
    }

    @Tested
    fun deleteTrackie(
        trackieModel: TrackieModel,
        currentDayOfWeek: String,
        onFailedToDeleteTrackie: () -> Unit
    ) {

        viewModelScope.launch {

            val properlyDeletedTrackie = async {

                repository.deleteTrackie(
                    trackieModel = trackieModel
                )
            }.await()

            when (properlyDeletedTrackie) {

                true -> {

                    val copyOfViewState = _uiState.value as SharedViewModelViewState.LoadedSuccessfully

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
                        copyOfTrackiesForToday.remove(element = trackieModel)

                        return copyOfTrackiesForToday
                    }

                    fun updateStatesOfTrackiesForToday(): Map<String, Boolean> {

                        val newStatesOfTrackiesForToday: MutableMap<String, Boolean> = mutableMapOf()

                        copyOfViewState.statesOfTrackiesForToday.forEach {

                            if (it.key != trackieModel.name) {
                                newStatesOfTrackiesForToday[it.key] = it.value
                            }
                        }

                        return newStatesOfTrackiesForToday
                    }

                    fun updateNamesOfAllTrackies(): MutableList<String>? {

                        var namesOfAllTrackies: MutableList<String>? = copyOfViewState.namesOfAllTrackies

                        return if (namesOfAllTrackies != null) {

                            namesOfAllTrackies.remove(element = trackieModel.name)
                            namesOfAllTrackies
                        }

                        else {
                            null
                        }
                    }

                    fun updateListOfAllTrackies(): MutableList<TrackieModel>? {

                        return if (copyOfViewState.allTrackies != null) {

                            var listOfAllTrackies = copyOfViewState.allTrackies!!.toMutableList()
                            listOfAllTrackies.remove(element = trackieModel)

                            listOfAllTrackies
                        }

                        else {
                            null
                        }
                    }

                    fun updateWeeklyRegularity(): Map<String, Map<Int, Int>> {

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

                                if (trackieModel.repeatOn.contains(dayOfWeek)) {

//                                  getting total amount of Trackies assigned for this day and decreasing it by one:
                                    val total = copyOfViewState.weeklyRegularity[dayOfWeek]!!.keys.toIntArray()[0] - 1

//                                  getting amount of ingested Trackies assigned for this day and decreasing it if needed
                                    val ingested =
                                        if (currentDayOfWeek == dayOfWeek) {

                                            copyOfViewState.statesOfTrackiesForToday[trackieModel.name].let { isMarkedAsIngested ->

                                                if (isMarkedAsIngested!!) {

                                                    val decreasedValue = copyOfViewState.weeklyRegularity[dayOfWeek]!!.values.toIntArray()[0] - 1

                                                    if (decreasedValue < 0) {
                                                        0
                                                    }

                                                    else {

                                                        decreasedValue
                                                    }
                                                }

                                                else {
                                                    copyOfViewState.weeklyRegularity[dayOfWeek]!!.values.toIntArray()[0]
                                                }
                                            }
                                        }

                                        else {

                                            val decreasedValue = copyOfViewState.weeklyRegularity[dayOfWeek]!!.values.toIntArray()[0] - 1

                                            if (decreasedValue < 0) {
                                                0
                                            }

                                            else {

                                                decreasedValue
                                            }
                                        }

                                    newWeeklyRegularity[dayOfWeek] = mapOf(total to ingested)
                                }

                                else {

//                                  getting total amount of Trackies assigned for this day.
                                    val total = copyOfViewState.weeklyRegularity[dayOfWeek]!!.keys.toIntArray()[0]

//                                  getting amount of ingested Trackies assigned for this day.
                                    val ingested = copyOfViewState.weeklyRegularity[dayOfWeek]!!.values.toIntArray()[0]

                                    newWeeklyRegularity[dayOfWeek] = mapOf(total to ingested)
                                }
                            }

                            else {

                                if (trackieModel.repeatOn.contains(dayOfWeek)) {

//                                  getting total amount of Trackies assigned for this day and decreasing it by one:
                                    val total = copyOfViewState.weeklyRegularity[dayOfWeek]!!.keys.toIntArray()[0] - 1
                                    newWeeklyRegularity[dayOfWeek] = mapOf(total to 0)
                                }

                                else {

//                                  getting total amount of Trackies assigned for this day.
                                    val total = copyOfViewState.weeklyRegularity[dayOfWeek]!!.keys.toIntArray()[0]

//                                  getting amount of ingested Trackies assigned for this day.
                                    val ingested = copyOfViewState.weeklyRegularity[dayOfWeek]!!.values.toIntArray()[0]

                                    newWeeklyRegularity[dayOfWeek] = mapOf(total to ingested)
                                }
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

                false -> {
                    onFailedToDeleteTrackie()
                }
            }
        }
    }

    @Tested
    fun fetchAllTrackies(
        onFailedToFetchAllTrackies: () -> Unit
    ) {

        viewModelScope.launch {

            val listOfAllTrackies = async {

                repository.fetchAllTrackies()
            }.await()

            if (listOfAllTrackies != null) {

                val copyOfViewState = _uiState.value as SharedViewModelViewState.LoadedSuccessfully
                val namesOfAllTrackies = if (copyOfViewState.namesOfAllTrackies != null) {

                    val namesOfAllTrackies = listOfAllTrackies.map { it.name }

                    copyOfViewState.namesOfAllTrackies!!.addAll(namesOfAllTrackies)

                    copyOfViewState.namesOfAllTrackies
                }

                else {

                    listOfAllTrackies
                        .map { it.name }
                        .toMutableList()
                }

                _uiState.update {

                    SharedViewModelViewState.LoadedSuccessfully(
                        license = copyOfViewState.license,
                        trackiesForToday = copyOfViewState.trackiesForToday,
                        statesOfTrackiesForToday = copyOfViewState.statesOfTrackiesForToday,
                        weeklyRegularity = copyOfViewState.weeklyRegularity,
                        namesOfAllTrackies = namesOfAllTrackies,
                        allTrackies = listOfAllTrackies
                    )
                }
            }

            else {

                onFailedToFetchAllTrackies()
            }
        }
    }

    @Tested
    fun markTrackieAsIngested(
        trackieModel: TrackieModel,
        currentDayOfWeek: String,
        onFailedToMarkTrackieAsIngested: () -> Unit
    ) {

        viewModelScope.launch {

            val properlyMarkedTrackieAsIngested = async {

                repository.markTrackieAsIngested(
                    currentDayOfWeek = currentDayOfWeek,
                    trackieModel = trackieModel,
                )
            }.await()

            when (properlyMarkedTrackieAsIngested) {

                true -> {

                    val copyOfViewState = _uiState.value as SharedViewModelViewState.LoadedSuccessfully

                    fun updateStatesOfTrackiesForToday(): Map<String, Boolean> {

                        val updatedMap = copyOfViewState.statesOfTrackiesForToday.toMutableMap()

                        updatedMap[trackieModel.name] = true

                        return updatedMap
                    }

                    fun updateWeeklyRegularity(): Map<String, Map<Int, Int>> {

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

                false -> {

                    onFailedToMarkTrackieAsIngested()
                }
            }
        }
    }

    fun deleteUsersData() {

        viewModelScope.launch {

            repository.deleteUsersData()
        }
    }
}