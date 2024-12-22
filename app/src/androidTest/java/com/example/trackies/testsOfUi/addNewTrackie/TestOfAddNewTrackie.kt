package com.example.trackies.testsOfUi.addNewTrackie

import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.nameOfTrackie.staticValues.NameOfTrackieHeightOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.nameOfTrackie.staticValues.NameOfTrackieHintOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.dailyDose.staticValues.DailyDoseHeightOptions
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.dailyDose.staticValues.DailyDoseHintOptions
import com.example.trackies.isSignedIn.addNewTrackie.buisness.AddNewTrackieSegments
import com.example.trackies.isSignedIn.addNewTrackie.ui.mainScreen.addNewTrackie
import com.example.trackies.isSignedIn.addNewTrackie.vm.AddNewTrackieViewModel
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelViewState
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.isSignedIn.user.buisness.LicenseModel
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.ExperimentalTestApi
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.performTextInput
import com.example.globalConstants.MeasuringUnit
import androidx.compose.ui.test.performKeyInput
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import com.example.globalConstants.DaysOfWeek
import androidx.compose.ui.test.performClick
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.ui.input.key.Key
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.pressKey
import userInterface.homeScreen.Models
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.update
import androidx.compose.ui.unit.dp
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlin.collections.Map
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.String
import kotlin.Int

@HiltAndroidTest
class TestOfAddNewTrackie {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private lateinit var addNewTrackieViewModel: AddNewTrackieViewModel

    var onReturnWasClicked = false
    var onUpdateTimeOfIngestionWasClicked = false
    var onDeleteTimeOfIngestionWasClicked = false
    var onAddNewTrackieWasClicked = false
    var onDisplayTrackiesPremium = false

    var wasClicked = false

    @Before
    fun beforeTest() {

        hiltRule.inject()

        addNewTrackieViewModel = AddNewTrackieViewModel()
        TestHelpingObject.setLoadedSuccessfully_ThreeTrackies()

        composeTestRule.setContent {

            val sharedViewModelViewState by TestHelpingObject.sharedViewModelViewState.collectAsState()
            onReturnWasClicked = false
            onUpdateTimeOfIngestionWasClicked = false
            onDeleteTimeOfIngestionWasClicked = false
            onAddNewTrackieWasClicked = false
            onDisplayTrackiesPremium = false
            wasClicked = false

            addNewTrackie(

                sharedViewModelUiState = sharedViewModelViewState,
                addNewTrackieViewModel = addNewTrackieViewModel,

                importNamesOfAllTrackies = {

                    addNewTrackieViewModel.importNamesOfAllTrackies(
                        namesOfAllTrackies = it
                    )
                },

                onReturn = {

                    onReturnWasClicked = true
                    addNewTrackieViewModel.clearAll()
                },

                onUpdateName = {

                    addNewTrackieViewModel.updateName(
                        name = it
                    )
                },
                onUpdateMeasuringUnit = {

                    addNewTrackieViewModel.updateMeasuringUnit(
                        measuringUnit = it
                    )
                },
                onUpdateDose = {

                    addNewTrackieViewModel.updateDose(
                        dose = it
                    )
                },
                onUpdateRepeatOn = {

                    addNewTrackieViewModel.updateRepeatOn(
                        repeatOn = it
                    )
                },

                onUpdateTimeOfIngestion = {

                    onUpdateTimeOfIngestionWasClicked = true
                },
                onDeleteTimeOfIngestion = {

                    onDeleteTimeOfIngestionWasClicked = true
                },

                onActivate = { segmentToActivate ->

                    if (
                        segmentToActivate == AddNewTrackieSegments.TimeOfIngestion &&
                        addNewTrackieViewModel.addNewTrackieModel.value.ingestionTime == null
                    ) {

                        addNewTrackieViewModel.activateSegment(
                            segmentToActivate = segmentToActivate
                        )
                    }

                    else {

                        addNewTrackieViewModel.activateSegment(
                            segmentToActivate = segmentToActivate
                        )
                    }
                },
                onDeactivate = { segmentToDeactivate ->

                    wasClicked = true

                    addNewTrackieViewModel.deactivateSegment(
                        segmentToDeactivate = segmentToDeactivate
                    )
                },

                onClearAll = {

                    addNewTrackieViewModel.clearAll()
                },

                onAdd = {

                    onAddNewTrackieWasClicked = true

                    addNewTrackieViewModel.clearAll()
                },

                onDisplayTrackiesPremiumDialog = {

                    onDisplayTrackiesPremium = true
                }
            )
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun nameOfTrackie_behavesAppropriately() = runBlocking {

//      segment is deactivated by default:
        nameOfTrackie.assertHeightIsEqualTo(NameOfTrackieHeightOptions.displayUnactivatedComponent.dp)

        composeTestRule.onNodeWithText("Name of trackie").assertIsDisplayed()
        composeTestRule.onNodeWithText(NameOfTrackieHintOptions.insertNewName).assertIsDisplayed()

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.name == "")
        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.nameOfTrackieIsActive == false)

        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.targetHeightOfTheSurface == NameOfTrackieHeightOptions.displayUnactivatedComponent)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.displayFieldWithInsertedName == false)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.displayFieldWithTextField == false)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.hint == NameOfTrackieHintOptions.insertNewName)

//      activate segment:
        nameOfTrackie.performClick()

        composeTestRule.awaitIdle()

        composeTestRule.onNodeWithText(NameOfTrackieHintOptions.nameCannotBeEmpty).assertIsDisplayed()
        nameOfTrackieTextField.assertIsDisplayed()

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.name == "")
        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.nameOfTrackieIsActive == true)

        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.targetHeightOfTheSurface == NameOfTrackieHeightOptions.displayTextField)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.displayFieldWithInsertedName == false)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.displayFieldWithTextField == true)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.hint == NameOfTrackieHintOptions.nameCannotBeEmpty)

//      deactivate segment:
        nameOfTrackie.performTouchInput {

            this.down(topCenter)
            this.up()
        }

        composeTestRule.awaitIdle()
        composeTestRule.onNodeWithText(NameOfTrackieHintOptions.insertNewName).assertIsDisplayed()

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.name == "")
        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.nameOfTrackieIsActive == false)

        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.targetHeightOfTheSurface == NameOfTrackieHeightOptions.displayUnactivatedComponent)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.displayFieldWithInsertedName == false)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.displayFieldWithTextField == false)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.hint == NameOfTrackieHintOptions.insertNewName)

//      activate segment, insert one letter
        nameOfTrackie.performClick()

        composeTestRule.awaitIdle()
        composeTestRule.onNodeWithText(NameOfTrackieHintOptions.nameCannotBeEmpty).assertIsDisplayed()
        nameOfTrackieTextField.performTextInput("W")

        composeTestRule.awaitIdle()
        composeTestRule.onNodeWithText(NameOfTrackieHintOptions.confirmNewName).assertIsDisplayed()

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.name == "W")
        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.nameOfTrackieIsActive == true)

        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.targetHeightOfTheSurface == NameOfTrackieHeightOptions.displayTextField)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.displayFieldWithInsertedName == false)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.displayFieldWithTextField == true)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.hint == NameOfTrackieHintOptions.confirmNewName)

//      deactivate segment
        nameOfTrackie.performTouchInput {

            this.down(topCenter)
            this.up()
        }

        composeTestRule.awaitIdle()
        nameOfTrackie.assertHeightIsEqualTo(NameOfTrackieHeightOptions.displayInsertedName.dp)

        composeTestRule.onNodeWithText(NameOfTrackieHintOptions.editNewName).assertIsDisplayed()
        composeTestRule.onNodeWithText("W").assertIsDisplayed()

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.name == "W")
        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.nameOfTrackieIsActive == false)

        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.targetHeightOfTheSurface == NameOfTrackieHeightOptions.displayInsertedName)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.displayFieldWithInsertedName == true)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.displayFieldWithTextField == false)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.hint == NameOfTrackieHintOptions.editNewName)

//      activate segment, delete 'W',
        nameOfTrackie.performClick()

        composeTestRule.awaitIdle()
        nameOfTrackie.assertHeightIsEqualTo(NameOfTrackieHeightOptions.displayTextField.dp)
        composeTestRule.onNodeWithText(NameOfTrackieHintOptions.confirmNewName).assertIsDisplayed()
        composeTestRule.onNodeWithText("W").assertIsDisplayed()

        nameOfTrackieTextField.performKeyInput {

            pressKey(Key.Backspace)
        }
        composeTestRule.awaitIdle()

        composeTestRule.onNodeWithText(NameOfTrackieHintOptions.nameCannotBeEmpty).assertIsDisplayed()

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.name == "")
        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.nameOfTrackieIsActive == true)

        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.targetHeightOfTheSurface == NameOfTrackieHeightOptions.displayTextField)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.displayFieldWithInsertedName == false)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.displayFieldWithTextField == true)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.hint == NameOfTrackieHintOptions.nameCannotBeEmpty)

//      insert already existing name
        nameOfTrackieTextField.performTextInput("A")

        composeTestRule.awaitIdle()

        composeTestRule.onNodeWithText("A").assertIsDisplayed()

        composeTestRule.onNodeWithText(NameOfTrackieHintOptions.nameAlreadyExists).assertIsDisplayed()

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.name == "A")
        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.nameOfTrackieIsActive == true)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.hint == NameOfTrackieHintOptions.nameAlreadyExists)

//      deactivate component
        nameOfTrackie.performTouchInput {

            this.down(topCenter)
            this.up()
        }

        composeTestRule.awaitIdle()

        nameOfTrackie.assertHeightIsEqualTo(NameOfTrackieHeightOptions.displayInsertedName.dp)
        composeTestRule.onNodeWithText("A").assertIsDisplayed()
        composeTestRule.onNodeWithText(NameOfTrackieHintOptions.nameAlreadyExists).assertIsDisplayed()

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.name == "A")
        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.nameOfTrackieIsActive == false)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.hint == NameOfTrackieHintOptions.nameAlreadyExists)

//      activate segment, insert unique name
        nameOfTrackie.performClick()

        composeTestRule.awaitIdle()

        nameOfTrackieTextField.performTextInput("aa")

        composeTestRule.awaitIdle()

        nameOfTrackie.assertHeightIsEqualTo(NameOfTrackieHeightOptions.displayTextField.dp)
        composeTestRule.onNodeWithText("Aaa").assertIsDisplayed()
        composeTestRule.onNodeWithText(NameOfTrackieHintOptions.confirmNewName).assertIsDisplayed()

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.name == "Aaa")
        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.nameOfTrackieIsActive == true)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.hint == NameOfTrackieHintOptions.confirmNewName)

//      deactivate segment:
        nameOfTrackie.performTouchInput {

            this.down(topCenter)
            this.up()
        }

        composeTestRule.awaitIdle()

        nameOfTrackie.assertHeightIsEqualTo(NameOfTrackieHeightOptions.displayInsertedName.dp)

//      activate segment:
        nameOfTrackie.performClick()

//      deactivate segment via activating other segment:
        dailyDose.performClick()

        composeTestRule.awaitIdle()

        dailyDose.assertHeightIsEqualTo(DailyDoseHeightOptions.displayFieldWithAvailableMeasuringUnitsToChoose.dp)

        nameOfTrackie.assertHeightIsEqualTo(NameOfTrackieHeightOptions.displayInsertedName.dp)
        composeTestRule.onNodeWithText("Aaa").assertIsDisplayed()
        composeTestRule.onNodeWithText(NameOfTrackieHintOptions.editNewName).assertIsDisplayed()

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.name == "Aaa")
        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.nameOfTrackieIsActive == false)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.hint == NameOfTrackieHintOptions.editNewName)

//      activate segment, delete inserted name, insert another name, deactivate segment via clicking the button 'clear all'
        nameOfTrackie.performClick()

        nameOfTrackieTextField.performKeyInput {

            pressKey(Key.Backspace)
            pressKey(Key.Backspace)
            pressKey(Key.Backspace)
        }

        composeTestRule.onNodeWithText(NameOfTrackieHintOptions.nameCannotBeEmpty).assertIsDisplayed()

        nameOfTrackieTextField.performTextInput("I like testing")

        composeTestRule.onNodeWithText("I like testing")

        composeTestRule.onNodeWithText("clear all").performClick()

        composeTestRule.awaitIdle()

        composeTestRule.onNodeWithText(NameOfTrackieHintOptions.insertNewName).assertIsDisplayed()

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.name == "")
        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.nameOfTrackieIsActive == false)

        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.targetHeightOfTheSurface == NameOfTrackieHeightOptions.displayUnactivatedComponent)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.displayFieldWithInsertedName == false)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.displayFieldWithTextField == false)
        assertTrue(addNewTrackieViewModel.nameOfTrackieViewState.value.hint == NameOfTrackieHintOptions.insertNewName)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun dailyDose_behavesAppropriately() = runBlocking {

//      segment is deactivated by default
        composeTestRule.onNodeWithText(DailyDoseHintOptions.insertDailyDose).assertIsDisplayed()

        dailyDose.assertHeightIsEqualTo(DailyDoseHeightOptions.displayUnactivatedSegment.dp)

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.dose == 0)
        assertNull(addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit)

        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.dailyDoseIsActive == false)

        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.targetHeightOfTheSurface == DailyDoseHeightOptions.displayUnactivatedSegment)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithInsertedDose == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithMeasuringUnits == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithTextField == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.hint == DailyDoseHintOptions.insertDailyDose)

//      activate segment, display field with radio buttons
        dailyDose.performClick()

        composeTestRule.awaitIdle()

        dailyDose.assertHeightIsEqualTo(DailyDoseHeightOptions.displayFieldWithAvailableMeasuringUnitsToChoose.dp)

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.dose == 0)
        assertNull(addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit)

        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.dailyDoseIsActive == true)

        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.targetHeightOfTheSurface == DailyDoseHeightOptions.displayFieldWithAvailableMeasuringUnitsToChoose)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithInsertedDose == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithMeasuringUnits == true)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithTextField == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.hint == DailyDoseHintOptions.chooseMeasuringUnitAndInsertDose)

//      click ‘ml’
        dailyDoseMl.performClick()

        composeTestRule.awaitIdle()

        dailyDose.assertHeightIsEqualTo(DailyDoseHeightOptions.displayTextField.dp)

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.dose == 0)
        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit == MeasuringUnit.ml)

        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.dailyDoseIsActive == true)

        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.targetHeightOfTheSurface == DailyDoseHeightOptions.displayTextField)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithInsertedDose == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithMeasuringUnits == true)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithTextField == true)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.hint == DailyDoseHintOptions.dailyDoseCannotBeZero)

//      click ‘g’
        dailyDoseG.performClick()

        composeTestRule.awaitIdle()

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit == MeasuringUnit.g)

//      deactivate segment via activating another segment
        nameOfTrackie.performClick()

        composeTestRule.awaitIdle()

        dailyDose.assertHeightIsEqualTo(DailyDoseHeightOptions.displayFieldWithInsertedDose.dp)

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.dose == 0)
        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit == MeasuringUnit.g)

        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.dailyDoseIsActive == false)

        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.targetHeightOfTheSurface == DailyDoseHeightOptions.displayFieldWithInsertedDose)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithInsertedDose == true)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithMeasuringUnits == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithTextField == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.hint == DailyDoseHintOptions.dailyDoseCannotBeZero)

//      activate segment, press ‘pcs’, enter dose
        dailyDose.performClick()

        composeTestRule.awaitIdle()

        dailyDosePcs.performClick()

        composeTestRule.awaitIdle()

        dailyDoseTextField.performTextInput("1")

        composeTestRule.awaitIdle()

        dailyDose.assertHeightIsEqualTo(DailyDoseHeightOptions.displayTextField.dp)

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.dose == 1)
        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit == MeasuringUnit.pcs)

        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.dailyDoseIsActive == true)

        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.targetHeightOfTheSurface == DailyDoseHeightOptions.displayTextField)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithInsertedDose == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithMeasuringUnits == true)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithTextField == true)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.hint == DailyDoseHintOptions.confirmDailyDosage)

//      press 'g'
        dailyDoseG.performClick()

        composeTestRule.awaitIdle()

        dailyDose.assertHeightIsEqualTo(DailyDoseHeightOptions.displayTextField.dp)

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.dose == 1)
        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit == MeasuringUnit.g)

//      press 'ml'
        dailyDoseMl.performClick()

        composeTestRule.awaitIdle()

        dailyDose.assertHeightIsEqualTo(DailyDoseHeightOptions.displayTextField.dp)

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.dose == 1)
        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit == MeasuringUnit.ml)

//      deactivate segment
        dailyDose.performClick()

        composeTestRule.awaitIdle()

        dailyDose.assertHeightIsEqualTo(DailyDoseHeightOptions.displayFieldWithInsertedDose.dp)
        composeTestRule.onNodeWithText("1 ml")

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.dose == 1)
        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit == MeasuringUnit.ml)

        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.dailyDoseIsActive == false)

        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.targetHeightOfTheSurface == DailyDoseHeightOptions.displayFieldWithInsertedDose)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithInsertedDose == true)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithMeasuringUnits == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithTextField == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.hint == DailyDoseHintOptions.editDailyDosage)

//      activate segment, remove completely inserted dose
        dailyDose.performClick()

        composeTestRule.awaitIdle()

        dailyDoseTextField.performKeyInput {

            pressKey(Key.Backspace)
        }

        composeTestRule.awaitIdle()

        dailyDose.assertHeightIsEqualTo(DailyDoseHeightOptions.displayTextField.dp)

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.dose == 0)
        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit == MeasuringUnit.ml)

        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.dailyDoseIsActive == true)

        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.targetHeightOfTheSurface == DailyDoseHeightOptions.displayTextField)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithInsertedDose == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithMeasuringUnits == true)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithTextField == true)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.hint == DailyDoseHintOptions.dailyDoseCannotBeZero)

//      deactivate segment
        dailyDose.performClick()

        composeTestRule.awaitIdle()

        dailyDose.assertHeightIsEqualTo(DailyDoseHeightOptions.displayFieldWithInsertedDose.dp)

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.dose == 0)
        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit == MeasuringUnit.ml)

        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.dailyDoseIsActive == false)

        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.targetHeightOfTheSurface == DailyDoseHeightOptions.displayFieldWithInsertedDose)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithInsertedDose == true)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithMeasuringUnits == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithTextField == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.hint == DailyDoseHintOptions.dailyDoseCannotBeZero)

//      activate segment, enter new dose
        dailyDose.performClick()

        composeTestRule.awaitIdle()

        dailyDoseTextField.performTextInput("250")

        composeTestRule.awaitIdle()

        dailyDose.assertHeightIsEqualTo(DailyDoseHeightOptions.displayTextField.dp)

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.dose == 250)
        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit == MeasuringUnit.ml)

        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.dailyDoseIsActive == true)

        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.targetHeightOfTheSurface == DailyDoseHeightOptions.displayTextField)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithInsertedDose == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithMeasuringUnits == true)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithTextField == true)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.hint == DailyDoseHintOptions.confirmDailyDosage)

//      deactivate segment, enter new dose
        dailyDose.performClick()

        composeTestRule.awaitIdle()

        dailyDose.assertHeightIsEqualTo(DailyDoseHeightOptions.displayFieldWithInsertedDose.dp)

        composeTestRule.onNodeWithText("250 ml")

//      activate component, add one digit more
        dailyDose.performClick()

        composeTestRule.awaitIdle()

        dailyDoseTextField.performTextInput("0")

        composeTestRule.awaitIdle()

        dailyDose.assertHeightIsEqualTo(DailyDoseHeightOptions.displayTextField.dp)

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.dose == 2500)
        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit == MeasuringUnit.ml)

        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.dailyDoseIsActive == true)

        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.targetHeightOfTheSurface == DailyDoseHeightOptions.displayTextField)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithInsertedDose == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithMeasuringUnits == true)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithTextField == true)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.hint == DailyDoseHintOptions.confirmDailyDosage)

//      deactivate component via clicking the button ‘clear all’
        composeTestRule.onNodeWithText("clear all").performClick()

        composeTestRule.awaitIdle()

        dailyDose.assertHeightIsEqualTo(DailyDoseHeightOptions.displayUnactivatedSegment.dp)

        assertTrue(addNewTrackieViewModel.addNewTrackieModel.value.dose == 0)
        assertNull(addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit)

        assertTrue(addNewTrackieViewModel.activityStatesOfSegments.value.dailyDoseIsActive == false)

        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.targetHeightOfTheSurface == DailyDoseHeightOptions.displayUnactivatedSegment)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithInsertedDose == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithMeasuringUnits == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.displayFieldWithTextField == false)
        assertTrue(addNewTrackieViewModel.dailyDoseViewState.value.hint == DailyDoseHintOptions.insertDailyDose)
    }

    private val nameOfTrackie = composeTestRule.onNodeWithTag("nameOfTrackie")
    private val nameOfTrackieTextField = composeTestRule.onNodeWithTag("nameOfTrackie - textField")

    private val dailyDose = composeTestRule.onNodeWithTag("dailyDose")
    private val dailyDoseMl = composeTestRule.onNodeWithText("ml")
    private val dailyDoseG = composeTestRule.onNodeWithText("g")
    private val dailyDosePcs = composeTestRule.onNodeWithText("pcs")
    private val dailyDoseTextField = composeTestRule.onNodeWithTag("dailyDose - text field")
}

private object TestHelpingObject {

    private var _sharedViewModelViewState = MutableStateFlow<SharedViewModelViewState>(
        value = SharedViewModelViewState.Loading
    )
    val sharedViewModelViewState = _sharedViewModelViewState.asStateFlow()

    fun setLoadedSuccessfully_ThreeTrackies() {

        _sharedViewModelViewState.update {

            SharedViewModelViewState.LoadedSuccessfully(

                license = LicenseModel(
                    active = true,
                    validUntil = "10.06.2025",
                    totalAmountOfTrackies = 3
                ),

                trackiesForToday = listOf<TrackieModel>(
                    Models.wholeWeekTrackieModel1,
                    Models.wholeWeekTrackieModel2,
                    Models.wholeWeekTrackieModel3
                ),

                statesOfTrackiesForToday = mapOf<String, Boolean>(
                    "A" to false,
                    "B" to true,
                    "C" to false
                ),

                weeklyRegularity = mapOf<String, Map<Int, Int>>(
                    DaysOfWeek.monday to mapOf(3 to 1),
                    DaysOfWeek.tuesday to mapOf(3 to 0),
                    DaysOfWeek.wednesday to mapOf(3 to 0),
                    DaysOfWeek.thursday to mapOf(3 to 0),
                    DaysOfWeek.friday to mapOf(3 to 0),
                    DaysOfWeek.saturday to mapOf(3 to 0),
                    DaysOfWeek.sunday to mapOf(3 to 0),
                ),

                namesOfAllTrackies = listOf("A", "B", "C"),

                allTrackies = null
            )
        }
    }
}