package userInterface.allTrackies

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.trackies.isSignedIn.allTrackies.buisness.ListOfTrackiesToDisplay
import com.example.trackies.isSignedIn.allTrackies.ui.allTrackies
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestOfAllTrackies {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var manipulatorOfAllTrackiesScreen: ManipulatorOfAllTrackiesScreen

    private var onReturnWasClicked = false
    private var onChangeListOfTrackiesToDisplayWasClicked: ListOfTrackiesToDisplay? = null
    private var onMarkTrackieAsIngestedWasClicked = false
    private var onDisplayDetailedTrackieWasClicked = false
    private var onFetchAllUsersTrackiesWasClicked = false

    @Before
    fun beforeTest() {

        manipulatorOfAllTrackiesScreen = ManipulatorOfAllTrackiesScreen()

        onReturnWasClicked = false
        onChangeListOfTrackiesToDisplayWasClicked = null
        onMarkTrackieAsIngestedWasClicked = false
        onDisplayDetailedTrackieWasClicked = false
        onFetchAllUsersTrackiesWasClicked = false

        composeTestRule.setContent {

            val listOfTrackiesToDisplay by manipulatorOfAllTrackiesScreen.listOfTrackiesToDisplay.collectAsState()
            val sharedViewModelViewState by manipulatorOfAllTrackiesScreen.sharedViewModelViewState.collectAsState()

            allTrackies(
                listOfTrackiesToDisplay = listOfTrackiesToDisplay,
                sharedViewModelViewState = sharedViewModelViewState,
                onReturn = {

                    onReturnWasClicked = true
                },
                onChangeListOfTrackiesToDisplay = {

                    onChangeListOfTrackiesToDisplayWasClicked = it
                    manipulatorOfAllTrackiesScreen.setListOfTrackiesToDisplay(it)
                },
                onMarkTrackieAsIngested = {},
                onDisplayDetailedTrackie = {},
                onFetchAllUsersTrackies = {

                    onFetchAllUsersTrackiesWasClicked = true
                }
            )
        }
    }

    @Test
    fun isLoadedSuccessfully_ListOfTrackiesToDisplayForToday_areDisplayedByDefault() {

        manipulatorOfAllTrackiesScreen.setLoadedSuccessfully_ThreeTrackies()

        iconButtonToNavigateBetweenActivities.assertIsDisplayed()

        mediumRadioTextButtonWholeWeek.assertIsDisplayed()
        mediumRadioTextButtonToday.assertIsDisplayed()

        displayAllTrackiesForToday.assertIsDisplayed()
        displayAllTrackies.assertIsNotDisplayed()

        composeTestRule.onNodeWithText("A").assertIsDisplayed()
        composeTestRule.onNodeWithText("B").assertIsDisplayed()
        composeTestRule.onNodeWithText("C").assertIsDisplayed()
    }

    @Test
    fun listOfTrackiesToDisplayToday_ListOfTrackiesToDisplayWholeWeek_switchingBetweenListsOfTrackies()  {

        manipulatorOfAllTrackiesScreen.setLoadedSuccessfully_ThreeTrackies()

//      display today's Trackies:
        iconButtonToNavigateBetweenActivities.assertIsDisplayed()

        mediumRadioTextButtonWholeWeek.assertIsDisplayed()
        mediumRadioTextButtonToday.assertIsDisplayed()

        displayAllTrackiesForToday.assertIsDisplayed()
        displayAllTrackies.assertIsNotDisplayed()

        composeTestRule.onNodeWithText("A").assertIsDisplayed()
        composeTestRule.onNodeWithText("B").assertIsDisplayed()
        composeTestRule.onNodeWithText("C").assertIsDisplayed()

//      display whole week Trackies:
        mediumRadioTextButtonWholeWeek.performClick()
        loadingPreviewOfListOfTrackies.assertIsDisplayed()

        assertTrue(onChangeListOfTrackiesToDisplayWasClicked == ListOfTrackiesToDisplay.WholeWeek)
        assertTrue(onFetchAllUsersTrackiesWasClicked)

        mediumRadioTextButtonToday.performClick()

        displayAllTrackiesForToday.assertIsDisplayed()
        displayAllTrackies.assertIsNotDisplayed()

        composeTestRule.onNodeWithText("A").assertIsDisplayed()
        composeTestRule.onNodeWithText("B").assertIsDisplayed()
        composeTestRule.onNodeWithText("C").assertIsDisplayed()

        assertTrue(onChangeListOfTrackiesToDisplayWasClicked == ListOfTrackiesToDisplay.Today)
    }

    @Test
    fun listOfTrackiesToDisplayWholeWeek_isDisplayedProperly() {

        manipulatorOfAllTrackiesScreen.setListOfTrackiesToDisplay(ListOfTrackiesToDisplay.WholeWeek)
        manipulatorOfAllTrackiesScreen.setLoadedSuccessfully_TrackiesForWholeWeek()

        displayAllTrackiesForToday.assertIsNotDisplayed()
        loadingPreviewOfListOfTrackies.assertIsNotDisplayed()
        displayAllTrackies.assertIsDisplayed()

        composeTestRule.onNodeWithText("A").assertIsDisplayed()
        composeTestRule.onNodeWithText("B").assertIsDisplayed()
        composeTestRule.onNodeWithText("C").assertIsDisplayed()
        composeTestRule.onNodeWithText("Aaa").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bbb").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ccc").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ddd").assertIsDisplayed()
    }

    private val iconButtonToNavigateBetweenActivities = composeTestRule.onNodeWithContentDescription("iconButtonToNavigateBetweenActivities")

    private val mediumRadioTextButtonWholeWeek = composeTestRule.onNodeWithText("whole week")
    private val mediumRadioTextButtonToday = composeTestRule.onNodeWithText("today")

    private val loadingPreviewOfListOfTrackies = composeTestRule.onNodeWithTag("loadingPreviewOfListOfTrackies")

    private val displayAllTrackiesForToday = composeTestRule.onNodeWithTag("displayAllTrackiesForToday")
    private val displayAllTrackies = composeTestRule.onNodeWithTag("displayAllTrackies")
}