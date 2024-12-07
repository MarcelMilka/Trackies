package com.example.trackies.isSignedIn.user

import androidx.compose.runtime.collectAsState
import com.example.globalConstants.CurrentTime
import com.example.globalConstants.DaysOfWeek
import com.example.trackies.isSignedIn.user.buisness.LicenseModel
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelViewState
import com.example.trackies.isSignedIn.user.data.UserRepository
import com.example.trackies.isSignedIn.user.vm.SharedViewModel
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.coroutines.ContinuationInterceptor

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class SharedViewModelUnitTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @MockK
    lateinit var userRepository: UserRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun beforeTest() {

        MockKAnnotations.init(this, relaxed = true)
        clearAllMocks()
    }

    private val trackieModel1 = TrackieModel(
        name = "Water",
        totalDose = 3000,
        measuringUnit = "ml",
        repeatOn = listOf(
            DaysOfWeek.monday,
            DaysOfWeek.tuesday,
            DaysOfWeek.wednesday,
            DaysOfWeek.thursday,
            DaysOfWeek.friday,
            DaysOfWeek.saturday,
            DaysOfWeek.sunday,
        ),
        ingestionTime = null
    )

    private val trackieModel2 = TrackieModel(
        name = "Banana",
        totalDose = 1,
        measuringUnit = "pcs",
        repeatOn = listOf(
            DaysOfWeek.saturday,
            DaysOfWeek.sunday
        ),
        ingestionTime = null
    )

    private val trackieModel3 = TrackieModel(
        name = "Chocolate",
        totalDose = 1,
        measuringUnit = "pcs",
        repeatOn = listOf(
            DaysOfWeek.monday
        ),
        ingestionTime = null
    )

    @Test
    fun `markTrackieAsIngested - markTrackieAsIngested returns false, onFailedToMarkTrackieAsIngested gets called`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        coEvery { userRepository.needToResetPastWeekRegularity() } returns false

        coEvery { userRepository.fetchUsersLicense() } returns LicenseModel(
            active = false,
            validUntil = null,
            totalAmountOfTrackies = 2
        )
        coEvery { userRepository.fetchTrackiesForToday(DaysOfWeek.wednesday) } returns listOf<TrackieModel>(trackieModel1, trackieModel2)
        coEvery { userRepository.fetchStatesOfTrackiesForToday(DaysOfWeek.wednesday) } returns mapOf<String, Boolean>(
            "Water" to false,
            "Banana" to false
        )
        coEvery { userRepository.fetchWeeklyRegularity() } returns mapOf<String, Map<Int, Int>>(
            DaysOfWeek.monday to mapOf(1 to 0),
            DaysOfWeek.tuesday to mapOf(1 to 0),
            DaysOfWeek.wednesday to mapOf(1 to 0),
            DaysOfWeek.thursday to mapOf(1 to 0),
            DaysOfWeek.friday to mapOf(1 to 0),
            DaysOfWeek.saturday to mapOf(2 to 0),
            DaysOfWeek.sunday to mapOf(2 to 0),
        )

        coEvery { userRepository.markTrackieAsIngested(DaysOfWeek.wednesday, trackieModel1) } returns false

        val viewModel = SharedViewModel(userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedOnFailedToMarkTrackieAsIngested = "onFailedToMarkTrackieAsIngested"
        var actualOnFailedToMarkTrackieAsIngested = ""
        viewModel.markTrackieAsIngested(
            trackieModel = trackieModel1,
            currentDayOfWeek = DaysOfWeek.wednesday,
            onFailedToMarkTrackieAsIngested = {

                actualOnFailedToMarkTrackieAsIngested = "onFailedToMarkTrackieAsIngested"
            }
        )

        assertEquals(
            expectedOnFailedToMarkTrackieAsIngested,
            actualOnFailedToMarkTrackieAsIngested
        )

        val expectedUiState = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(
                active = false,
                validUntil = null,
                totalAmountOfTrackies = 2
            ),
            trackiesForToday = listOf<TrackieModel>(trackieModel1, trackieModel2),
            statesOfTrackiesForToday = mapOf<String, Boolean>(
                "Water" to false,
                "Banana" to false
            ),
            weeklyRegularity = mapOf<String, Map<Int, Int>>(
                DaysOfWeek.monday to mapOf(1 to 0),
                DaysOfWeek.tuesday to mapOf(1 to 0),
                DaysOfWeek.wednesday to mapOf(1 to 0),
                DaysOfWeek.thursday to mapOf(1 to 0),
                DaysOfWeek.friday to mapOf(1 to 0),
                DaysOfWeek.saturday to mapOf(2 to 0),
                DaysOfWeek.sunday to mapOf(2 to 0),
            ),
            namesOfAllTrackies = null,
            allTrackies = null
        )
        val actualUiState = viewModel.uiState.value

        assertEquals(
            expectedUiState,
            actualUiState
        )

//      Verifications:
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(DaysOfWeek.wednesday)
            userRepository.fetchStatesOfTrackiesForToday(DaysOfWeek.wednesday)
            userRepository.fetchWeeklyRegularity()

            userRepository.markTrackieAsIngested(DaysOfWeek.wednesday, trackieModel1)
        }

        coVerifySequence {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(DaysOfWeek.wednesday)
            userRepository.fetchStatesOfTrackiesForToday(DaysOfWeek.wednesday)
            userRepository.fetchWeeklyRegularity()

            userRepository.markTrackieAsIngested(DaysOfWeek.wednesday, trackieModel1)
        }
    }

    @Test
    fun `markTrackieAsIngested - markTrackieAsIngested returns true, SharedViewModelViewState is updated properly`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        coEvery { userRepository.needToResetPastWeekRegularity() } returns false

        coEvery { userRepository.fetchUsersLicense() } returns LicenseModel(
            active = false,
            validUntil = null,
            totalAmountOfTrackies = 2
        )
        coEvery { userRepository.fetchTrackiesForToday(DaysOfWeek.wednesday) } returns listOf<TrackieModel>(trackieModel1)
        coEvery { userRepository.fetchStatesOfTrackiesForToday(DaysOfWeek.wednesday) } returns mapOf<String, Boolean>(
            "Water" to false
        )
        coEvery { userRepository.fetchWeeklyRegularity() } returns mapOf<String, Map<Int, Int>>(
            DaysOfWeek.monday to mapOf(1 to 0),
            DaysOfWeek.tuesday to mapOf(1 to 0),
            DaysOfWeek.wednesday to mapOf(1 to 0),
            DaysOfWeek.thursday to mapOf(1 to 0),
            DaysOfWeek.friday to mapOf(1 to 0),
            DaysOfWeek.saturday to mapOf(2 to 0),
            DaysOfWeek.sunday to mapOf(2 to 0),
        )

        coEvery { userRepository.markTrackieAsIngested(DaysOfWeek.wednesday, trackieModel1) } returns true

        val viewModel = SharedViewModel(userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedOnFailedToMarkTrackieAsIngested = ""
        var actualOnFailedToMarkTrackieAsIngested = ""
        viewModel.markTrackieAsIngested(
            trackieModel = trackieModel1,
            currentDayOfWeek = DaysOfWeek.wednesday,
            onFailedToMarkTrackieAsIngested = {

                actualOnFailedToMarkTrackieAsIngested = "onFailedToMarkTrackieAsIngested"
            }
        )

        assertEquals(
            expectedOnFailedToMarkTrackieAsIngested,
            actualOnFailedToMarkTrackieAsIngested
        )

        val expectedUiState = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(
                active = false,
                validUntil = null,
                totalAmountOfTrackies = 2
            ),
            trackiesForToday = listOf<TrackieModel>(trackieModel1, trackieModel2),
            statesOfTrackiesForToday = mapOf<String, Boolean>(
                "Water" to true
            ),
            weeklyRegularity = mapOf<String, Map<Int, Int>>(
                DaysOfWeek.monday to mapOf(1 to 0),
                DaysOfWeek.tuesday to mapOf(1 to 0),
                DaysOfWeek.wednesday to mapOf(1 to 1),
                DaysOfWeek.thursday to mapOf(1 to 0),
                DaysOfWeek.friday to mapOf(1 to 0),
                DaysOfWeek.saturday to mapOf(2 to 0),
                DaysOfWeek.sunday to mapOf(2 to 0),
            ),
            namesOfAllTrackies = null,
            allTrackies = null
        )
        val actualUiState = viewModel.uiState.value

        assertEquals(
            expectedUiState,
            actualUiState
        )

//      Verifications:
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(DaysOfWeek.wednesday)
            userRepository.fetchStatesOfTrackiesForToday(DaysOfWeek.wednesday)
            userRepository.fetchWeeklyRegularity()

            userRepository.markTrackieAsIngested(DaysOfWeek.wednesday, trackieModel1)
        }

        coVerifySequence {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(DaysOfWeek.wednesday)
            userRepository.fetchStatesOfTrackiesForToday(DaysOfWeek.wednesday)
            userRepository.fetchWeeklyRegularity()

            userRepository.markTrackieAsIngested(DaysOfWeek.wednesday, trackieModel1)
        }
    }
}














@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
class MainCoroutineRule : TestWatcher(), TestCoroutineScope by TestCoroutineScope() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(this.coroutineContext[ContinuationInterceptor] as CoroutineDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }

}