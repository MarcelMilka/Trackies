package com.example.trackies.isSignedIn.addNewTrackie.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.globalConstants.MeasuringUnit
import com.example.globalConstants.annotationClasses.Tested
import com.example.trackies.isSignedIn.addNewTrackie.buisness.AddNewTrackieSegments
import com.example.trackies.isSignedIn.addNewTrackie.buisness.AddNewTrackieModel
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewTrackieViewModel @Inject constructor(): ViewModel() {

    var namesOfAllExistingTrackies = MutableStateFlow<List<String>>(listOf())

    var addNewTrackieModel = MutableStateFlow(AddNewTrackieModel())
    var activityStatesOfSegments = MutableStateFlow(ActivityStatesOfSegments())
    var buttonAddNewTrackieIsEnabled = MutableStateFlow(false)

    var nameOfTrackieViewState = MutableStateFlow(NameOfTrackieViewState())
    var dailyDoseViewState = MutableStateFlow(DailyDoseViewState())
    var scheduleDaysViewState = MutableStateFlow(ScheduleDaysViewState())
    var timeOfIngestionViewState = MutableStateFlow(TimeOfIngestionViewState())

    @Tested
    fun importNamesOfAllTrackies(namesOfAllTrackies: List<String>) {

        namesOfAllExistingTrackies.update {

            namesOfAllTrackies
        }
    }

    init {

        viewModelScope.launch {

//          Enabling/disabling button responsible for adding new Trackie
            this.launch {

                addNewTrackieModel.collect {

                    if (
                        it.name != "" &&
                        it.dose != 0 &&
                        it.measuringUnit != null &&
                        it.repeatOn.count() != 0 &&
                        activityStatesOfSegments.value.nameOfTrackieIsActive == false &&
                        activityStatesOfSegments.value.dailyDoseIsActive == false &&
                        activityStatesOfSegments.value.scheduleDaysIsActive == false &&
                        activityStatesOfSegments.value.timeOfIngestionIsActive == false
                    ) {

                        buttonAddNewTrackieIsEnabled.emit(
                            value = true
                        )
                    }

                    else {

                        buttonAddNewTrackieIsEnabled.emit(
                            value = false
                        )
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

            this.launch {

                addNewTrackieModel.collect {

                    if (activityStatesOfSegments.value.nameOfTrackieIsActive) {

                        if (it.name == "") {

                            nameOfTrackieChangeHint(
                                targetHint = NameOfTrackieHintOptions.nameCannotBeEmpty
                            )
                        }

                        else {

                            if (namesOfAllExistingTrackies.value.contains(it.name)) {

                                nameOfTrackieChangeHint(
                                    targetHint = NameOfTrackieHintOptions.nameAlreadyExists
                                )
                            }

                            else {

                                nameOfTrackieChangeHint(
                                    targetHint = NameOfTrackieHintOptions.confirmNewName
                                )
                            }
                        }
                    }
                }
            }


//          Following and updating UI state of the segment 'DailyDose'
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

            this.launch {

                addNewTrackieModel
                    .combine(activityStatesOfSegments) { model, states -> CombinedModelAndStates(model, states) }
                    .combine(dailyDoseViewState) { dataClass, viewState ->

                        CombinedModelStatesAndViewState(
                            dataClass.model,
                            dataClass.states,
                            viewState
                        )
                    }
                    .collect {

                        when (it.states.dailyDoseIsActive) {

//                          activated
                            true -> {

                                when (it.viewState.targetHeightOfTheSurface) {

                                    DailyDoseHeightOptions.displayFieldWithAvailableMeasuringUnitsToChoose -> {

                                        if (it.model.measuringUnit == null) {

                                            dailyDoseChangeHint(
                                                targetHint = DailyDoseHintOptions.chooseMeasuringUnitAndInsertDose
                                            )
                                        }
                                    }

                                    DailyDoseHeightOptions.displayTextField -> {

                                        if (it.model.dose == 0) {

                                            dailyDoseChangeHint(
                                                targetHint = DailyDoseHintOptions.dailyDoseCannotBeZero
                                            )
                                        }

                                        else  {

                                            dailyDoseChangeHint(
                                                targetHint = DailyDoseHintOptions.confirmDailyDosage
                                            )
                                        }
                                    }
                                }
                            }

//                          deactivated
                            false -> {

                                when (it.viewState.targetHeightOfTheSurface) {

                                    DailyDoseHeightOptions.displayUnactivatedSegment -> {

                                        dailyDoseChangeHint(
                                            targetHint = DailyDoseHintOptions.insertDailyDose
                                        )
                                    }

                                    DailyDoseHeightOptions.displayFieldWithInsertedDose -> {

                                        if (it.model.dose == 0) {

                                            dailyDoseChangeHint(
                                                targetHint = DailyDoseHintOptions.dailyDoseCannotBeZero
                                            )
                                        }

                                        else {

                                            dailyDoseChangeHint(
                                                targetHint = DailyDoseHintOptions.editDailyDosage
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
            }

            this.launch {

                addNewTrackieModel.collect {

                    if (it.measuringUnit != null && activityStatesOfSegments.value.dailyDoseIsActive) {

                        dailyDoseDisplayTextField()
                    }
                }
            }

//          Following and updating UI state of the segment 'ScheduleDays'
            this.launch {

                activityStatesOfSegments.collect {

                    when (it.scheduleDaysIsActive) {

//                      Expand
                        true -> {

                            scheduleDaysDisplayDaysToSchedule()
                        }

//                      Collapse
                        false -> {

                            if (addNewTrackieModel.value.repeatOn.isEmpty()) {

                                scheduleDaysDisplayCollapsed()
                            }

                            else {

                                scheduleDaysDisplayScheduledDays()
                            }
                        }
                    }
                }
            }

            this.launch {

                addNewTrackieModel
                    .combine(activityStatesOfSegments) { model, states ->

                        CombinedModelAndStates(model, states)
                    }
                    .collect {

                        when (it.states.scheduleDaysIsActive) {

                            true -> {

                                if (it.model.repeatOn.isEmpty()) {

                                    scheduleDaysChangeHint(
                                        targetHint = ScheduleDaysHintOptions.selectAtLeastOneDayOfWeek
                                    )
                                }

                                else {

                                    scheduleDaysChangeHint(
                                        targetHint = ScheduleDaysHintOptions.confirmScheduledDays
                                    )
                                }
                            }

                            false -> {

                                if (it.model.repeatOn.isEmpty()) {

                                    scheduleDaysChangeHint(
                                        targetHint = ScheduleDaysHintOptions.scheduleDaysOfIngestion
                                    )
                                }

                                else {

                                    scheduleDaysChangeHint(
                                        targetHint = ScheduleDaysHintOptions.editScheduledDays
                                    )
                                }
                            }
                        }
                    }
            }


//          Following and updating UI state of the segment 'TimeOfIngestion'
            this.launch {

                if (addNewTrackieModel.value.ingestionTime == null) {

                    scheduleTimeDisplayUnactivated()
                }
            }

            this.launch {

                activityStatesOfSegments.collect {

                    when (it.timeOfIngestionIsActive) {

//                      Expand
                        true -> {

                            scheduleTimeDisplayActivatedTimeComponent()
                        }

//                      Collapse
                        false -> {

                            if (addNewTrackieModel.value.ingestionTime == null) {

                                scheduleTimeDisplayUnactivated()
                            }

                            else {

                                scheduleTimeDisplayUnactivatedTimeComponent()
                            }
                        }
                    }
                }
            }
        }
    }

    private data class CombinedModelAndStates(
        var model: AddNewTrackieModel,
        var states: ActivityStatesOfSegments
    )

    private data class CombinedModelStatesAndViewState(
        var model: AddNewTrackieModel,
        var states: ActivityStatesOfSegments,
        val viewState: DailyDoseViewState
    )

//  Operators of 'AddNewTrackieViewState'
    @Tested
    fun updateName(name: String) {

        addNewTrackieModel.update {
            it.copy(
                name = name
            )
        }
    }

    @Tested
    fun updateDose(dose: Int) {

        addNewTrackieModel.update {
            it.copy(
                dose = dose,
            )
        }
    }

    @Tested
    fun updateMeasuringUnit(measuringUnit: MeasuringUnit) {

        addNewTrackieModel.update {
            it.copy(
                measuringUnit = measuringUnit
            )
        }
    }

    @Tested
    fun updateRepeatOn(repeatOn: Set<String>) {

        addNewTrackieModel.update {
            it.copy(
                repeatOn = repeatOn,
            )
        }
    }

    @Tested
    fun updateTimeOfIngestion(ingestionTimeEntity: TimeOfIngestionEntity?) {

        val ingestionTime = ingestionTimeEntity?.convertIntoTimeOfIngestion()

        addNewTrackieModel.update {

            it.copy(
                ingestionTime = ingestionTime
            )
        }
    }

    @Tested
    fun clearAll() {

        addNewTrackieModel.update {

            it.copy(

                name = "",
                dose = 0,
                repeatOn = mutableSetOf(),
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
                targetHeightOfTheSurface = DailyDoseHeightOptions.displayUnactivatedSegment,
                displayFieldWithInsertedDose = false,
                displayFieldWithMeasuringUnits = false,
                displayFieldWithTextField = false,
                hint = DailyDoseHintOptions.insertDailyDose,
                error = false
            )
        }

        scheduleDaysViewState.update {

            it.copy(
                targetHeightOfTheSurface = ScheduleDaysHeightOptions.displayDeactivated,
                displayScheduledDaysOfWeek = false,
                displayChips = false,
                hint = ScheduleDaysHintOptions.scheduleDaysOfIngestion
            )
        }

        timeOfIngestionViewState.update {

            it.copy(
                targetHeightOfTheSurface = TimeOfIngestionHeightOptions.displayUnactivatedComponent,
                hint = TimeOfIngestionHintOptions.clickToInsertTimeOfIngestion,
                displayContentInTimeComponent = false
            )
        }
    }

    @Tested
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

    @Tested
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

        if (namesOfAllExistingTrackies.value.contains(currentNameOfTrackie)) {
            hint = NameOfTrackieHintOptions.nameAlreadyExists
            error = true
        }

        else {

            hint = if (currentNameOfTrackie == "") {

                NameOfTrackieHintOptions.nameCannotBeEmpty
            }

            else {

                NameOfTrackieHintOptions.confirmNewName
            }

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

        if (namesOfAllExistingTrackies.value.contains(currentNameOfTrackie)) {
            hint = NameOfTrackieHintOptions.nameAlreadyExists
            error = true
        }

        else {
            hint = NameOfTrackieHintOptions.editNewName
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

    private fun nameOfTrackieChangeHint(targetHint: String) {

        nameOfTrackieViewState.update {

            it.copy(
                hint = targetHint,
            )
        }
    }


//  'DailyDose' operators
    private fun dailyDoseDisplayCollapsed() {

        dailyDoseViewState.update {

            it.copy(
                targetHeightOfTheSurface = DailyDoseHeightOptions.displayUnactivatedSegment,

                displayFieldWithInsertedDose = false,
                displayFieldWithMeasuringUnits = false,
                displayFieldWithTextField = false,
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
            )
        }
    }

    private fun dailyDoseDisplayTextField() {

        dailyDoseViewState.update {
            it.copy(
                targetHeightOfTheSurface = DailyDoseHeightOptions.displayTextField,

                displayFieldWithInsertedDose = false,
                displayFieldWithMeasuringUnits = true,
                displayFieldWithTextField = true
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
            )
        }
    }

    private fun dailyDoseChangeHint(targetHint: String) {

        dailyDoseViewState.update {

            it.copy(
                hint = targetHint,
            )
        }
    }


//  'ScheduleDays' operators
    private fun scheduleDaysDisplayCollapsed() {

        scheduleDaysViewState.update {

            it.copy(
                targetHeightOfTheSurface = ScheduleDaysHeightOptions.displayDeactivated,

                displayScheduledDaysOfWeek = false,
                displayChips = false,
            )
        }
    }

    private fun scheduleDaysDisplayDaysToSchedule() {

        scheduleDaysViewState.update {

            it.copy(
                targetHeightOfTheSurface = ScheduleDaysHeightOptions.displayRadioButtons,

                displayScheduledDaysOfWeek = false,
                displayChips = true,
            )
        }
    }

    private fun scheduleDaysDisplayScheduledDays() {

        scheduleDaysViewState.update {

            it.copy(
                targetHeightOfTheSurface = ScheduleDaysHeightOptions.displaySchedule,

                displayScheduledDaysOfWeek = true,
                displayChips = false,
            )
        }
    }

    private fun scheduleDaysChangeHint(targetHint: String) {

        scheduleDaysViewState.update {

            it.copy(
                hint = targetHint,
            )
        }
    }

//  'TimeOfIngestion' operators
    private fun scheduleTimeDisplayUnactivated() {

        timeOfIngestionViewState.update {
            it.copy(
                targetHeightOfTheSurface = TimeOfIngestionHeightOptions.displayUnactivatedComponent,
                hint = TimeOfIngestionHintOptions.clickToInsertTimeOfIngestion,
                displayContentInTimeComponent = false
            )
        }
    }

    private fun scheduleTimeDisplayActivatedTimeComponent() {

        timeOfIngestionViewState.update {
            it.copy(
                targetHeightOfTheSurface = TimeOfIngestionHeightOptions.displayActivatedTimeComponent,
                hint = TimeOfIngestionHintOptions.clickToConfirmIngestionTime,
                displayContentInTimeComponent = true
            )
        }
    }

    private fun scheduleTimeDisplayUnactivatedTimeComponent() {

        timeOfIngestionViewState.update {
            it.copy(
                targetHeightOfTheSurface = TimeOfIngestionHeightOptions.displayUnactivatedTimeComponent,
                hint = TimeOfIngestionHintOptions.clickToEditOrDeleteTimeOfIngestion,
                displayContentInTimeComponent = false
            )
        }
    }
}