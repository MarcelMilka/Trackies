package testsOfViewModels

import MainCoroutineRule
import com.example.globalConstants.DaysOfWeek
import com.example.globalConstants.MeasuringUnit
import com.example.trackies.isSignedIn.addNewTrackie.buisness.ActivityStatesOfSegments
import com.example.trackies.isSignedIn.addNewTrackie.buisness.AddNewTrackieModel
import com.example.trackies.isSignedIn.addNewTrackie.buisness.AddNewTrackieSegments
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.dailyDose.buisness.DailyDoseViewState
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.nameOfTrackie.buisness.NameOfTrackieViewState
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.scheduleDays.buisness.ScheduleDaysViewState
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.buisness.TimeOfIngestion
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.buisness.TimeOfIngestionEntity
import com.example.trackies.isSignedIn.addNewTrackie.ui.segments.timeOfIngestion.buisness.TimeOfIngestionViewState
import com.example.trackies.isSignedIn.addNewTrackie.vm.AddNewTrackieViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddNewTrackieViewModelUnitTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    lateinit var addNewTrackieViewModel: AddNewTrackieViewModel

    @Before
    fun beforeTest() {

        addNewTrackieViewModel = AddNewTrackieViewModel()
    }

    @Test
    fun `importNamesOfAllTrackies() - takes empty list, namesOfAllExistingTrackies is empty`() = runBlocking {

//      preparations:
        addNewTrackieViewModel.importNamesOfAllTrackies(
            namesOfAllTrackies = listOf<String>()
        )

//      verifications:
        val namesOfAllTrackies = addNewTrackieViewModel.namesOfAllExistingTrackies.value
        assertEquals(namesOfAllTrackies, listOf<String>())
    }

    @Test
    fun `importNamesOfAllTrackies() - takes list of strings, namesOfAllExistingTrackies is not empty`() = runBlocking {

//      preparations:
        addNewTrackieViewModel.importNamesOfAllTrackies(
            namesOfAllTrackies = listOf<String>("A", "B", "C")
        )

//      verifications:
        val namesOfAllTrackies = addNewTrackieViewModel.namesOfAllExistingTrackies.value
        assertEquals(namesOfAllTrackies, listOf<String>("A", "B", "C"))
    }

    @Test
    fun `activateSegment() activates the correct segment and deactivates others`() = runBlocking {

        // Making sure every segment is set to false by default:
        val expected1 = ActivityStatesOfSegments()
        val actual1 = addNewTrackieViewModel.activityStatesOfSegments.value
        assertEquals(expected1, actual1)

        // Activating NameOfTrackie and making sure only NameOfTrackie is true:
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.NameOfTrackie)

        val expected2 = TestHelpingObject.nameOfTrackieIsActive
        val actual2 = addNewTrackieViewModel.activityStatesOfSegments.value
        assertEquals(expected2, actual2)

        // Activating DailyDose and making sure only DailyDose is true:
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.DailyDose)

        val expected3 = TestHelpingObject.dailyDoseIsActive
        val actual3 = addNewTrackieViewModel.activityStatesOfSegments.value
        assertEquals(expected3, actual3)

        // Activating ScheduleDays and making sure only ScheduleDays is true:
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.ScheduleDays)

        val expected4 = TestHelpingObject.scheduleDaysIsActive
        val actual4 = addNewTrackieViewModel.activityStatesOfSegments.value
        assertEquals(expected4, actual4)

        // Activating TimeOfIngestion and making sure only TimeOfIngestion is true:
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.TimeOfIngestion)

        val expected5 = TestHelpingObject.timeOfIngestionIsActive
        val actual5 = addNewTrackieViewModel.activityStatesOfSegments.value
        assertEquals(expected5, actual5)
    }

    @Test
    fun `deactivateSegment() deactivates the correct segment`() = runBlocking {

        // Making sure every segment is set to false by default:
        val expected1 = ActivityStatesOfSegments()
        val actual1 = addNewTrackieViewModel.activityStatesOfSegments.value
        assertEquals(expected1, actual1)

        // Activating and then deactivating NameOfTrackie, ensuring it returns to default state:
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.NameOfTrackie)
        addNewTrackieViewModel.deactivateSegment(AddNewTrackieSegments.NameOfTrackie)

        val expected2 = ActivityStatesOfSegments()
        val actual2 = addNewTrackieViewModel.activityStatesOfSegments.value
        assertEquals(expected2, actual2)

        // Activating DailyDose and then deactivating it, ensuring it returns to default state:
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.DailyDose)
        addNewTrackieViewModel.deactivateSegment(AddNewTrackieSegments.DailyDose)

        val expected3 = ActivityStatesOfSegments()
        val actual3 = addNewTrackieViewModel.activityStatesOfSegments.value
        assertEquals(expected3, actual3)

        // Activating ScheduleDays and then deactivating it, ensuring it returns to default state:
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.ScheduleDays)
        addNewTrackieViewModel.deactivateSegment(AddNewTrackieSegments.ScheduleDays)

        val expected4 = ActivityStatesOfSegments()
        val actual4 = addNewTrackieViewModel.activityStatesOfSegments.value
        assertEquals(expected4, actual4)

        // Activating TimeOfIngestion and then deactivating it, ensuring it returns to default state:
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.TimeOfIngestion)
        addNewTrackieViewModel.deactivateSegment(AddNewTrackieSegments.TimeOfIngestion)

        val expected5 = ActivityStatesOfSegments()
        val actual5 = addNewTrackieViewModel.activityStatesOfSegments.value
        assertEquals(expected5, actual5)
    }

    @Test
    fun `activateSegment() and deactivateSegment() interact together without interruptions`() = runBlocking {

        // Making sure every segment is set to false by default:
        val expected1 = ActivityStatesOfSegments()
        val actual1 = addNewTrackieViewModel.activityStatesOfSegments.value
        assertEquals(expected1, actual1)

        // Activating NameOfTrackie and making sure only NameOfTrackie is true:
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.NameOfTrackie)

        val expected2 = TestHelpingObject.nameOfTrackieIsActive
        val actual2 = addNewTrackieViewModel.activityStatesOfSegments.value
        assertEquals(expected2, actual2)

        // Deactivating NameOfTrackie, ensuring it returns to default state:
        addNewTrackieViewModel.deactivateSegment(AddNewTrackieSegments.NameOfTrackie)

        val expected3 = ActivityStatesOfSegments()
        val actual3 = addNewTrackieViewModel.activityStatesOfSegments.value
        assertEquals(expected3, actual3)

        // Activating DailyDose, ensuring only DailyDose is true:
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.DailyDose)

        val expected4 = TestHelpingObject.dailyDoseIsActive
        val actual4 = addNewTrackieViewModel.activityStatesOfSegments.value
        assertEquals(expected4, actual4)

        // Activating ScheduleDays while DailyDose is active, ensuring only ScheduleDays becomes active:
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.ScheduleDays)

        val expected5 = TestHelpingObject.scheduleDaysIsActive
        val actual5 = addNewTrackieViewModel.activityStatesOfSegments.value
        assertEquals(expected5, actual5)

        // Deactivating ScheduleDays and activating TimeOfIngestion, ensuring only TimeOfIngestion is true:
        addNewTrackieViewModel.deactivateSegment(AddNewTrackieSegments.ScheduleDays)
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.TimeOfIngestion)

        val expected6 = TestHelpingObject.timeOfIngestionIsActive
        val actual6 = addNewTrackieViewModel.activityStatesOfSegments.value
        assertEquals(expected6, actual6)

        // Activating and deactivating multiple segments in sequence:
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.NameOfTrackie)
        addNewTrackieViewModel.deactivateSegment(AddNewTrackieSegments.NameOfTrackie)
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.TimeOfIngestion)
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.DailyDose)
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.ScheduleDays)
        addNewTrackieViewModel.deactivateSegment(AddNewTrackieSegments.ScheduleDays)
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.NameOfTrackie)


        val expected7 = TestHelpingObject.nameOfTrackieIsActive
        val actual7 = addNewTrackieViewModel.activityStatesOfSegments.value
        assertEquals(expected7, actual7)
    }

    @Test
    fun `updateName() - properly updates constructor 'name' in AddNewTrackieModel`() = runBlocking {

//      Making sure 'name' is set to "" by default:
        val expected1 = ""
        val actual1 = addNewTrackieViewModel.addNewTrackieModel.value.name
        assertEquals(expected1, actual1)

//      Inserting various values and making sure changes are made properly
        addNewTrackieViewModel.updateName("Water")
        val expected2 = "Water"
        val actual2 = addNewTrackieViewModel.addNewTrackieModel.value.name
        assertEquals(expected2, actual2)

        addNewTrackieViewModel.updateName("Water ")
        val expected3 = "Water "
        val actual3 = addNewTrackieViewModel.addNewTrackieModel.value.name
        assertEquals(expected3, actual3)

        addNewTrackieViewModel.updateName("Water")
        val expected4 = "Water"
        val actual4 = addNewTrackieViewModel.addNewTrackieModel.value.name
        assertEquals(expected4, actual4)

        addNewTrackieViewModel.updateName("Wate")
        val expected5 = "Wate"
        val actual5 = addNewTrackieViewModel.addNewTrackieModel.value.name
        assertEquals(expected5, actual5)

        addNewTrackieViewModel.updateName("Wat")
        val expected6 = "Wat"
        val actual6 = addNewTrackieViewModel.addNewTrackieModel.value.name
        assertEquals(expected6, actual6)

        addNewTrackieViewModel.updateName("Wa")
        val expected7 = "Wa"
        val actual7 = addNewTrackieViewModel.addNewTrackieModel.value.name
        assertEquals(expected7, actual7)

        addNewTrackieViewModel.updateName("W")
        val expected8 = "W"
        val actual8 = addNewTrackieViewModel.addNewTrackieModel.value.name
        assertEquals(expected8, actual8)

        addNewTrackieViewModel.updateName("")
        val expected9 = ""
        val actual9 = addNewTrackieViewModel.addNewTrackieModel.value.name
        assertEquals(expected9, actual9)

        addNewTrackieViewModel.updateName("C")
        val expected10 = "C"
        val actual10 = addNewTrackieViewModel.addNewTrackieModel.value.name
        assertEquals(expected10, actual10)

        addNewTrackieViewModel.updateName("Cr")
        val expected11 = "Cr"
        val actual11 = addNewTrackieViewModel.addNewTrackieModel.value.name
        assertEquals(expected11, actual11)

        addNewTrackieViewModel.updateName("Cre")
        val expected12 = "Cre"
        val actual12 = addNewTrackieViewModel.addNewTrackieModel.value.name
        assertEquals(expected12, actual12)

        addNewTrackieViewModel.updateName("Crea")
        val expected13 = "Crea"
        val actual13 = addNewTrackieViewModel.addNewTrackieModel.value.name
        assertEquals(expected13, actual13)

        addNewTrackieViewModel.updateName("Creat")
        val expected14 = "Creat"
        val actual14 = addNewTrackieViewModel.addNewTrackieModel.value.name
        assertEquals(expected14, actual14)

        addNewTrackieViewModel.updateName("Creati")
        val expected15 = "Creati"
        val actual15 = addNewTrackieViewModel.addNewTrackieModel.value.name
        assertEquals(expected15, actual15)

        addNewTrackieViewModel.updateName("Creatin")
        val expected16 = "Creatin"
        val actual16 = addNewTrackieViewModel.addNewTrackieModel.value.name
        assertEquals(expected16, actual16)

        addNewTrackieViewModel.updateName("Creatine")
        val expected17 = "Creatine"
        val actual17 = addNewTrackieViewModel.addNewTrackieModel.value.name
        assertEquals(expected17, actual17)
    }

    @Test
    fun `updateDose() - properly updates constructor 'dose' in AddNewTrackieModel`() = runBlocking {

//      Making sure 'name' is set to "" by default:
        val expected1 = 0
        val actual1 = addNewTrackieViewModel.addNewTrackieModel.value.dose
        assertEquals(expected1, actual1)

//      Inserting various values and making sure changes are made properly
        addNewTrackieViewModel.updateDose(10)
        val expected2 = 10
        val actual2 = addNewTrackieViewModel.addNewTrackieModel.value.dose
        assertEquals(expected2, actual2)

        addNewTrackieViewModel.updateDose(1)
        val expected3 = 1
        val actual3 = addNewTrackieViewModel.addNewTrackieModel.value.dose
        assertEquals(expected3, actual3)

        addNewTrackieViewModel.updateDose(15)
        val expected4 = 15
        val actual4 = addNewTrackieViewModel.addNewTrackieModel.value.dose
        assertEquals(expected4, actual4)

        addNewTrackieViewModel.updateDose(150)
        val expected5 = 150
        val actual5 = addNewTrackieViewModel.addNewTrackieModel.value.dose
        assertEquals(expected5, actual5)

        addNewTrackieViewModel.updateDose(15)
        val expected6 = 15
        val actual6 = addNewTrackieViewModel.addNewTrackieModel.value.dose
        assertEquals(expected6, actual6)

        addNewTrackieViewModel.updateDose(1)
        val expected7 = 1
        val actual7 = addNewTrackieViewModel.addNewTrackieModel.value.dose
        assertEquals(expected7, actual7)

        addNewTrackieViewModel.updateDose(0)
        val expected8 = 0
        val actual8 = addNewTrackieViewModel.addNewTrackieModel.value.dose
        assertEquals(expected8, actual8)
    }

    @Test
    fun `updateMeasuringUnit() - properly updates constructor 'measuringUnit' in AddNewTrackieModel`() = runBlocking {

//      Making sure 'measuringUnit' is set to null by default:
        val defaultValue = addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit
        assertNull(defaultValue)

//      Inserting various values and making sure changes are made properly
        addNewTrackieViewModel.updateMeasuringUnit(MeasuringUnit.ml)
        val expected1 = MeasuringUnit.ml
        val actual1 = addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit
        assertEquals(expected1, actual1)

        addNewTrackieViewModel.updateMeasuringUnit(MeasuringUnit.g)
        val expected2 = MeasuringUnit.g
        val actual2 = addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit
        assertEquals(expected2, actual2)

        addNewTrackieViewModel.updateMeasuringUnit(MeasuringUnit.pcs)
        val expected3 = MeasuringUnit.pcs
        val actual3 = addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit
        assertEquals(expected3, actual3)

        addNewTrackieViewModel.updateMeasuringUnit(MeasuringUnit.g)
        val expected4 = MeasuringUnit.g
        val actual4 = addNewTrackieViewModel.addNewTrackieModel.value.measuringUnit
        assertEquals(expected4, actual4)
    }

    @Test
    fun `updateRepeatOn() - properly updates constructor 'repeatOn' in AddNewTrackieModel`() = runBlocking {

//      Making sure 'repeatOn' is empty by default:
        val expected1 = setOf<DaysOfWeek>()
        val actual1 = addNewTrackieViewModel.addNewTrackieModel.value.repeatOn
        assertEquals(expected1, actual1)

//      Inserting various values and making sure changes are made properly
        addNewTrackieViewModel.updateRepeatOn(setOf(DaysOfWeek.monday))
        val expected2 = setOf(DaysOfWeek.monday)
        val actual2 = addNewTrackieViewModel.addNewTrackieModel.value.repeatOn
        assertEquals(expected2, actual2)

        addNewTrackieViewModel.updateRepeatOn(setOf(DaysOfWeek.monday, DaysOfWeek.tuesday))
        val expected3 = setOf(DaysOfWeek.monday, DaysOfWeek.tuesday)
        val actual3 = addNewTrackieViewModel.addNewTrackieModel.value.repeatOn
        assertEquals(expected3, actual3)

        addNewTrackieViewModel.updateRepeatOn(setOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday))
        val expected4 = setOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday)
        val actual4 = addNewTrackieViewModel.addNewTrackieModel.value.repeatOn
        assertEquals(expected4, actual4)

        addNewTrackieViewModel.updateRepeatOn(setOf(DaysOfWeek.tuesday, DaysOfWeek.wednesday))
        val expected5 = setOf(DaysOfWeek.tuesday, DaysOfWeek.wednesday)
        val actual5 = addNewTrackieViewModel.addNewTrackieModel.value.repeatOn
        assertEquals(expected5, actual5)

        addNewTrackieViewModel.updateRepeatOn(setOf(DaysOfWeek.wednesday))
        val expected6 = setOf(DaysOfWeek.wednesday)
        val actual6 = addNewTrackieViewModel.addNewTrackieModel.value.repeatOn
        assertEquals(expected6, actual6)

        addNewTrackieViewModel.updateRepeatOn(setOf(DaysOfWeek.monday, DaysOfWeek.wednesday))
        val expected7 = setOf(DaysOfWeek.monday, DaysOfWeek.wednesday)
        val actual7 = addNewTrackieViewModel.addNewTrackieModel.value.repeatOn
        assertEquals(expected7, actual7)

        addNewTrackieViewModel.updateRepeatOn(setOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday))
        val expected8 = setOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday)
        val actual8 = addNewTrackieViewModel.addNewTrackieModel.value.repeatOn
        assertEquals(expected8, actual8)

        addNewTrackieViewModel.updateRepeatOn(setOf<String>())
        val expected9 = setOf<DaysOfWeek>()
        val actual9 = addNewTrackieViewModel.addNewTrackieModel.value.repeatOn
        assertEquals(expected9, actual9)
    }

    @Test
    fun `updateTimeOfIngestion() - properly updates constructor 'ingestionTime' in AddNewTrackieModel`() = runBlocking {

//      Making sure 'ingestionTime' is null by default:
        val defaultValue = addNewTrackieViewModel.addNewTrackieModel.value.timeOfIngestion
        assertNull(defaultValue)

//      Inserting various values and making sure changes are made properly
        addNewTrackieViewModel.updateTimeOfIngestion(TimeOfIngestionEntity(10, 0))
        val expected1 = TimeOfIngestion("10", "00")
        val actual1 = addNewTrackieViewModel.addNewTrackieModel.value.timeOfIngestion
        assertEquals(expected1, actual1)

        addNewTrackieViewModel.updateTimeOfIngestion(null)
        val expected2 = null
        val actual2 = addNewTrackieViewModel.addNewTrackieModel.value.timeOfIngestion
        assertEquals(expected2, actual2)

        addNewTrackieViewModel.updateTimeOfIngestion(TimeOfIngestionEntity(13, 30))
        val expected3 = TimeOfIngestion("13", "30")
        val actual3 = addNewTrackieViewModel.addNewTrackieModel.value.timeOfIngestion
        assertEquals(expected3, actual3)

        addNewTrackieViewModel.updateTimeOfIngestion(TimeOfIngestionEntity(6, 5))
        val expected4 = TimeOfIngestion("06", "05")
        val actual4 = addNewTrackieViewModel.addNewTrackieModel.value.timeOfIngestion
        assertEquals(expected4, actual4)
    }

    @Test
    fun `clearAll() - properly sets view states to default`() = runBlocking {

//      Implementing some random values to the view states:
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.NameOfTrackie)
        addNewTrackieViewModel.updateName("Water")
        addNewTrackieViewModel.updateDose(150)
        addNewTrackieViewModel.updateMeasuringUnit(MeasuringUnit.pcs)
        addNewTrackieViewModel.updateRepeatOn(setOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday))
        addNewTrackieViewModel.updateTimeOfIngestion(TimeOfIngestionEntity(13, 30))

//      Resetting view states:
        addNewTrackieViewModel.clearAll()

//      Making sure every view state is reset to its default values:
        assertEquals(
            AddNewTrackieModel(),
            addNewTrackieViewModel.addNewTrackieModel.value
        )

        assertEquals(
            ActivityStatesOfSegments(),
            addNewTrackieViewModel.activityStatesOfSegments.value
        )

        assertEquals(
            NameOfTrackieViewState(),
            addNewTrackieViewModel.nameOfTrackieViewState.value
        )

        assertEquals(
            DailyDoseViewState(),
            addNewTrackieViewModel.dailyDoseViewState.value
        )

        assertEquals(
            ScheduleDaysViewState(),
            addNewTrackieViewModel.scheduleDaysViewState.value
        )

        assertEquals(
            TimeOfIngestionViewState(),
            addNewTrackieViewModel.timeOfIngestionViewState.value
        )
    }

    @Test
    fun `buttonAddNewTrackieIsEnabled - is properly set to true when all required factors are provided`() = runBlocking {

//      Making sure 'buttonAddNewTrackieIsEnabled' is false by default:
        assertFalse(addNewTrackieViewModel.buttonAddNewTrackieIsEnabled.value)

//      Making sure the variable is true when all requirements are fulfilled:
        addNewTrackieViewModel.updateName("Water")
        addNewTrackieViewModel.updateDose(150)
        addNewTrackieViewModel.updateMeasuringUnit(MeasuringUnit.pcs)
        addNewTrackieViewModel.updateRepeatOn(setOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday))
        addNewTrackieViewModel.updateTimeOfIngestion(TimeOfIngestionEntity(13, 30))

        val expected1 = ActivityStatesOfSegments()
        val actual1 = addNewTrackieViewModel.activityStatesOfSegments.value
        assertEquals(expected1, actual1)

        val expected2 = AddNewTrackieModel(name= "Water", dose= 150, measuringUnit= MeasuringUnit.pcs, repeatOn= setOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday), timeOfIngestion=TimeOfIngestion(hour="13", minute="30"))
        val actual2 = addNewTrackieViewModel.addNewTrackieModel.value
        assertEquals(expected2, actual2)

        assertTrue(addNewTrackieViewModel.buttonAddNewTrackieIsEnabled.value)
    }

    @Test
    fun `buttonAddNewTrackieIsEnabled - is false when name is not provided`() = runBlocking {

//      Setting values for other factors:
        addNewTrackieViewModel.updateDose(150)
        addNewTrackieViewModel.updateMeasuringUnit(MeasuringUnit.pcs)
        addNewTrackieViewModel.updateRepeatOn(setOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday))

//      Making sure buttonAddNewTrackieIsEnabled equals false
        assertFalse(addNewTrackieViewModel.buttonAddNewTrackieIsEnabled.value)
    }

    @Test
    fun `buttonAddNewTrackieIsEnabled - is false when dose is not provided`() = runBlocking {

//      Setting values for other factors:
        addNewTrackieViewModel.updateName("Water")
        addNewTrackieViewModel.updateMeasuringUnit(MeasuringUnit.pcs)
        addNewTrackieViewModel.updateRepeatOn(setOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday))

//      Making sure buttonAddNewTrackieIsEnabled equals false
        assertFalse(addNewTrackieViewModel.buttonAddNewTrackieIsEnabled.value)
    }

    @Test
    fun `buttonAddNewTrackieIsEnabled - is false when measuring unit is not provided`() = runBlocking {

//      Setting values for other factors:
        addNewTrackieViewModel.updateName("Water")
        addNewTrackieViewModel.updateDose(150)
        addNewTrackieViewModel.updateRepeatOn(setOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday))

//      Making sure buttonAddNewTrackieIsEnabled equals false
        assertFalse(addNewTrackieViewModel.buttonAddNewTrackieIsEnabled.value)
    }

    @Test
    fun `buttonAddNewTrackieIsEnabled - is false when repeat on is not provided`() = runBlocking {

//      Setting values for other factors:
        addNewTrackieViewModel.updateName("Water")
        addNewTrackieViewModel.updateDose(150)
        addNewTrackieViewModel.updateMeasuringUnit(MeasuringUnit.pcs)

//      Making sure buttonAddNewTrackieIsEnabled equals false
        assertFalse(addNewTrackieViewModel.buttonAddNewTrackieIsEnabled.value)
    }

    @Test
    fun `buttonAddNewTrackieIsEnabled - is false when a segment is active`() = runBlocking {

//      Setting values for other factors:
        addNewTrackieViewModel.updateDose(150)
        addNewTrackieViewModel.updateMeasuringUnit(MeasuringUnit.pcs)
        addNewTrackieViewModel.updateRepeatOn(setOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday))
        addNewTrackieViewModel.activateSegment(AddNewTrackieSegments.NameOfTrackie)
        addNewTrackieViewModel.updateName("Water")

//      Making sure NameOfTrackie is true:
        assertFalse(addNewTrackieViewModel.activityStatesOfSegments.value == ActivityStatesOfSegments())

//      Making sure buttonAddNewTrackieIsEnabled equals false
        assertFalse(addNewTrackieViewModel.buttonAddNewTrackieIsEnabled.value)
    }


}

private object TestHelpingObject {

    val nameOfTrackieIsActive = ActivityStatesOfSegments(
        nameOfTrackieIsActive = true,
        dailyDoseIsActive = false,
        scheduleDaysIsActive = false,
        timeOfIngestionIsActive = false
    )

    val dailyDoseIsActive = ActivityStatesOfSegments(
        nameOfTrackieIsActive = false,
        dailyDoseIsActive = true,
        scheduleDaysIsActive = false,
        timeOfIngestionIsActive = false
    )

    val scheduleDaysIsActive = ActivityStatesOfSegments(
        nameOfTrackieIsActive = false,
        dailyDoseIsActive = false,
        scheduleDaysIsActive = true,
        timeOfIngestionIsActive = false
    )

    val timeOfIngestionIsActive = ActivityStatesOfSegments(
        nameOfTrackieIsActive = false,
        dailyDoseIsActive = false,
        scheduleDaysIsActive = false,
        timeOfIngestionIsActive = true
    )
}