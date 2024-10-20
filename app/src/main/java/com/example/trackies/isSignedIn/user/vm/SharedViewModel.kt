package com.example.trackies.isSignedIn.user.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackies.isSignedIn.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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

            if (licenseInformation != null) {

                _uiState.update {

                    SharedViewModelViewState.LoadedSuccessfully(
                        license = licenseInformation,
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
}