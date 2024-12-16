package com.example.trackies.isSignedIn.allTrackies.vm

import androidx.lifecycle.ViewModel
import com.example.globalConstants.annotationClasses.Tested
import com.example.trackies.isSignedIn.allTrackies.buisness.ListOfTrackiesToDisplay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AllTrackiesViewModel @Inject constructor(): ViewModel() {

    private var _listToDisplay = MutableStateFlow<ListOfTrackiesToDisplay>(ListOfTrackiesToDisplay.Today)
    val listToDisplay = _listToDisplay.asStateFlow()

    @Tested
    fun switchListToDisplay(listToDisplay: ListOfTrackiesToDisplay) {

        _listToDisplay.update {
            listToDisplay
        }
    }
}