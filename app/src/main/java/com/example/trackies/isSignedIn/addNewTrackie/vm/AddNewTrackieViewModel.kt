package com.example.trackies.isSignedIn.addNewTrackie.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackies.isSignedIn.addNewTrackie.buisness.AddNewTrackieSegments
import com.example.trackies.isSignedIn.addNewTrackie.buisness.AddNewTrackieViewState
import com.example.trackies.isSignedIn.addNewTrackie.buisness.EnumMeasuringUnits
import com.example.trackies.isSignedIn.addNewTrackie.buisness.InsertDailyDosageViewState
import com.example.trackies.isSignedIn.addNewTrackie.buisness.InsertNameOfTrackieViewState
import com.example.trackies.isSignedIn.addNewTrackie.buisness.ScheduleDaysViewState
import com.example.trackies.isSignedIn.addNewTrackie.buisness.ScheduleTimeViewState
import com.example.trackies.isSignedIn.addNewTrackie.buisness.StatesOfSegments
import com.example.trackies.isSignedIn.addNewTrackie.presentation.insertDailyDosage.loadedSuccessfully.DailyDosageHints
import com.example.trackies.isSignedIn.addNewTrackie.presentation.insertDailyDosage.loadedSuccessfully.InsertDailyDosageFixedHeightValues
import com.example.trackies.isSignedIn.addNewTrackie.presentation.insertNameOfTrackie.loadedSuccessfully.InsertNameOfTrackieFixedHeightValues
import com.example.trackies.isSignedIn.addNewTrackie.presentation.insertNameOfTrackie.loadedSuccessfully.NameOfTrackieHints
import com.example.trackies.isSignedIn.addNewTrackie.presentation.scheduleDays.loadedSuccessfully.ScheduleDaysHints
import com.example.trackies.isSignedIn.addNewTrackie.presentation.scheduleDays.loadedSuccessfully.ScheduleDaysSetOfHeights
import com.example.trackies.isSignedIn.constantValues.DaysOfWeek
import com.example.trackies.isSignedIn.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewTrackieViewModel @Inject constructor(
    private var userRepository: UserRepository
): ViewModel() {

    val namesOfAllExistingTrackies = mutableListOf<String>()

    var addNewTrackieViewState = MutableStateFlow(AddNewTrackieViewState())
    var statesOfSegments = MutableStateFlow(StatesOfSegments())
    var buttonAddNewTrackieIsEnabled = MutableStateFlow(false)

    var insertNameOfTrackieViewState = MutableStateFlow(InsertNameOfTrackieViewState())
    var insertDailyDosageViewState = MutableStateFlow(InsertDailyDosageViewState())
    var scheduleDaysViewState = MutableStateFlow(ScheduleDaysViewState())
    var scheduleTimeViewState = MutableStateFlow(ScheduleTimeViewState())

    init {

        viewModelScope.launch {
            addNewTrackieViewState.collect {

                Log.d("Halla!", "vms $it")

                if (
                    it.name != "" &&
                    it.totalDose != 0 &&
                    it.measuringUnit != "" &&
                    it.repeatOn.count() != 0
                ) {
                    buttonAddNewTrackieIsEnabled.emit(value = true)
                }

                else {
                    buttonAddNewTrackieIsEnabled.emit(value = false)
                }
            }
        }
    }


    fun updateName(nameOfTheNewTrackie: String) {

        addNewTrackieViewState.update {
            it.copy(
                name = nameOfTheNewTrackie
            )
        }
    }

    fun updateMeasuringUnitAndDose(totalDose: Int, measuringUnit: String) {

        addNewTrackieViewState.update {
            it.copy(
                totalDose = totalDose,
                measuringUnit = measuringUnit
            )
        }
    }

    fun updateRepeatOn(repeatOn: MutableSet<String>) {

        addNewTrackieViewState.update {

            it.copy(
                repeatOn = repeatOn.toMutableList()
            )
        }
    }

    fun clearAll() {

        addNewTrackieViewState.update {

            it.copy(

                name = "",
                totalDose = 0,
                repeatOn = mutableListOf(),
                measuringUnit = "",
                ingestionTime = null
            )
        }

        statesOfSegments.update {

            it.copy(

                insertNameIsActive = false,
                insertTotalDoseIsActive = false,
                scheduleDaysIsActive = false,
                insertTimeOfIngestionIsActive = false
            )
        }

        insertNameOfTrackieViewState.update {

            it.copy(
                nameOfTrackie = "",
                targetHeightOfTheColumn = InsertNameOfTrackieFixedHeightValues.displayUnactivatedComponent,
                targetHeightOfTheSurface = InsertNameOfTrackieFixedHeightValues.displayUnactivatedComponent,
                displayFieldWithInsertedName = false,
                displayFieldWithTextField = false,
                hint = NameOfTrackieHints.insertNewName,
                error = false
            )
        }

        insertDailyDosageViewState.update {

            it.copy(
                measuringUnit = null,
                totalDailyDose = 1,
                mlIsChosen = false,
                gIsChosen = false,
                pcsIsChosen = false,
                targetHeightOfTheColumn = InsertDailyDosageFixedHeightValues.displayUnactivatedComponent,
                targetHeightOfTheSurface = InsertDailyDosageFixedHeightValues.displayUnactivatedComponent,
                displayFieldWithInsertedDose = false,
                displayFieldWithMeasuringUnits = false,
                displayFieldWithTextField = false,
                hint = DailyDosageHints.insertDailyDosage,
                error = false
            )
        }

        scheduleDaysViewState.update {

            it.copy(
                repeatOn = mutableSetOf(),
                mondayIsSelected = false,
                tuesdayIsSelected = false,
                wednesdayIsSelected = false,
                thursdayIsSelected = false,
                fridayIsSelected = false,
                saturdayIsSelected = false,
                sundayIsSelected = false,
                targetHeightOfTheColumn = ScheduleDaysSetOfHeights.displayUnactivatedComponent,
                targetHeightOfTheSurface = ScheduleDaysSetOfHeights.displayUnactivatedComponent,
                displayFieldWithChosenDaysOfWeek = false,
                displayFieldWithSelectableButtons = false,
                hint = ScheduleDaysHints.selectDaysOfWeek
            )
        }
    }

    fun activateSegment(segmentToActivate: AddNewTrackieSegments) {

        when (segmentToActivate) {

            AddNewTrackieSegments.NameOfTrackie -> {

                statesOfSegments.update { activityState ->
                    activityState.copy(
                        insertNameIsActive = true,
                        insertTotalDoseIsActive = false,
                        scheduleDaysIsActive = false,
                        insertTimeOfIngestionIsActive = false
                    )
                }
            }

            AddNewTrackieSegments.DailyDosage -> {
                statesOfSegments.update { activityState ->
                    activityState.copy(
                        insertNameIsActive = false,
                        insertTotalDoseIsActive = true,
                        scheduleDaysIsActive = false,
                        insertTimeOfIngestionIsActive = false
                    )
                }
            }

            AddNewTrackieSegments.ScheduleDays -> {
                statesOfSegments.update { activityState ->
                    activityState.copy(
                        insertNameIsActive = false,
                        insertTotalDoseIsActive = false,
                        scheduleDaysIsActive = true,
                        insertTimeOfIngestionIsActive = false
                    )
                }
            }

            AddNewTrackieSegments.TimeOfIngestion -> {
                statesOfSegments.update { activityState ->
                    activityState.copy(
                        insertNameIsActive = false,
                        insertTotalDoseIsActive = false,
                        scheduleDaysIsActive = false,
                        insertTimeOfIngestionIsActive = true
                    )
                }
            }
        }
    }

    fun deactivateSegment(segmentToDeactivate: AddNewTrackieSegments) {

        when (segmentToDeactivate) {
            AddNewTrackieSegments.NameOfTrackie -> {
                statesOfSegments.update { activityState ->
                    activityState.copy(
                        insertNameIsActive = false,
                    )
                }
            }

            AddNewTrackieSegments.DailyDosage -> {
                statesOfSegments.update { activityState ->
                    activityState.copy(
                        insertTotalDoseIsActive = false,
                    )
                }
            }

            AddNewTrackieSegments.ScheduleDays -> {
                statesOfSegments.update { activityState ->
                    activityState.copy(
                        scheduleDaysIsActive = false,
                    )
                }
            }

            AddNewTrackieSegments.TimeOfIngestion -> {
                statesOfSegments.update { activityState ->
                    activityState.copy(
                        insertTimeOfIngestionIsActive = false
                    )
                }
            }
        }
    }


//  InsertName operators
    fun nameOfTrackieInsertNewName(nameOfTrackie: String) {

        val hint = if (namesOfAllExistingTrackies.contains(nameOfTrackie)) {
            NameOfTrackieHints.nameAlreadyExists
        }

        else {
            NameOfTrackieHints.confirmNewName
        }

        insertNameOfTrackieViewState.update {
            it.copy(
                nameOfTrackie = nameOfTrackie,
                hint = hint
            )
        }
    }

    fun nameOfTrackieDisplayCollapsed() {

        insertNameOfTrackieViewState.update {

            it.copy(
                targetHeightOfTheColumn = InsertNameOfTrackieFixedHeightValues.displayUnactivatedComponent,
                targetHeightOfTheSurface = InsertNameOfTrackieFixedHeightValues.displayUnactivatedComponent,
                displayFieldWithInsertedName = false,
                displayFieldWithTextField = false,
                hint = NameOfTrackieHints.insertNewName
            )
        }
    }

    fun nameOfTrackieDisplayInsertedValue(nameOfTrackie: String) {

        var hint = ""
        var error = false

        if (namesOfAllExistingTrackies.contains(nameOfTrackie)) {
            hint = NameOfTrackieHints.nameAlreadyExists
            error = true
        }

        else {
            hint = NameOfTrackieHints.confirmNewName
            error = false
        }

        insertNameOfTrackieViewState.update {

            it.copy(
                targetHeightOfTheColumn = InsertNameOfTrackieFixedHeightValues.displayInsertedName,
                targetHeightOfTheSurface = InsertNameOfTrackieFixedHeightValues.displayInsertedName,
                displayFieldWithInsertedName = true,
                displayFieldWithTextField = false,
                hint = hint,
                error = error
            )
        }
    }

    fun nameOfTrackieDisplayTextField(nameOfTrackie: String) {

        var hint = ""
        var error = false

        if (namesOfAllExistingTrackies.contains(nameOfTrackie)) {
            hint = NameOfTrackieHints.nameAlreadyExists
            error = true
        }
        else {
            hint = NameOfTrackieHints.confirmNewName
            error = false
        }

        insertNameOfTrackieViewState.update {

            it.copy(
                targetHeightOfTheColumn = InsertNameOfTrackieFixedHeightValues.displayTextField,
                targetHeightOfTheSurface = InsertNameOfTrackieFixedHeightValues.displayTextField,
                displayFieldWithInsertedName = false,
                displayFieldWithTextField = true,
                hint = hint,
                error = error
            )
        }
    }


//  InsertDailyDosage operators
    fun dailyDoseInsertMeasuringUnit(measuringUnit: EnumMeasuringUnits) {

        insertDailyDosageViewState.update {

            when (measuringUnit) {

                EnumMeasuringUnits.ml -> {
                    it.copy(
                        measuringUnit = measuringUnit,
                        mlIsChosen = true,
                        gIsChosen = false,
                        pcsIsChosen = false
                    )
                }

                EnumMeasuringUnits.g -> {
                    it.copy(
                        measuringUnit = measuringUnit,
                        mlIsChosen = false,
                        gIsChosen = true,
                        pcsIsChosen = false
                    )
                }

                EnumMeasuringUnits.pcs -> {
                    it.copy(
                        measuringUnit = measuringUnit,
                        mlIsChosen = false,
                        gIsChosen = false,
                        pcsIsChosen = true
                    )
                }
            }
        }
    }

    fun dailyDoseInsertTotalDose(totalDose: Int) {

        insertDailyDosageViewState.update {
            it.copy(
                totalDailyDose = totalDose
            )
        }
    }

    fun dailyDoseDisplayCollapsed() {

        insertDailyDosageViewState.update {
            it.copy(
                targetHeightOfTheColumn = InsertDailyDosageFixedHeightValues.displayUnactivatedComponent,
                targetHeightOfTheSurface = InsertDailyDosageFixedHeightValues.displayUnactivatedComponent,

                displayFieldWithInsertedDose = false,
                displayFieldWithMeasuringUnits = false,
                displayFieldWithTextField = false,

                hint = DailyDosageHints.insertDailyDosage
            )
        }
    }

    fun dailyDoseDisplayInsertedValue() {

        insertDailyDosageViewState.update {
            it.copy(
                targetHeightOfTheColumn = InsertDailyDosageFixedHeightValues.displayFieldWithInsertedDose,
                targetHeightOfTheSurface = InsertDailyDosageFixedHeightValues.displayFieldWithInsertedDose,

                displayFieldWithInsertedDose = true,
                displayFieldWithMeasuringUnits = false,
                displayFieldWithTextField = false,

                hint = DailyDosageHints.editDailyDosage
            )
        }
    }

    fun dailyDoseDisplayMeasuringUnitsToChoose() {

        insertDailyDosageViewState.update {
            it.copy(
                targetHeightOfTheColumn = InsertDailyDosageFixedHeightValues.displayFieldWithAvailableMeasuringUnitsToChoose,
                targetHeightOfTheSurface = InsertDailyDosageFixedHeightValues.displayFieldWithAvailableMeasuringUnitsToChoose,

                displayFieldWithInsertedDose = false,
                displayFieldWithMeasuringUnits = true,
                displayFieldWithTextField = false,

                hint = DailyDosageHints.chooseMeasuringUnitAndInsertDose
            )
        }
    }

    fun dailyDoseDisplayTextField() {

        insertDailyDosageViewState.update {
            it.copy(
                targetHeightOfTheColumn = InsertDailyDosageFixedHeightValues.displayTextField,
                targetHeightOfTheSurface = InsertDailyDosageFixedHeightValues.displayTextField,

                displayFieldWithInsertedDose = false,
                displayFieldWithMeasuringUnits = true,
                displayFieldWithTextField = true,

                hint = DailyDosageHints.confirmDailyDosage
            )
        }
    }


//  ScheduleDays operators
    fun scheduleDaysInsertDayOfWeek(dayOfWeek: String) {

        when(dayOfWeek) {

            DaysOfWeek.monday -> {
                scheduleDaysViewState.update {
                    it.copy(mondayIsSelected = true)
                }
            }

            DaysOfWeek.tuesday -> {
                scheduleDaysViewState.update {
                    it.copy(tuesdayIsSelected = true)
                }
            }

            DaysOfWeek.wednesday -> {
                scheduleDaysViewState.update {
                    it.copy(wednesdayIsSelected = true)
                }
            }

            DaysOfWeek.thursday -> {
                scheduleDaysViewState.update {
                    it.copy(thursdayIsSelected = true)
                }
            }

            DaysOfWeek.friday -> {
                scheduleDaysViewState.update {
                    it.copy(fridayIsSelected = true)
                }
            }

            DaysOfWeek.saturday -> {
                scheduleDaysViewState.update {
                    it.copy(saturdayIsSelected = true)
                }
            }

            DaysOfWeek.sunday -> {
                scheduleDaysViewState.update {
                    it.copy(sundayIsSelected = true)
                }
            }

        }

        val outdatedRepeatOn: MutableSet<String> = scheduleDaysViewState.value.repeatOn
        val updatedRepeatOn: MutableSet<String> = outdatedRepeatOn.toMutableSet()
        updatedRepeatOn.add(dayOfWeek)

        scheduleDaysViewState.update {

            it.copy(
                repeatOn = updatedRepeatOn
            )
        }
    }

    fun scheduleDaysExtractDayOfWeek(dayOfWeek: String) {

        when(dayOfWeek) {

            DaysOfWeek.monday -> {
                scheduleDaysViewState.update {
                    it.copy(mondayIsSelected = false)
                }
            }

            DaysOfWeek.tuesday -> {
                scheduleDaysViewState.update {
                    it.copy(tuesdayIsSelected = false)
                }
            }

            DaysOfWeek.wednesday -> {
                scheduleDaysViewState.update {
                    it.copy(wednesdayIsSelected = false)
                }
            }

            DaysOfWeek.thursday -> {
                scheduleDaysViewState.update {
                    it.copy(thursdayIsSelected = false)
                }
            }

            DaysOfWeek.friday -> {
                scheduleDaysViewState.update {
                    it.copy(fridayIsSelected = false)
                }
            }

            DaysOfWeek.saturday -> {
                scheduleDaysViewState.update {
                    it.copy(saturdayIsSelected = false)
                }
            }

            DaysOfWeek.sunday -> {
                scheduleDaysViewState.update {
                    it.copy(sundayIsSelected = false)
                }
            }

        }

        val outdatedRepeatOn: MutableSet<String> = scheduleDaysViewState.value.repeatOn
        val updatedRepeatOn: MutableSet<String> = outdatedRepeatOn.toMutableSet()
        updatedRepeatOn.remove(dayOfWeek)

        scheduleDaysViewState.update {

            it.copy(
                repeatOn = updatedRepeatOn
            )
        }
    }

    fun scheduleDaysDisplayCollapsed() {

        scheduleDaysViewState.update {
            it.copy(
                targetHeightOfTheColumn = ScheduleDaysSetOfHeights.displayUnactivatedComponent,
                targetHeightOfTheSurface = ScheduleDaysSetOfHeights.displayUnactivatedComponent,

                displayFieldWithChosenDaysOfWeek = false,
                displayFieldWithSelectableButtons = false,

                hint = ScheduleDaysHints.selectDaysOfWeek
            )
        }
    }

    fun scheduleDaysDisplayScheduledDays() {

        scheduleDaysViewState.update {
            it.copy(
                targetHeightOfTheColumn = ScheduleDaysSetOfHeights.displayChosenDaysOfWeek,
                targetHeightOfTheSurface = ScheduleDaysSetOfHeights.displayChosenDaysOfWeek,

                displayFieldWithChosenDaysOfWeek = false,
                displayFieldWithSelectableButtons = true,

                hint = ScheduleDaysHints.editSelectedDaysOfWeek
            )
        }
    }

    fun scheduleDaysDisplayDaysToSchedule() {

        scheduleDaysViewState.update {
            it.copy(
                targetHeightOfTheColumn = ScheduleDaysSetOfHeights.displayRadioButtons,
                targetHeightOfTheSurface = ScheduleDaysSetOfHeights.displayRadioButtons,

                displayFieldWithChosenDaysOfWeek = false,
                displayFieldWithSelectableButtons = true,

                hint = ScheduleDaysHints.confirmSelectedDaysOfWeek
            )
        }
    }

//  Schedule time of ingestion operators
    fun scheduleTimeDisplayButton() {

    }
}