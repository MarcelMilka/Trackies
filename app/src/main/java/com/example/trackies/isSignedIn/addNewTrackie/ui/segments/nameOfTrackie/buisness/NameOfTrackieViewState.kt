package com.example.trackies.isSignedIn.addNewTrackie.ui.segments.nameOfTrackie.buisness

import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.nameOfTrackie.staticValues.NameOfTrackieHeightOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.nameOfTrackie.staticValues.NameOfTrackieHintOptions

data class NameOfTrackieViewState(

    var targetHeightOfTheSurface: Int = NameOfTrackieHeightOptions.displayUnactivatedComponent,

    var displayFieldWithInsertedName: Boolean = false,
    var displayFieldWithTextField: Boolean = false,
    var hint: String = NameOfTrackieHintOptions.insertNewName,

    var error: Boolean = false
)