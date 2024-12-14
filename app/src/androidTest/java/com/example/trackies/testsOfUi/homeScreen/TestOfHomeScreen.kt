package com.example.trackies.testsOfUi.homeScreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.trackies.isSignedIn.homeScreen.buisness.HomeScreenChartToDisplay
import com.example.trackies.isSignedIn.homeScreen.ui.homeScreen
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TestOfHomeScreen {

    lateinit var manipulatorOfHomeScreensViewStates: ManipulatorOfHomeScreen

    @get:Rule(order = 0)
    val composeTestRule = createComposeRule()

    private var onOpenSettingsWasCalled = false
    private var onDisplayAllTrackiesWasCalled = false
    private var onAddNewTrackieWasCalled = false
    private var onDisplayDetailedTrackieWasCalled: TrackieModel? = null
    private var onUpdateHeightOfLazyColumnWasCalled: Int? = null
    private var onSwitchChartWasCalled: HomeScreenChartToDisplay? = null

    @Before
    fun beforeTest() {

        manipulatorOfHomeScreensViewStates = ManipulatorOfHomeScreen()

        onOpenSettingsWasCalled = false
        onDisplayAllTrackiesWasCalled = false
        onAddNewTrackieWasCalled = false
        onDisplayDetailedTrackieWasCalled = null
        onUpdateHeightOfLazyColumnWasCalled = null
        onSwitchChartWasCalled = null

        composeTestRule.setContent {

            val sharedViewModelViewState by manipulatorOfHomeScreensViewStates.sharedViewModelViewState.collectAsState()
            val homeScreenViewState by manipulatorOfHomeScreensViewStates.homeScreenViewState.collectAsState()

            homeScreen(

                sharedViewModelUiState = sharedViewModelViewState,

                homeScreenViewModelUiState = homeScreenViewState,

                onOpenSettings = {

                    onOpenSettingsWasCalled = true
                },

                onDisplayAllTrackies = {

                    onDisplayAllTrackiesWasCalled = true
                },

                onAddNewTrackie = {

                    onAddNewTrackieWasCalled = true
                },

                onDisplayDetailedTrackie = {


                    onDisplayDetailedTrackieWasCalled = it
                },

                onUpdateHeightOfLazyColumn = {

                    onUpdateHeightOfLazyColumnWasCalled = it
                },

                onSwitchChart = {

                    onSwitchChartWasCalled = it

                    manipulatorOfHomeScreensViewStates.setChartToDisplay(it)
                },

                onMarkAsIngested = {}
            )
        }
    }

    @Test
    fun uiStateIs_Loading() = runBlocking {

        manipulatorOfHomeScreensViewStates.setLoading()

        loadingPreviewOfListOfTrackies.assertIsDisplayed()
        previewOfListOfTrackies.assertIsNotDisplayed()

        loadingButtonDisplayAllTrackies.assertIsDisplayed()
        buttonDisplayAllTrackies.assertIsNotDisplayed()

        loadingButtonAddAnotherTrackie.assertIsDisplayed()
        buttonAddNewTrackie.assertIsNotDisplayed()

        loadingRowWithRadioButtons.assertIsDisplayed()
        rowWithRadioButtons.assertIsNotDisplayed()

        loadingRegularityChart.assertIsDisplayed()
        homeScreenWeeklyRegularityChart.assertIsNotDisplayed()
        homeScreenMonthlyRegularityChart.assertIsNotDisplayed()
        yearlyRegularityChartLoadedSuccessfully.assertIsNotDisplayed()

        assertNull(null)
    }

    @Test
    fun uiStateIs_LoadedSuccessfully() = runBlocking {

        manipulatorOfHomeScreensViewStates.setLoadedSuccessfully_NoTrackies()

        loadingPreviewOfListOfTrackies.assertIsNotDisplayed()
        previewOfListOfTrackies.assertIsNotDisplayed()

        loadingButtonDisplayAllTrackies.assertIsNotDisplayed()
        buttonDisplayAllTrackies.assertIsDisplayed()

        loadingButtonAddAnotherTrackie.assertIsNotDisplayed()
        buttonAddNewTrackie.assertIsDisplayed()

        loadingRowWithRadioButtons.assertIsNotDisplayed()
        rowWithRadioButtons.assertIsDisplayed()

        loadingRegularityChart.assertIsNotDisplayed()
        homeScreenWeeklyRegularityChart.assertIsDisplayed()
        homeScreenMonthlyRegularityChart.assertIsNotDisplayed()
        yearlyRegularityChartLoadedSuccessfully.assertIsNotDisplayed()

        assertNull(null)
    }

    @Test
    fun uiStateIs_FailedToLoadData() = runBlocking {

        manipulatorOfHomeScreensViewStates.setFailedToLoadData()

        loadingPreviewOfListOfTrackies.assertIsNotDisplayed()
        previewOfListOfTrackies.assertIsNotDisplayed()

        loadingButtonDisplayAllTrackies.assertIsNotDisplayed()
        buttonDisplayAllTrackies.assertIsNotDisplayed()

        loadingButtonAddAnotherTrackie.assertIsNotDisplayed()
        buttonAddNewTrackie.assertIsNotDisplayed()

        loadingRowWithRadioButtons.assertIsNotDisplayed()
        rowWithRadioButtons.assertIsNotDisplayed()

        loadingRegularityChart.assertIsNotDisplayed()
        homeScreenWeeklyRegularityChart.assertIsNotDisplayed()
        homeScreenMonthlyRegularityChart.assertIsNotDisplayed()
        yearlyRegularityChartLoadedSuccessfully.assertIsNotDisplayed()

        composeTestRule.onNodeWithText(text = "Whoops...").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "An error occurred while loading your data. Try again later.").assertIsDisplayed()

        assertNull(null)
    }

    @Test
    fun switchFrom_Loading_to_LoadedSuccessfully_noTrackies() = runBlocking {

        manipulatorOfHomeScreensViewStates.setLoading()

        loadingPreviewOfListOfTrackies.assertIsDisplayed()
        previewOfListOfTrackies.assertIsNotDisplayed()

        loadingButtonDisplayAllTrackies.assertIsDisplayed()
        buttonDisplayAllTrackies.assertIsNotDisplayed()

        loadingButtonAddAnotherTrackie.assertIsDisplayed()
        buttonAddNewTrackie.assertIsNotDisplayed()

        loadingRowWithRadioButtons.assertIsDisplayed()
        rowWithRadioButtons.assertIsNotDisplayed()

        loadingRegularityChart.assertIsDisplayed()
        homeScreenWeeklyRegularityChart.assertIsNotDisplayed()
        homeScreenMonthlyRegularityChart.assertIsNotDisplayed()
        yearlyRegularityChartLoadedSuccessfully.assertIsNotDisplayed()

        manipulatorOfHomeScreensViewStates.setLoadedSuccessfully_NoTrackies()

        loadingPreviewOfListOfTrackies.assertIsNotDisplayed()
        previewOfListOfTrackies.assertIsNotDisplayed()

        loadingButtonDisplayAllTrackies.assertIsNotDisplayed()
        buttonDisplayAllTrackies.assertIsDisplayed()

        loadingButtonAddAnotherTrackie.assertIsNotDisplayed()
        buttonAddNewTrackie.assertIsDisplayed()

        loadingRowWithRadioButtons.assertIsNotDisplayed()
        rowWithRadioButtons.assertIsDisplayed()

        loadingRegularityChart.assertIsNotDisplayed()
        homeScreenWeeklyRegularityChart.assertIsDisplayed()
        homeScreenMonthlyRegularityChart.assertIsNotDisplayed()
        yearlyRegularityChartLoadedSuccessfully.assertIsNotDisplayed()

        assertNull(null)
    }

    @Test
    fun switchFrom_Loading_to_LoadedSuccessfully_oneTrackie() = runBlocking {

        manipulatorOfHomeScreensViewStates.setLoading()

        loadingPreviewOfListOfTrackies.assertIsDisplayed()
        loadingButtonDisplayAllTrackies.assertIsDisplayed()
        loadingButtonAddAnotherTrackie.assertIsDisplayed()
        loadingRowWithRadioButtons.assertIsDisplayed()
        loadingRegularityChart.assertIsDisplayed()

        manipulatorOfHomeScreensViewStates.setLoadedSuccessfully_OneTrackie()

        previewOfListOfTrackies.assertIsDisplayed()
        composeTestRule.onNodeWithText("A").assertIsDisplayed()
        rowWithInformationAboutTrackiesLeftToDisplay.assertIsNotDisplayed()

        buttonDisplayAllTrackies.assertIsDisplayed()

        buttonAddNewTrackie.assertIsDisplayed()

        rowWithRadioButtons.assertIsDisplayed()

        homeScreenWeeklyRegularityChart.assertIsDisplayed()
        homeScreenMonthlyRegularityChart.assertIsNotDisplayed()
        yearlyRegularityChartLoadedSuccessfully.assertIsNotDisplayed()

        assertNull(null)
    }

    @Test
    fun switchFrom_Loading_to_LoadedSuccessfully_twoTrackies() = runBlocking {

        manipulatorOfHomeScreensViewStates.setLoading()

        loadingPreviewOfListOfTrackies.assertIsDisplayed()
        loadingButtonDisplayAllTrackies.assertIsDisplayed()
        loadingButtonAddAnotherTrackie.assertIsDisplayed()
        loadingRowWithRadioButtons.assertIsDisplayed()
        loadingRegularityChart.assertIsDisplayed()

        manipulatorOfHomeScreensViewStates.setLoadedSuccessfully_TwoTrackies()

        previewOfListOfTrackies.assertIsDisplayed()
        composeTestRule.onNodeWithText("A").assertIsDisplayed()
        composeTestRule.onNodeWithText("B").assertIsDisplayed()
        rowWithInformationAboutTrackiesLeftToDisplay.assertIsNotDisplayed()

        buttonDisplayAllTrackies.assertIsDisplayed()

        buttonAddNewTrackie.assertIsDisplayed()

        rowWithRadioButtons.assertIsDisplayed()

        homeScreenWeeklyRegularityChart.assertIsDisplayed()
        homeScreenMonthlyRegularityChart.assertIsNotDisplayed()
        yearlyRegularityChartLoadedSuccessfully.assertIsNotDisplayed()

        assertNull(null)
    }

    @Test
    fun switchFrom_Loading_to_LoadedSuccessfully_threeTrackies() = runBlocking {

        manipulatorOfHomeScreensViewStates.setLoading()

        loadingPreviewOfListOfTrackies.assertIsDisplayed()
        loadingButtonDisplayAllTrackies.assertIsDisplayed()
        loadingButtonAddAnotherTrackie.assertIsDisplayed()
        loadingRowWithRadioButtons.assertIsDisplayed()
        loadingRegularityChart.assertIsDisplayed()

        manipulatorOfHomeScreensViewStates.setLoadedSuccessfully_ThreeTrackies()

        previewOfListOfTrackies.assertIsDisplayed()
        composeTestRule.onNodeWithText("A").assertIsDisplayed()
        composeTestRule.onNodeWithText("B").assertIsDisplayed()
        composeTestRule.onNodeWithText("C").assertIsDisplayed()
        rowWithInformationAboutTrackiesLeftToDisplay.assertIsNotDisplayed()

        buttonDisplayAllTrackies.assertIsDisplayed()

        buttonAddNewTrackie.assertIsDisplayed()

        rowWithRadioButtons.assertIsDisplayed()

        homeScreenWeeklyRegularityChart.assertIsDisplayed()
        homeScreenMonthlyRegularityChart.assertIsNotDisplayed()
        yearlyRegularityChartLoadedSuccessfully.assertIsNotDisplayed()

        assertNull(null)
    }

    @Test
    fun switchFrom_Loading_to_LoadedSuccessfully_fourTrackies() = runBlocking {

        manipulatorOfHomeScreensViewStates.setLoading()

        loadingPreviewOfListOfTrackies.assertIsDisplayed()
        loadingButtonDisplayAllTrackies.assertIsDisplayed()
        loadingButtonAddAnotherTrackie.assertIsDisplayed()
        loadingRowWithRadioButtons.assertIsDisplayed()
        loadingRegularityChart.assertIsDisplayed()

        manipulatorOfHomeScreensViewStates.setLoadedSuccessfully_FourTrackies()

        previewOfListOfTrackies.assertIsDisplayed()
        composeTestRule.onNodeWithText("A").assertIsDisplayed()
        composeTestRule.onNodeWithText("B").assertIsDisplayed()
        composeTestRule.onNodeWithText("C").assertIsDisplayed()
        rowWithInformationAboutTrackiesLeftToDisplay.assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "+ 1 more trackie").assertIsDisplayed()

        buttonDisplayAllTrackies.assertIsDisplayed()

        buttonAddNewTrackie.assertIsDisplayed()

        rowWithRadioButtons.assertIsDisplayed()

        homeScreenWeeklyRegularityChart.assertIsDisplayed()
        homeScreenMonthlyRegularityChart.assertIsNotDisplayed()
        yearlyRegularityChartLoadedSuccessfully.assertIsNotDisplayed()

        assertNull(null)
    }

    @Test
    fun switchFrom_Loading_to_LoadedSuccessfully_fiveTrackies() = runBlocking {

        manipulatorOfHomeScreensViewStates.setLoading()

        loadingPreviewOfListOfTrackies.assertIsDisplayed()
        loadingButtonDisplayAllTrackies.assertIsDisplayed()
        loadingButtonAddAnotherTrackie.assertIsDisplayed()
        loadingRowWithRadioButtons.assertIsDisplayed()
        loadingRegularityChart.assertIsDisplayed()

        manipulatorOfHomeScreensViewStates.setLoadedSuccessfully_FiveTrackies()

        previewOfListOfTrackies.assertIsDisplayed()
        composeTestRule.onNodeWithText("A").assertIsDisplayed()
        composeTestRule.onNodeWithText("B").assertIsDisplayed()
        composeTestRule.onNodeWithText("C").assertIsDisplayed()
        rowWithInformationAboutTrackiesLeftToDisplay.assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "+ 2 more trackies").assertIsDisplayed()

        buttonDisplayAllTrackies.assertIsDisplayed()

        buttonAddNewTrackie.assertIsDisplayed()

        rowWithRadioButtons.assertIsDisplayed()

        homeScreenWeeklyRegularityChart.assertIsDisplayed()
        homeScreenMonthlyRegularityChart.assertIsNotDisplayed()
        yearlyRegularityChartLoadedSuccessfully.assertIsNotDisplayed()

        assertNull(null)
    }

    @Test
    fun switchFrom_Loading_to_FailedToLoadData() = runBlocking {

        manipulatorOfHomeScreensViewStates.setLoading()

        loadingPreviewOfListOfTrackies.assertIsDisplayed()
        previewOfListOfTrackies.assertIsNotDisplayed()

        loadingButtonDisplayAllTrackies.assertIsDisplayed()
        buttonDisplayAllTrackies.assertIsNotDisplayed()

        loadingButtonAddAnotherTrackie.assertIsDisplayed()
        buttonAddNewTrackie.assertIsNotDisplayed()

        loadingRowWithRadioButtons.assertIsDisplayed()
        rowWithRadioButtons.assertIsNotDisplayed()

        loadingRegularityChart.assertIsDisplayed()
        homeScreenWeeklyRegularityChart.assertIsNotDisplayed()
        homeScreenMonthlyRegularityChart.assertIsNotDisplayed()
        yearlyRegularityChartLoadedSuccessfully.assertIsNotDisplayed()

        manipulatorOfHomeScreensViewStates.setFailedToLoadData()

        loadingPreviewOfListOfTrackies.assertIsNotDisplayed()
        previewOfListOfTrackies.assertIsNotDisplayed()

        loadingButtonDisplayAllTrackies.assertIsNotDisplayed()
        buttonDisplayAllTrackies.assertIsNotDisplayed()

        loadingButtonAddAnotherTrackie.assertIsNotDisplayed()
        buttonAddNewTrackie.assertIsNotDisplayed()

        loadingRowWithRadioButtons.assertIsNotDisplayed()
        rowWithRadioButtons.assertIsNotDisplayed()

        loadingRegularityChart.assertIsNotDisplayed()
        homeScreenWeeklyRegularityChart.assertIsNotDisplayed()
        homeScreenMonthlyRegularityChart.assertIsNotDisplayed()
        yearlyRegularityChartLoadedSuccessfully.assertIsNotDisplayed()

        composeTestRule.onNodeWithText(text = "Whoops...").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "An error occurred while loading your data. Try again later.").assertIsDisplayed()

        assertNull(null)
    }

    @Test
    fun iconButtonToNavigateBetweenActivities_calls_onOpenSettings() = runBlocking {

        manipulatorOfHomeScreensViewStates.setLoadedSuccessfully_NoTrackies()

        iconButtonToNavigateBetweenActivities.performClick()
        assertTrue(onOpenSettingsWasCalled)

        assertNull(null)
    }

    @Test
    fun buttonDisplayAllTrackies_calls_onDisplayAllTrackies() = runBlocking {

        manipulatorOfHomeScreensViewStates.setLoadedSuccessfully_NoTrackies()

        buttonDisplayAllTrackies.performClick()
        assertTrue(onDisplayAllTrackiesWasCalled)

        assertNull(null)
    }

    @Test
    fun buttonAddNewTrackie_calls_onAddNewTrackie() = runBlocking {

        manipulatorOfHomeScreensViewStates.setLoadedSuccessfully_ThreeTrackies()

        buttonAddNewTrackie.performClick()
        assertTrue(onAddNewTrackieWasCalled)

        assertNull(null)
    }

    @Test
    fun onDisplayDetailedTrackie_isCalled() = runBlocking {

        manipulatorOfHomeScreensViewStates.setLoadedSuccessfully_OneTrackie()

        composeTestRule
            .onNodeWithContentDescription("iconButton")
            .performClick()

        assertEquals(
            Models.wholeWeekTrackieModel1,
            onDisplayDetailedTrackieWasCalled
        )
    }

    @Test
    fun switchingBetweenCharts() = runBlocking {

        manipulatorOfHomeScreensViewStates.setLoadedSuccessfully_TwoTrackies()

        composeTestRule
            .onNodeWithText("monthly")
            .performClick()

        homeScreenWeeklyRegularityChart.assertIsNotDisplayed()
        homeScreenMonthlyRegularityChart.assertIsDisplayed()
        yearlyRegularityChartLoadedSuccessfully.assertIsNotDisplayed()

        assertEquals(
            HomeScreenChartToDisplay.Monthly,
            onSwitchChartWasCalled
        )

        composeTestRule
            .onNodeWithText("weekly")
            .performClick()

        homeScreenWeeklyRegularityChart.assertIsDisplayed()
        homeScreenMonthlyRegularityChart.assertIsNotDisplayed()
        yearlyRegularityChartLoadedSuccessfully.assertIsNotDisplayed()

        assertEquals(
            HomeScreenChartToDisplay.Weekly,
            onSwitchChartWasCalled
        )

        composeTestRule
            .onNodeWithText("yearly")
            .performClick()

        homeScreenWeeklyRegularityChart.assertIsNotDisplayed()
        homeScreenMonthlyRegularityChart.assertIsNotDisplayed()
        yearlyRegularityChartLoadedSuccessfully.assertIsDisplayed()

        assertEquals(
            HomeScreenChartToDisplay.Yearly,
            onSwitchChartWasCalled
        )
    }

    @Test
    fun clickingBarsOfTheChart() = runBlocking {

        manipulatorOfHomeScreensViewStates.setLoadedSuccessfully_ThreeTrackies()

        composeTestRule
            .onNodeWithTag("bar monday")
            .performClick()

        composeTestRule
            .onNodeWithTag("activated bar monday")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("bar monday")
            .performClick()

        composeTestRule
            .onNodeWithTag("activated bar monday")
            .assertIsNotDisplayed()

        assertNull(null)
    }

//  UI elements of the upper and lower parts:
    private val iconButtonToNavigateBetweenActivities = composeTestRule.onNodeWithContentDescription(
        label = "iconButtonToNavigateBetweenActivities"
    )

    private val loadingPreviewOfListOfTrackies = composeTestRule.onNodeWithTag(testTag = "loadingPreviewOfListOfTrackies")
    private val previewOfListOfTrackies = composeTestRule.onNodeWithTag(testTag = "previewOfListOfTrackies")

    private val loadingButtonDisplayAllTrackies = composeTestRule.onNodeWithTag(testTag = "loadingButtonDisplayAllTrackies")
    private val buttonDisplayAllTrackies = composeTestRule.onNodeWithTag(testTag = "buttonDisplayAllTrackies")

    private val loadingButtonAddAnotherTrackie = composeTestRule.onNodeWithTag(testTag = "loadingButtonAddAnotherTrackie")
    private val buttonAddNewTrackie = composeTestRule.onNodeWithTag(testTag = "buttonAddNewTrackie")

    private val loadingRowWithRadioButtons = composeTestRule.onNodeWithTag(testTag = "loadingRowWithRadioButtons")
    private val rowWithRadioButtons = composeTestRule.onNodeWithTag(testTag = "rowWithRadioButtons")

    private val loadingRegularityChart = composeTestRule.onNodeWithTag(testTag = "loadingRegularityChart")
    private val homeScreenWeeklyRegularityChart = composeTestRule.onNodeWithTag(testTag = "homeScreenWeeklyRegularityChart")
    private val homeScreenMonthlyRegularityChart = composeTestRule.onNodeWithTag(testTag = "homeScreenMonthlyRegularityChart")
    private val yearlyRegularityChartLoadedSuccessfully = composeTestRule.onNodeWithTag(testTag = "yearlyRegularityChartLoadedSuccessfully")

    private val rowWithInformationAboutTrackiesLeftToDisplay =
        composeTestRule.onNodeWithTag(testTag = "Row with information about trackies left to display")
}