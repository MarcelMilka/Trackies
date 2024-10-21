package com.example.trackies.isSignedIn.user.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

            if (
                licenseInformation != null &&
                trackiesForToday != null
            ) {

                _uiState.update {

                    SharedViewModelViewState.LoadedSuccessfully(
                        license = licenseInformation,
                        trackiesForToday = trackiesForToday,
                        namesOfAllTrackies = null
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

                var copyOfTrackiesForToday = copyOfViewState.trackiesForToday.toMutableList()
                copyOfTrackiesForToday.add(element = trackieViewState)

                return copyOfTrackiesForToday
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

            val updatedLicense = updateLicenseViewState()
            val trackiesForToday = updateTrackiesForToday()
            val updatedNamesOfAllTrackies = updateNamesOfAllTrackies()

            _uiState.update {

                SharedViewModelViewState.LoadedSuccessfully(
                    license = updatedLicense,
                    trackiesForToday = trackiesForToday,
                    namesOfAllTrackies = updatedNamesOfAllTrackies,
                )
            }
        }

        else {
            onFailure("Wrong UI state")
        }
    }
}