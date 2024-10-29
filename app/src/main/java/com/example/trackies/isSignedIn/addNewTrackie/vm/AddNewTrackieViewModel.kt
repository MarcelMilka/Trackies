package com.example.trackies.isSignedIn.addNewTrackie.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackies.isSignedIn.addNewTrackie.buisness.AddNewTrackieSegments
import com.example.trackies.isSignedIn.addNewTrackie.buisness.AddNewTrackieModel
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.dailyDose.buisness.EnumMeasuringUnits
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.buisness.TimeOfIngestionEntity
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.dailyDose.buisness.DailyDoseViewState
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.nameOfTrackie.buisness.NameOfTrackieViewState
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.scheduleDays.buisness.ScheduleDaysViewState
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.buisness.TimeOfIngestionViewState
import com.example.trackies.isSignedIn.addNewTrackie.buisness.ActivityStatesOfSegments
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.buisness.convertIntoTimeOfIngestion
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.dailyDose.staticValues.DailyDoseHintOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.dailyDose.staticValues.DailyDoseHeightOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.nameOfTrackie.staticValues.NameOfTrackieHeightOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.nameOfTrackie.staticValues.NameOfTrackieHintOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.scheduleDays.staticValues.ScheduleDaysHintOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.scheduleDays.staticValues.ScheduleDaysHeightOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.staticValues.TimeOfIngestionHintOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.staticValues.TimeOfIngestionHeightOptions
import com.example.globalConstants.DaysOfWeek
import com.example.trackies.isSignedIn.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewTrackieViewModel @Inject constructor(
    private var repository: UserRepository
): ViewModel() {

    val namesOfAllExistingTrackies = mutableListOf<String>()

    var addNewTrackieModel = MutableStateFlow(AddNewTrackieModel())
    var activityStatesOfSegments = MutableStateFlow(ActivityStatesOfSegments())
    var buttonAddNewTrackieIsEnabled = MutableStateFlow(false)

    var nameOfTrackieViewState = MutableStateFlow(NameOfTrackieViewState())
    var dailyDoseViewState = MutableStateFlow(DailyDoseViewState())
    var scheduleDaysViewState = MutableStateFlow(ScheduleDaysViewState())
    var timeOfIngestionViewState = MutableStateFlow(TimeOfIngestionViewState())

    init {

        viewModelScope.launch {

//          Enabling/disabling button responsible for adding new Trackie
            this.launch {

                addNewTrackieModel.collect {

                    if (
                        it.name != "" &&
                        it.dose != 0 &&
                        it.measuringUnit != null &&
                        it.repeatOn.count() != 0
                    ) {
                        buttonAddNewTrackieIsEnabled.emit(value = true)
                    }

                    else {
                        buttonAddNewTrackieIsEnabled.emit(value = false)
                    }
                }
            }


//          Following and updating UI state of the segment 'NameOfTrackie'
            this.launch {

                activityStatesOfSegments.collect {

//                  when this segment is active...
                    when (it.nameOfTrackieIsActive) {

//                      Expand
                        true -> {
                            nameOfTrackieDisplayTextField()
                        }

//                      Collapse
                        false -> {

                            if (addNewTrackieModel.value.name != "") {
                                nameOfTrackieDisplayInsertedValue()
                            }

                            else {
                                nameOfTrackieDisplayCollapsed()
                            }
                        }
                    }
                }
            }



//          Following and updating UI state of the segment 'DailyDose'
            this.launch {

                addNewTrackieModel.collect {

                    if (
                        it.measuringUnit != null &&
                        activityStatesOfSegments.value.dailyDoseIsActive
                        ) {

                        dailyDoseDisplayTextField()
                    }
                }
            }

            this.launch {

                activityStatesOfSegments.collect {

                    when (it.dailyDoseIsActive) {

//                      Expand
                        true -> {

                            if (addNewTrackieModel.value.measuringUnit == null) {

                                dailyDoseDisplayMeasuringUnitsToChoose()
                            }

                            else {

                                dailyDoseDisplayTextField()
                            }
                        }

//                      Collapse
                        false -> {

                            if (addNewTrackieModel.value.measuringUnit == null) {

                                dailyDoseDisplayCollapsed()
                            }

                            else {

                                dailyDoseDisplayInsertedValue()
                            }
                        }
                    }
                }
            }


//          Following and updating UI state of the segment 'ScheduleDays'
            this.launch {

                activityStatesOfSegments.collect {

                    when (it.scheduleDaysIsActive) {

                        true -> {}

                        false -> {}
                    }
                }
            }


//          Following and updating UI state of the segment 'TimeOfIngestion'
            this.launch {

                activityStatesOfSegments.collect {

                    when (it.timeOfIngestionIsActive) {

                        true -> {}

                        false -> {}
                    }
                }
            }
        }
    }

//  Operators of 'AddNewTrackieViewState'
    fun updateName(name: String) {

        addNewTrackieModel.update {
            it.copy(
                name = name
            )
        }
    }

    fun updateDose(dose: Int) {

        addNewTrackieModel.update {
            it.copy(
                dose = dose,
            )
        }
    }

    fun updateMeasuringUnit(measuringUnit: EnumMeasuringUnits) {

        addNewTrackieModel.update {
            it.copy(
                measuringUnit = measuringUnit
            )
        }
    }

    fun updateRepeatOn(repeatOn: MutableSet<String>) {

        addNewTrackieModel.update {

            it.copy(
                repeatOn = repeatOn.toMutableList()
            )
        }
    }

    fun updateIngestionTime(ingestionTimeEntity: TimeOfIngestionEntity?) {

        val ingestionTime = ingestionTimeEntity?.convertIntoTimeOfIngestion()

        timeOfIngestionViewState.update {
            it.copy(
                ingestionTime = ingestionTime
            )
        }
    }

    fun clearAll() {

        addNewTrackieModel.update {

            it.copy(

                name = "",
                dose = 0,
                repeatOn = mutableListOf(),
                measuringUnit = null,
                ingestionTime = null
            )
        }

        activityStatesOfSegments.update {

            it.copy(

                nameOfTrackieIsActive = false,
                dailyDoseIsActive = false,
                scheduleDaysIsActive = false,
                timeOfIngestionIsActive = false
            )
        }

        nameOfTrackieViewState.update {

            it.copy(
                targetHeightOfTheSurface = NameOfTrackieHeightOptions.displayUnactivatedComponent,
                displayFieldWithInsertedName = false,
                displayFieldWithTextField = false,
                hint = NameOfTrackieHintOptions.insertNewName,
                error = false
            )
        }

        dailyDoseViewState.update {

            it.copy(
                targetHeightOfTheSurface = DailyDoseHeightOptions.displayUnactivatedComponent,
                displayFieldWithInsertedDose = false,
                displayFieldWithMeasuringUnits = false,
                displayFieldWithTextField = false,
                hint = DailyDoseHintOptions.insertDailyDosage,
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
                targetHeightOfTheColumn = ScheduleDaysHeightOptions.displayUnactivatedComponent,
                targetHeightOfTheSurface = ScheduleDaysHeightOptions.displayUnactivatedComponent,
                displayFieldWithChosenDaysOfWeek = false,
                displayFieldWithSelectableButtons = false,
                hint = ScheduleDaysHintOptions.selectDaysOfWeek
            )
        }

        timeOfIngestionViewState.update {

            it.copy(
                targetHeightOfTheSurface = TimeOfIngestionHeightOptions.displayUnactivatedComponent,
                hint = TimeOfIngestionHintOptions.clickToInsertTimeOfIngestion,
                ingestionTime = null,
                displayContentInTimeComponent = false
            )
        }
    }

    fun activateSegment(segmentToActivate: AddNewTrackieSegments) {

        when (segmentToActivate) {

            AddNewTrackieSegments.NameOfTrackie -> {

                activityStatesOfSegments.update { activityState ->
                    activityState.copy(
                        nameOfTrackieIsActive = true,
                        dailyDoseIsActive = false,
                        scheduleDaysIsActive = false,
                        timeOfIngestionIsActive = false
                    )
                }
            }

            AddNewTrackieSegments.DailyDose -> {
                activityStatesOfSegments.update { activityState ->
                    activityState.copy(
                        nameOfTrackieIsActive = false,
                        dailyDoseIsActive = true,
                        scheduleDaysIsActive = false,
                        timeOfIngestionIsActive = false
                    )
                }
            }

            AddNewTrackieSegments.ScheduleDays -> {
                activityStatesOfSegments.update { activityState ->
                    activityState.copy(
                        nameOfTrackieIsActive = false,
                        dailyDoseIsActive = false,
                        scheduleDaysIsActive = true,
                        timeOfIngestionIsActive = false
                    )
                }
            }

            AddNewTrackieSegments.TimeOfIngestion -> {
                activityStatesOfSegments.update { activityState ->
                    activityState.copy(
                        nameOfTrackieIsActive = false,
                        dailyDoseIsActive = false,
                        scheduleDaysIsActive = false,
                        timeOfIngestionIsActive = true
                    )
                }
            }
        }
    }

    fun deactivateSegment(segmentToDeactivate: AddNewTrackieSegments) {

        when (segmentToDeactivate) {
            AddNewTrackieSegments.NameOfTrackie -> {
                activityStatesOfSegments.update { activityState ->
                    activityState.copy(
                        nameOfTrackieIsActive = false,
                    )
                }
            }

            AddNewTrackieSegments.DailyDose -> {
                activityStatesOfSegments.update { activityState ->
                    activityState.copy(
                        dailyDoseIsActive = false,
                    )
                }
            }

            AddNewTrackieSegments.ScheduleDays -> {
                activityStatesOfSegments.update { activityState ->
                    activityState.copy(
                        scheduleDaysIsActive = false,
                    )
                }
            }

            AddNewTrackieSegments.TimeOfIngestion -> {
                activityStatesOfSegments.update { activityState ->
                    activityState.copy(
                        timeOfIngestionIsActive = false
                    )
                }
            }
        }
    }


//  'NameOfTrackie' operators
    private fun nameOfTrackieDisplayCollapsed() {

        nameOfTrackieViewState.update {

            it.copy(
                targetHeightOfTheSurface = NameOfTrackieHeightOptions.displayUnactivatedComponent,
                displayFieldWithInsertedName = false,
                displayFieldWithTextField = false,
                hint = NameOfTrackieHintOptions.insertNewName
            )
        }
    }

    private fun nameOfTrackieDisplayTextField() {

        val currentNameOfTrackie = addNewTrackieModel.value.name

        var hint = ""
        var error = false

        if (namesOfAllExistingTrackies.contains(currentNameOfTrackie)) {
            hint = NameOfTrackieHintOptions.nameAlreadyExists
            error = true
        }
        else {
            hint = NameOfTrackieHintOptions.confirmNewName
            error = false
        }

        nameOfTrackieViewState.update {

            it.copy(
                targetHeightOfTheSurface = NameOfTrackieHeightOptions.displayTextField,
                displayFieldWithInsertedName = false,
                displayFieldWithTextField = true,
                hint = hint,
                error = error
            )
        }
    }

    private fun nameOfTrackieDisplayInsertedValue() {

        val currentNameOfTrackie = addNewTrackieModel.value.name

        var hint = ""
        var error = false

        if (namesOfAllExistingTrackies.contains(currentNameOfTrackie)) {
            hint = NameOfTrackieHintOptions.nameAlreadyExists
            error = true
        }

        else {
            hint = NameOfTrackieHintOptions.confirmNewName
            error = false
        }

        nameOfTrackieViewState.update {

            it.copy(
                targetHeightOfTheSurface = NameOfTrackieHeightOptions.displayInsertedName,
                displayFieldWithInsertedName = true,
                displayFieldWithTextField = false,
                hint = hint,
                error = error
            )
        }
    }


//  'DailyDose' operators
    private fun dailyDoseDisplayCollapsed() {

        dailyDoseViewState.update {

            it.copy(
                targetHeightOfTheSurface = DailyDoseHeightOptions.displayUnactivatedComponent,

                displayFieldWithInsertedDose = false,
                displayFieldWithMeasuringUnits = false,
                displayFieldWithTextField = false,
                hint = DailyDoseHintOptions.insertDailyDosage,
            )
        }
    }

    private fun dailyDoseDisplayMeasuringUnitsToChoose() {

        dailyDoseViewState.update {
            it.copy(
                targetHeightOfTheSurface = DailyDoseHeightOptions.displayFieldWithAvailableMeasuringUnitsToChoose,

                displayFieldWithInsertedDose = false,
                displayFieldWithMeasuringUnits = true,
                displayFieldWithTextField = false,

                hint = DailyDoseHintOptions.chooseMeasuringUnitAndInsertDose
            )
        }
    }

    private fun dailyDoseDisplayTextField() {

        dailyDoseViewState.update {
            it.copy(
                targetHeightOfTheSurface = DailyDoseHeightOptions.displayTextField,

                displayFieldWithInsertedDose = false,
                displayFieldWithMeasuringUnits = true,
                displayFieldWithTextField = true,

                hint = DailyDoseHintOptions.confirmDailyDosage
            )
        }
    }

    private fun dailyDoseDisplayInsertedValue() {

        dailyDoseViewState.update {

            it.copy(
                targetHeightOfTheSurface = DailyDoseHeightOptions.displayFieldWithInsertedDose,

                displayFieldWithInsertedDose = true,
                displayFieldWithMeasuringUnits = false,
                displayFieldWithTextField = false,
                hint = DailyDoseHintOptions.editDailyDosage,
            )
        }
    }


//  'ScheduleDays' operators
    fun scheduleDaysDisplayCollapsed() {

    scheduleDaysViewState.update {
        it.copy(
            targetHeightOfTheColumn = ScheduleDaysHeightOptions.displayUnactivatedComponent,
            targetHeightOfTheSurface = ScheduleDaysHeightOptions.displayUnactivatedComponent,

            displayFieldWithChosenDaysOfWeek = false,
            displayFieldWithSelectableButtons = false,

            hint = ScheduleDaysHintOptions.selectDaysOfWeek
        )
    }
}

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

    fun scheduleDaysDisplayScheduledDays() {

        scheduleDaysViewState.update {
            it.copy(
                targetHeightOfTheColumn = ScheduleDaysHeightOptions.displayChosenDaysOfWeek,
                targetHeightOfTheSurface = ScheduleDaysHeightOptions.displayChosenDaysOfWeek,

                displayFieldWithChosenDaysOfWeek = false,
                displayFieldWithSelectableButtons = true,

                hint = ScheduleDaysHintOptions.editSelectedDaysOfWeek
            )
        }
    }

    fun scheduleDaysDisplayDaysToSchedule() {

        scheduleDaysViewState.update {
            it.copy(
                targetHeightOfTheColumn = ScheduleDaysHeightOptions.displayRadioButtons,
                targetHeightOfTheSurface = ScheduleDaysHeightOptions.displayRadioButtons,

                displayFieldWithChosenDaysOfWeek = false,
                displayFieldWithSelectableButtons = true,

                hint = ScheduleDaysHintOptions.confirmSelectedDaysOfWeek
            )
        }
    }


//  'TimeOfIngestion' operators
    fun scheduleTimeDisplayUnactivated() {

        timeOfIngestionViewState.update {
            it.copy(
                targetHeightOfTheSurface = TimeOfIngestionHeightOptions.displayUnactivatedComponent,
                hint = TimeOfIngestionHintOptions.clickToInsertTimeOfIngestion,
                ingestionTime = null,
                displayContentInTimeComponent = false
            )
        }
    }

    fun scheduleTimeDisplayActivatedTimeComponent() {

        timeOfIngestionViewState.update {
            it.copy(
                targetHeightOfTheSurface = TimeOfIngestionHeightOptions.displayActivatedTimeComponent,
                hint = TimeOfIngestionHintOptions.clickToConfirmIngestionTime,
                displayContentInTimeComponent = true
            )
        }
    }

    fun scheduleTimeDisplayUnactivatedTimeComponent() {

        timeOfIngestionViewState.update {
            it.copy(
                targetHeightOfTheSurface = TimeOfIngestionHeightOptions.displayUnactivatedTimeComponent,
                hint = TimeOfIngestionHintOptions.clickToEditOrDeleteTimeOfIngestion,
                displayContentInTimeComponent = false
            )
        }
    }
}