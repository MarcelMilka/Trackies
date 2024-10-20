package com.example.trackies.isSignedIn.addNewTrackie.buisness

import com.example.trackies.isSignedIn.addNewTrackie.presentation.insertNameOfTrackie.loadedSuccessfully.InsertNameOfTrackieFixedHeightValues
import com.example.trackies.isSignedIn.addNewTrackie.presentation.insertNameOfTrackie.loadedSuccessfully.NameOfTrackieHints

data class InsertNameOfTrackieViewState(
    var nameOfTrackie: String = "",

    var targetHeightOfTheColumn: Int = InsertNameOfTrackieFixedHeightValues.displayUnactivatedComponent,
    var targetHeightOfTheSurface: Int = InsertNameOfTrackieFixedHeightValues.displayUnactivatedComponent,

    var displayFieldWithInsertedName: Boolean = false,
    var displayFieldWithTextField: Boolean = false,
    var hint: String = NameOfTrackieHints.insertNewName,

    var error: Boolean = false
)