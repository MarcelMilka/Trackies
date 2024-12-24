package userInterface.detailedTrackie

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.globalConstants.DaysOfWeek
import com.example.globalConstants.MeasuringUnit
import com.example.trackies.isSignedIn.detailedTrackie.ui.detailedTrackie
import com.example.trackies.isSignedIn.user.buisness.LicenseModel
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelViewState
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.Int
import kotlin.String
import kotlin.collections.Map

class TestOfDetailedTrackie {

    @get:Rule()
    val composeTestRule = createComposeRule()

    private var onReturnWasClicked = false
    private var onDeleteWasClicked: TrackieModel? = null

    @Before
    fun setup() {

        onReturnWasClicked = false
        onDeleteWasClicked = null

        composeTestRule.setContent {

            val sharedViewModelViewState by SVMVS.sharedViewModelViewState.collectAsState()
            val trackieModel by TM.trackieModel.collectAsState()

            detailedTrackie(
                sharedViewModelViewState = sharedViewModelViewState,
                trackieModel = trackieModel,
                onReturn = {

                    onReturnWasClicked = true
                },
                onDelete = {

                    onDeleteWasClicked = it
                }
            )
        }
    }

    @Test
    fun loadingUI_TrackieModelIsFetched_LoadedSuccessfullyUI() = runBlocking {

//      set to LoadedSuccessfully, because it's possible to enter the screen only when data is loaded
        SVMVS.setLoadedSuccessfully()

//      when trackie is being fetched:
        iconButtonToNavigateBetweenActivities.assertIsDisplayed()
        iconButtonToDeleteTrackie.assertIsNotDisplayed()

        loadingUpperPartOfDetailedTrackie.assertIsDisplayed()
        detailedTrackieUpperPart.assertIsNotDisplayed()

        loadingRegularityChart.assertIsDisplayed()
        detailedTrackieRegularityChart.assertIsNotDisplayed()


        TM.setTrackieModel()
        composeTestRule.awaitIdle()

//      when is fetched:
        iconButtonToNavigateBetweenActivities.assertIsDisplayed()
        iconButtonToDeleteTrackie.assertIsDisplayed()

        loadingUpperPartOfDetailedTrackie.assertIsNotDisplayed()
        detailedTrackieUpperPart.assertIsDisplayed()

        loadingRegularityChart.assertIsNotDisplayed()
        detailedTrackieRegularityChart.assertIsDisplayed()

        assertNull(null)
    }

    @Test
    fun buttonReturn_callsOnReturn_trackieModelIsNull() = runBlocking {

        iconButtonToNavigateBetweenActivities.performClick()

        composeTestRule.awaitIdle()

        assertTrue(onReturnWasClicked)
    }

    @Test
    fun buttonReturn_callsOnReturn_trackieModelIsNotNull() = runBlocking {

        SVMVS.setLoadedSuccessfully()
        TM.setTrackieModel()

        composeTestRule.awaitIdle()

        iconButtonToNavigateBetweenActivities.performClick()

        composeTestRule.awaitIdle()

        assertTrue(onReturnWasClicked)
    }

    @Test
    fun buttonDelete_callsOnDelete_onlyWhenLoadedSuccessfully() = runBlocking {

        iconButtonToDeleteTrackie.assertIsNotDisplayed()

        composeTestRule.awaitIdle()

        SVMVS.setLoadedSuccessfully()
        TM.setTrackieModel()

        composeTestRule.awaitIdle()

        iconButtonToDeleteTrackie.performClick()

        composeTestRule.awaitIdle()

        assertEquals(
            TrackieModel(
                name = "A",
                totalDose = 1,
                measuringUnit = MeasuringUnit.ml,
                repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday),
                ingestionTime = null
            ),
            onDeleteWasClicked
        )
    }

    private val iconButtonToNavigateBetweenActivities = composeTestRule.onNodeWithTag("iconButtonToNavigateBetweenActivities")
    private val iconButtonToDeleteTrackie = composeTestRule.onNodeWithTag("iconButton")

    private val loadingUpperPartOfDetailedTrackie = composeTestRule.onNodeWithTag("loadingUpperPartOfDetailedTrackie")
    private val detailedTrackieUpperPart = composeTestRule.onNodeWithTag("detailedTrackieUpperPart")

    private val loadingRegularityChart = composeTestRule.onNodeWithTag("loadingRegularityChart")
    private val detailedTrackieRegularityChart = composeTestRule.onNodeWithTag("detailedTrackieRegularityChart")
}

private object SVMVS {

    var sharedViewModelViewState = MutableStateFlow<SharedViewModelViewState>(SharedViewModelViewState.Loading)

    fun setLoadedSuccessfully() {

        sharedViewModelViewState.update {

            SharedViewModelViewState.LoadedSuccessfully(

                license = LicenseModel(
                    active = true,
                    validUntil = "10.06.2025",
                    totalAmountOfTrackies = 3
                ),

                trackiesForToday = listOf<TrackieModel>(),

                statesOfTrackiesForToday = mapOf<String, Boolean>(),

                weeklyRegularity = mapOf<String, Map<Int, Int>>(),

                namesOfAllTrackies = listOf(),

                allTrackies = null
            )
        }
    }
}

private object TM {

    var trackieModel = MutableStateFlow<TrackieModel?>(null)

    fun setTrackieModel() {

        trackieModel.update {

            TrackieModel(
                name = "A",
                totalDose = 1,
                measuringUnit = MeasuringUnit.ml,
                repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday),
                ingestionTime = null
            )
        }
    }
}