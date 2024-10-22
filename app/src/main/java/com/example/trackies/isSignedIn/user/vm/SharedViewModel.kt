package com.example.trackies.isSignedIn.user.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackies.isSignedIn.constantValues.CurrentTime
import com.example.trackies.isSignedIn.trackie.TrackieViewState
import com.example.trackies.isSignedIn.user.buisness.licenseViewState.LicenseViewState
import com.example.trackies.isSignedIn.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// To check any errors which occur while fetching/saving/deleting data type "SharedViewModel-firebase"

@HiltViewModel
class SharedViewModel @Inject constructor(
    private var repository: UserRepository
): ViewModel() {

    private var _uiState = MutableStateFlow<SharedViewModelViewState>(value = SharedViewModelViewState.Loading)
    val uiState = _uiState.asStateFlow()

    init {

        repository.firstTimeInTheApp {}

        viewModelScope.launch {

            val licenseInformation = repository.fetchUsersLicense()
            val trackiesForToday = repository.fetchTrackiesForToday(
                onFailure = {
                    Log.d("SharedViewModel-firebase", "init, fetchTrackiesForToday - $it")
                }
            )
            val statesOfTrackiesForToday = repository.fetchStatesOfTrackiesForToday(
                onFailure = {
                    Log.d("SharedViewModel-firebase", "init, fetchStatesOfTrackiesForToday - $it")
                }
            )

            if (
                licenseInformation != null &&
                trackiesForToday != null &&
                statesOfTrackiesForToday != null
            ) {

                _uiState.update {

                    SharedViewModelViewState.LoadedSuccessfully(
                        license = licenseInformation,
                        trackiesForToday = trackiesForToday,
                        statesOfTrackiesForToday = statesOfTrackiesForToday,
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

    fun addNewTrackie(
        trackieViewState: TrackieViewState,
        onFailure: (String) -> Unit
    ) {

        if (_uiState.value is SharedViewModelViewState.LoadedSuccessfully) {

//          Firebase
            viewModelScope.launch {

                repository.addNewTrackie(
                    trackieViewState = trackieViewState,
                    onSuccess = {},
                    onFailure = {
                        Log.d("SharedViewModel-firebase", "method 'addNewTrackie' - $it")
                    }
                )
            }

//          UI
            val copyOfViewState = _uiState.value as SharedViewModelViewState.LoadedSuccessfully

//          This method is responsible for updating information contained by LicenseViewState and increasing the amount of trackies by one.
            fun updateLicenseViewState(): LicenseViewState {

                val copyOfLicenseViewState = (_uiState.value as SharedViewModelViewState.LoadedSuccessfully).license
                val newAmountOfTrackies = copyOfViewState.license.totalAmountOfTrackies + 1

                return LicenseViewState(
                    active = copyOfLicenseViewState.active,
                    validUntil = copyOfLicenseViewState.validUntil,
                    totalAmountOfTrackies = newAmountOfTrackies
                )
            }

            fun updateTrackiesForToday(): MutableList<TrackieViewState> {

                val currentDayOfWeek = CurrentTime.getCurrentDayOfWeek()
                var copyOfTrackiesForToday = copyOfViewState.trackiesForToday.toMutableList()

                return if (trackieViewState.repeatOn.contains(element = currentDayOfWeek)) {

                    copyOfTrackiesForToday.add(element = trackieViewState)
                    copyOfTrackiesForToday
                }

                else {
                    copyOfTrackiesForToday
                }
            }

            fun updateStatesOfTrackiesForToday(): Map<String, Boolean> {

                val newStatesOfTrackiesForToday: MutableMap<String, Boolean> = mutableMapOf()

                copyOfViewState.statesOfTrackiesForToday.forEach {

                    newStatesOfTrackiesForToday[it.key] = it.value
                }
                newStatesOfTrackiesForToday[trackieViewState.name] = false

                return newStatesOfTrackiesForToday
            }

//          This method is responsible for adding name of the new Trackie to the mutableList which contains names of all trackies owned by a User.
            fun updateNamesOfAllTrackies(): MutableList<String> {

                var namesOfAllTrackies: MutableList<String>? = copyOfViewState.namesOfAllTrackies

                return if (namesOfAllTrackies != null) {

                    namesOfAllTrackies.add(trackieViewState.name)
                    namesOfAllTrackies
                }

                else {
                    mutableListOf(trackieViewState.name)
                }
            }

//          This method is responsible for adding Trackie to the list of all trackies (when the parameter 'allTrackies' is not equal to null.)
            fun updateListOfAllTrackies(): MutableList<TrackieViewState>? {

                return if (copyOfViewState.allTrackies != null) {

                    var listOfAllTrackies = copyOfViewState.allTrackies!!.toMutableList()
                    listOfAllTrackies.add(element = trackieViewState)

                    listOfAllTrackies
                }

                else {
                    null
                }
            }



            val updatedLicense = updateLicenseViewState()
            val trackiesForToday = updateTrackiesForToday()
            val updatedStatesOfTrackiesForToday = updateStatesOfTrackiesForToday()
            val updatedNamesOfAllTrackies = updateNamesOfAllTrackies()
            val updatedListOfAllTrackies = updateListOfAllTrackies()

            _uiState.update {

                SharedViewModelViewState.LoadedSuccessfully(
                    license = updatedLicense,
                    trackiesForToday = trackiesForToday,
                    statesOfTrackiesForToday = updatedStatesOfTrackiesForToday,
                    namesOfAllTrackies = updatedNamesOfAllTrackies,
                    allTrackies = updatedListOfAllTrackies
                )
            }
        }

        else {
            onFailure("Wrong UI state")
        }
    }

    fun deleteTrackie(
        trackieViewState: TrackieViewState,
        onFailure: (String) -> Unit
    ) {

        if (_uiState.value is SharedViewModelViewState.LoadedSuccessfully) {

//          Firebase
            viewModelScope.launch {

                repository.deleteTrackie(
                    trackieViewState = trackieViewState,
                    onSuccess = {},
                    onFailure = {
                        Log.d("SharedViewModel-firebase", "method 'deleteTrackie' - $it")
                    }
                )
            }

//          UI
            val copyOfViewState = _uiState.value as SharedViewModelViewState.LoadedSuccessfully

//          This method is responsible for updating information contained by LicenseViewState and decreasing the amount of trackies by one.
            fun updateLicenseViewState(): LicenseViewState {

                val copyOfLicenseViewState = (_uiState.value as SharedViewModelViewState.LoadedSuccessfully).license
                val newAmountOfTrackies = copyOfViewState.license.totalAmountOfTrackies - 1

                return LicenseViewState(
                    active = copyOfLicenseViewState.active,
                    validUntil = copyOfLicenseViewState.validUntil,
                    totalAmountOfTrackies = newAmountOfTrackies
                )
            }

            fun updateTrackiesForToday(): MutableList<TrackieViewState> {

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

//          This method is responsible for adding name of the new Trackie to the mutableList which contains names of all trackies owned by a User.
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

//          This method is responsible for removing Trackie from the list of all trackies (when the parameter 'allTrackies' is not equal to null.)
            fun updateListOfAllTrackies(): MutableList<TrackieViewState>? {

                return if (copyOfViewState.allTrackies != null) {

                    var listOfAllTrackies = copyOfViewState.allTrackies!!.toMutableList()
                    listOfAllTrackies.remove(element = trackieViewState)

                    listOfAllTrackies
                }

                else {
                    null
                }
            }

            val updatedLicense = updateLicenseViewState()
            val trackiesForToday = updateTrackiesForToday()
            val updatedStatesOfTrackiesForToday = updateStatesOfTrackiesForToday()
            val updatedNamesOfAllTrackies = updateNamesOfAllTrackies()
            val updatedListOfAllTrackies = updateListOfAllTrackies()

            _uiState.update {

                SharedViewModelViewState.LoadedSuccessfully(
                    license = updatedLicense,
                    trackiesForToday = trackiesForToday,
                    statesOfTrackiesForToday = updatedStatesOfTrackiesForToday,
                    namesOfAllTrackies = updatedNamesOfAllTrackies,
                    allTrackies = updatedListOfAllTrackies
                )
            }
        }

        else {
            onFailure("Wrong UI state")
        }
    }

    fun fetchListOfAllTrackies(onFailure: (String) -> Unit) {

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
                        namesOfAllTrackies = copyOfViewState.namesOfAllTrackies,
                        allTrackies = allTrackies
                    )
                }
            }
        }
    }
}