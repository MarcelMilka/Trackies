package com.example.trackies.isSignedIn.user

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
            DaysOfWeek.monday,
            DaysOfWeek.tuesday,
            DaysOfWeek.wednesday,
            DaysOfWeek.thursday,
            DaysOfWeek.friday,
        ),
        ingestionTime = null
    )


    @Test
    fun `isFirstTimeInTheApp returns null, uiState should be set to AnErrorOccurred`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns null
        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Assertions
        assertEquals(SharedViewModelViewState.FailedToLoadData, viewModel.uiState.value)

//      Verification
        coVerify(exactly = 1) { userRepository.isFirstTimeInTheApp() }
    }

    @Test
    fun `isFirstTimeInTheApp does not return null, uiState SHOULD NOT be set to AnErrorOccurred`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Assertions
        assertNotEquals(
            SharedViewModelViewState.FailedToLoadData::class,
            viewModel.uiState.value::class
        )

//      Verification
        coVerify(exactly = 1) { userRepository.isFirstTimeInTheApp() }
    }


    @Test
    fun `needToResetPastWeekRegularity returns null, uiState should be set to AnErrorOccurred`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity() } returns null
        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Assertions
        assertEquals(SharedViewModelViewState.FailedToLoadData, viewModel.uiState.value)

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()
        }

        coVerifySequence {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()
        }
    }

    @Test
    fun `needToResetPastWeekRegularity returns true, resetWeeklyRegularity returns false, uiState should be set to AnErrorOccurred`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity() } returns true
        coEvery { userRepository.resetWeeklyRegularity() } returns false
        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Assertions
        assertEquals(
            SharedViewModelViewState.FailedToLoadData,
            viewModel.uiState.value
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()
            userRepository.resetWeeklyRegularity()
        }

        coVerifySequence {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()
            userRepository.resetWeeklyRegularity()
        }
    }

    @Test
    fun `needToResetPastWeekRegularity returns true, resetWeeklyRegularity returns true, uiState SHOULD NOT be set to AnErrorOccurred`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity() } returns true
        coEvery { userRepository.resetWeeklyRegularity() } returns true
        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Assertions
        assertNotEquals(
            SharedViewModelViewState.FailedToLoadData,
            viewModel.uiState.value
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()
            userRepository.resetWeeklyRegularity()
        }
    }

    @Test
    fun `needToResetPastWeekRegularity returns false, resetWeeklyRegularity IS NOT CALLED`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity() } returns false
        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Assertions
        assertNotEquals(
            SharedViewModelViewState.FailedToLoadData,
            viewModel.uiState.value
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()
        }

        coVerify(exactly = 0){
            userRepository.resetWeeklyRegularity()
        }
    }


    @Test
    fun `needToResetPastWeekRegularity returns true, resetWeeklyRegularity returns true, fetchUsersLicense returns null, FailedToLoadData`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        coEvery { userRepository.needToResetPastWeekRegularity() } returns true
        coEvery { userRepository.resetWeeklyRegularity() } returns true

        coEvery { userRepository.fetchUsersLicense() } returns null

        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Assertions
        assertEquals(
            SharedViewModelViewState.FailedToLoadData::class,
            viewModel.uiState.value::class
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()
            userRepository.resetWeeklyRegularity()

            userRepository.fetchUsersLicense()
        }
    }

    @Test
    fun `needToResetPastWeekRegularity returns true, resetWeeklyRegularity returns true, fetchTrackiesForToday returns null, FailedToLoadData`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        coEvery { userRepository.needToResetPastWeekRegularity() } returns true
        coEvery { userRepository.resetWeeklyRegularity() } returns true

        coEvery { userRepository.fetchUsersLicense() } returns mockk<LicenseModel>()
        coEvery { userRepository.fetchTrackiesForToday() } returns null

        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Assertions
        assertEquals(
            SharedViewModelViewState.FailedToLoadData::class,
            viewModel.uiState.value::class
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()
            userRepository.resetWeeklyRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
        }
    }

    @Test
    fun `needToResetPastWeekRegularity returns true, resetWeeklyRegularity returns true, fetchStatesOfTrackiesForToday returns null, FailedToLoadData`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        coEvery { userRepository.needToResetPastWeekRegularity() } returns true
        coEvery { userRepository.resetWeeklyRegularity() } returns true

        coEvery { userRepository.fetchUsersLicense() } returns mockk<LicenseModel>()
        coEvery { userRepository.fetchTrackiesForToday() } returns mockk<List<TrackieModel>>()
        coEvery { userRepository.fetchStatesOfTrackiesForToday() } returns null

        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Assertions
        assertEquals(
            SharedViewModelViewState.FailedToLoadData::class,
            viewModel.uiState.value::class
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()
            userRepository.resetWeeklyRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
            userRepository.fetchStatesOfTrackiesForToday()
        }
    }

    @Test
    fun `needToResetPastWeekRegularity returns true, resetWeeklyRegularity returns true, fetchWeeklyRegularity returns null, FailedToLoadData`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        coEvery { userRepository.needToResetPastWeekRegularity() } returns true
        coEvery { userRepository.resetWeeklyRegularity() } returns true

        coEvery { userRepository.fetchUsersLicense() } returns mockk<LicenseModel>()
        coEvery { userRepository.fetchTrackiesForToday() } returns mockk<List<TrackieModel>>()
        coEvery { userRepository.fetchStatesOfTrackiesForToday() } returns mockk<Map<String, Boolean>>()
        coEvery { userRepository.fetchWeeklyRegularity() } returns null

        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Assertions
        assertEquals(
            SharedViewModelViewState.FailedToLoadData::class,
            viewModel.uiState.value::class
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()
            userRepository.resetWeeklyRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
            userRepository.fetchStatesOfTrackiesForToday()
            userRepository.fetchWeeklyRegularity()
        }
    }

    @Test
    fun `needToResetPastWeekRegularity returns true, resetWeeklyRegularity returns true, LoadedSuccessfully`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        coEvery { userRepository.needToResetPastWeekRegularity() } returns true
        coEvery { userRepository.resetWeeklyRegularity() } returns true

        coEvery { userRepository.fetchUsersLicense() } returns mockk<LicenseModel>()
        coEvery { userRepository.fetchTrackiesForToday() } returns mockk<List<TrackieModel>>()
        coEvery { userRepository.fetchStatesOfTrackiesForToday() } returns mockk<Map<String, Boolean>>()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mockk<Map<String, Map<Int, Int>>>()

        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Assertions
        assertEquals(
            SharedViewModelViewState.LoadedSuccessfully::class,
            viewModel.uiState.value::class
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()
            userRepository.resetWeeklyRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
            userRepository.fetchStatesOfTrackiesForToday()
            userRepository.fetchWeeklyRegularity()
        }
    }


    @Test
    fun `needToResetPastWeekRegularity returns false, fetchUsersLicense returns null, FailedToLoadData`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        coEvery { userRepository.needToResetPastWeekRegularity() } returns false

        coEvery { userRepository.fetchUsersLicense() } returns null

        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Assertions
        assertEquals(
            SharedViewModelViewState.FailedToLoadData::class,
            viewModel.uiState.value::class
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
        }

        coVerify(exactly = 0) {

            userRepository.resetWeeklyRegularity()
        }
    }

    @Test
    fun `needToResetPastWeekRegularity returns false, fetchTrackiesForToday returns null, FailedToLoadData`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        coEvery { userRepository.needToResetPastWeekRegularity() } returns false

        coEvery { userRepository.fetchUsersLicense() } returns mockk()
        coEvery { userRepository.fetchTrackiesForToday() } returns null

        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Assertions
        assertEquals(
            SharedViewModelViewState.FailedToLoadData::class,
            viewModel.uiState.value::class
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
        }

        coVerify(exactly = 0) {

            userRepository.resetWeeklyRegularity()
        }
    }

    @Test
    fun `needToResetPastWeekRegularity returns false, fetchStatesOfTrackiesForToday returns null, FailedToLoadData`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        coEvery { userRepository.needToResetPastWeekRegularity() } returns false

        coEvery { userRepository.fetchUsersLicense() } returns mockk()
        coEvery { userRepository.fetchTrackiesForToday() } returns mockk()
        coEvery { userRepository.fetchStatesOfTrackiesForToday() } returns null

        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Assertions
        assertEquals(
            SharedViewModelViewState.FailedToLoadData::class,
            viewModel.uiState.value::class
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
            userRepository.fetchStatesOfTrackiesForToday()
        }

        coVerify(exactly = 0) {

            userRepository.resetWeeklyRegularity()
        }
    }

    @Test
    fun `needToResetPastWeekRegularity returns false, fetchWeeklyRegularity returns null, FailedToLoadData`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        coEvery { userRepository.needToResetPastWeekRegularity() } returns false

        coEvery { userRepository.fetchUsersLicense() } returns mockk()
        coEvery { userRepository.fetchTrackiesForToday() } returns mockk()
        coEvery { userRepository.fetchStatesOfTrackiesForToday() } returns mockk()
        coEvery { userRepository.fetchWeeklyRegularity() } returns null

        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Assertions
        assertEquals(
            SharedViewModelViewState.FailedToLoadData::class,
            viewModel.uiState.value::class
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
            userRepository.fetchStatesOfTrackiesForToday()
            userRepository.fetchWeeklyRegularity()
        }

        coVerify(exactly = 0) {

            userRepository.resetWeeklyRegularity()
        }
    }

    @Test
    fun `needToResetPastWeekRegularity returns false, LoadedSuccessfully`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        coEvery { userRepository.needToResetPastWeekRegularity() } returns false

        coEvery { userRepository.fetchUsersLicense() } returns mockk()
        coEvery { userRepository.fetchTrackiesForToday() } returns mockk()
        coEvery { userRepository.fetchStatesOfTrackiesForToday() } returns mockk()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mockk()

        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Assertions
        assertEquals(
            SharedViewModelViewState.LoadedSuccessfully::class,
            viewModel.uiState.value::class
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
            userRepository.fetchStatesOfTrackiesForToday()
            userRepository.fetchWeeklyRegularity()
        }

        coVerify(exactly = 0) {

            userRepository.resetWeeklyRegularity()
        }
    }


    @Test
    fun `addNewTrackie - addNewTrackie returns false, uiState does not get updated`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        coEvery { userRepository.needToResetPastWeekRegularity() } returns false
//
        coEvery { userRepository.fetchUsersLicense() } returns LicenseModel(
            active = false,
            validUntil = null,
            totalAmountOfTrackies = 0
        )
        coEvery { userRepository.fetchTrackiesForToday() } returns listOf<TrackieModel>()
        coEvery { userRepository.fetchStatesOfTrackiesForToday() } returns mapOf<String, Boolean>()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mapOf<String, Map<Int, Int>>(
            DaysOfWeek.monday to mapOf(0 to 0),
            DaysOfWeek.tuesday to mapOf(0 to 0),
            DaysOfWeek.wednesday to mapOf(0 to 0),
            DaysOfWeek.thursday to mapOf(0 to 0),
            DaysOfWeek.friday to mapOf(0 to 0),
            DaysOfWeek.saturday to mapOf(0 to 0),
            DaysOfWeek.sunday to mapOf(0 to 0),
        )

        coEvery { userRepository.addNewTrackie(trackieModel = trackieModel1) } returns false

        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Asserting shared view model view state does not have any Trackies:
        val expectedUiState = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(
                active = false,
                validUntil = null,
                totalAmountOfTrackies = 0
            ),
            trackiesForToday = listOf<TrackieModel>(),
            statesOfTrackiesForToday = mapOf<String, Boolean>(),
            weeklyRegularity = mapOf<String, Map<Int, Int>>(
                DaysOfWeek.monday to mapOf(0 to 0),
                DaysOfWeek.tuesday to mapOf(0 to 0),
                DaysOfWeek.wednesday to mapOf(0 to 0),
                DaysOfWeek.thursday to mapOf(0 to 0),
                DaysOfWeek.friday to mapOf(0 to 0),
                DaysOfWeek.saturday to mapOf(0 to 0),
                DaysOfWeek.sunday to mapOf(0 to 0),
            ),
            namesOfAllTrackies = null,
            allTrackies = null
        )
        val actualUiState = viewModel.uiState.value
        assertEquals(
            expectedUiState,
            actualUiState
        )

//      Adding new Trackie and checking if UI state has proper data:
        val expectedOnFailureMessage = "onFailedToAddNewTrackie"
        var actualOnFailureMessage = ""

        viewModel.addNewTrackie(
            trackieModel = trackieModel1,
            onFailedToAddNewTrackie = {

                actualOnFailureMessage = "onFailedToAddNewTrackie"
            },
        )

        advanceUntilIdle()

        assertEquals(
            expectedUiState,
            actualUiState
        )

        assertEquals(
            expectedOnFailureMessage,
            actualOnFailureMessage
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
            userRepository.fetchStatesOfTrackiesForToday()
            userRepository.fetchWeeklyRegularity()

            userRepository.addNewTrackie(trackieModel = trackieModel1)
        }

        coVerify(exactly = 0) { userRepository.resetWeeklyRegularity() }

        coVerifySequence {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
            userRepository.fetchStatesOfTrackiesForToday()
            userRepository.fetchWeeklyRegularity()

            userRepository.addNewTrackie(trackieModel = trackieModel1)
        }
    }

    @Test
    fun `addNewTrackie - addNewTrackie returns true, two new trackies get added properly`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        coEvery { userRepository.needToResetPastWeekRegularity() } returns false

        coEvery { userRepository.fetchUsersLicense() } returns LicenseModel(
            active = false,
            validUntil = null,
            totalAmountOfTrackies = 0
        )
        coEvery { userRepository.fetchTrackiesForToday() } returns listOf<TrackieModel>()
        coEvery { userRepository.fetchStatesOfTrackiesForToday() } returns mapOf<String, Boolean>()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mapOf<String, Map<Int, Int>>(
            DaysOfWeek.monday to mapOf(0 to 0),
            DaysOfWeek.tuesday to mapOf(0 to 0),
            DaysOfWeek.wednesday to mapOf(0 to 0),
            DaysOfWeek.thursday to mapOf(0 to 0),
            DaysOfWeek.friday to mapOf(0 to 0),
            DaysOfWeek.saturday to mapOf(0 to 0),
            DaysOfWeek.sunday to mapOf(0 to 0),
        )

        coEvery { userRepository.addNewTrackie(trackieModel1) } returns true
        coEvery { userRepository.addNewTrackie(trackieModel2) } returns true

        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Asserting shared view model view state does not have any Trackies:
        val expectedUiState1 = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(
                active = false,
                validUntil = null,
                totalAmountOfTrackies = 0
            ),
            trackiesForToday = listOf<TrackieModel>(),
            statesOfTrackiesForToday = mapOf<String, Boolean>(),
            weeklyRegularity = mapOf<String, Map<Int, Int>>(
                DaysOfWeek.monday to mapOf(0 to 0),
                DaysOfWeek.tuesday to mapOf(0 to 0),
                DaysOfWeek.wednesday to mapOf(0 to 0),
                DaysOfWeek.thursday to mapOf(0 to 0),
                DaysOfWeek.friday to mapOf(0 to 0),
                DaysOfWeek.saturday to mapOf(0 to 0),
                DaysOfWeek.sunday to mapOf(0 to 0),
            ),
            namesOfAllTrackies = null,
            allTrackies = null
        )
        val actualUiState1 = viewModel.uiState.value
        assertEquals(
            expectedUiState1,
            actualUiState1
        )

//      Adding new Trackie and checking if UI state has proper data:
        val expectedOnFailureMessage1 = ""
        var actualOnFailureMessage1 = ""

        val expectedOnFailureMessage2 = ""
        var actualOnFailureMessage2 = ""

        viewModel.addNewTrackie(
            trackieModel = trackieModel1,
            onFailedToAddNewTrackie = {
                actualOnFailureMessage1 = "asdalksjdhakljhd"
            }
        )
        viewModel.addNewTrackie(
            trackieModel = trackieModel2,
            onFailedToAddNewTrackie = {
                actualOnFailureMessage2 = "asdalksjdhakljhd"

            }
        )

        advanceUntilIdle()

        val expectedUiState2 = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(
                active = false,
                validUntil = null,
                totalAmountOfTrackies = 2
            ),
            trackiesForToday = listOf<TrackieModel>(trackieModel1),
            statesOfTrackiesForToday = mapOf<String, Boolean>(
                "Water" to false
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
            namesOfAllTrackies = mutableListOf("Water", "Banana"),
            allTrackies = null
        )
        val actualUiState2 = viewModel.uiState.value
        assertEquals(
            expectedUiState2,
            actualUiState2
        )

        assertEquals(
            expectedOnFailureMessage1,
            actualOnFailureMessage1
        )

        assertEquals(
            expectedOnFailureMessage2,
            actualOnFailureMessage2
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
            userRepository.fetchStatesOfTrackiesForToday()
            userRepository.fetchWeeklyRegularity()

            userRepository.addNewTrackie(trackieModel = trackieModel1)
            userRepository.addNewTrackie(trackieModel = trackieModel2)
        }

        coVerify(exactly = 0) { userRepository.resetWeeklyRegularity() }

        coVerifySequence {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
            userRepository.fetchStatesOfTrackiesForToday()
            userRepository.fetchWeeklyRegularity()

            userRepository.addNewTrackie(trackieModel = trackieModel1)
            userRepository.addNewTrackie(trackieModel = trackieModel2)
        }
    }


    @Test
    fun `deleteTrackie - deleteTrackie returns false, uiState does not get updated` () = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        coEvery { userRepository.needToResetPastWeekRegularity() } returns false
//
        coEvery { userRepository.fetchUsersLicense() } returns LicenseModel(
            active = false,
            validUntil = null,
            totalAmountOfTrackies = 0
        )
        coEvery { userRepository.fetchTrackiesForToday() } returns listOf<TrackieModel>()
        coEvery { userRepository.fetchStatesOfTrackiesForToday() } returns mapOf<String, Boolean>()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mapOf<String, Map<Int, Int>>(
            DaysOfWeek.monday to mapOf(0 to 0),
            DaysOfWeek.tuesday to mapOf(0 to 0),
            DaysOfWeek.wednesday to mapOf(0 to 0),
            DaysOfWeek.thursday to mapOf(0 to 0),
            DaysOfWeek.friday to mapOf(0 to 0),
            DaysOfWeek.saturday to mapOf(0 to 0),
            DaysOfWeek.sunday to mapOf(0 to 0),
        )

        coEvery { userRepository.deleteTrackie(trackieModel = trackieModel1) } returns false

        val viewModel = SharedViewModel(userRepository)

//      Wait until all tasks complete:
        advanceUntilIdle()

//      Asserting shared view model view state does not have any Trackies:
        val expectedUiState = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(
                active = false,
                validUntil = null,
                totalAmountOfTrackies = 0
            ),
            trackiesForToday = listOf<TrackieModel>(),
            statesOfTrackiesForToday = mapOf<String, Boolean>(),
            weeklyRegularity = mapOf<String, Map<Int, Int>>(
                DaysOfWeek.monday to mapOf(0 to 0),
                DaysOfWeek.tuesday to mapOf(0 to 0),
                DaysOfWeek.wednesday to mapOf(0 to 0),
                DaysOfWeek.thursday to mapOf(0 to 0),
                DaysOfWeek.friday to mapOf(0 to 0),
                DaysOfWeek.saturday to mapOf(0 to 0),
                DaysOfWeek.sunday to mapOf(0 to 0),
            ),
            namesOfAllTrackies = null,
            allTrackies = null
        )
        val actualUiState = viewModel.uiState.value
        assertEquals(
            expectedUiState,
            actualUiState
        )

//      Adding new Trackie and checking if UI state has proper data:
        val expectedOnFailureMessage = "onFailedToDeleteNewTrackie"
        var actualOnFailureMessage = ""

        viewModel.deleteTrackie(
            trackieViewState = trackieModel1,
            onFailedToDeleteTrackie = {

                actualOnFailureMessage = "onFailedToDeleteNewTrackie"
            }
        )

        advanceUntilIdle()

        assertEquals(
            expectedUiState,
            actualUiState
        )

        assertEquals(
            expectedOnFailureMessage,
            actualOnFailureMessage
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
            userRepository.fetchStatesOfTrackiesForToday()
            userRepository.fetchWeeklyRegularity()

            userRepository.deleteTrackie(trackieModel = trackieModel1)
        }

        coVerify(exactly = 0) { userRepository.resetWeeklyRegularity() }

        coVerifySequence {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
            userRepository.fetchStatesOfTrackiesForToday()
            userRepository.fetchWeeklyRegularity()

            userRepository.deleteTrackie(trackieModel = trackieModel1)
        }
    }

    @Test
    fun `deleteTrackie - deleteTrackie returns false, uiState does not get updated v2`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        coEvery { userRepository.needToResetPastWeekRegularity() } returns false

        coEvery { userRepository.fetchUsersLicense() } returns LicenseModel(
            active = false,
            validUntil = null,
            totalAmountOfTrackies = 0
        )
        coEvery { userRepository.fetchTrackiesForToday() } returns listOf<TrackieModel>()
        coEvery { userRepository.fetchStatesOfTrackiesForToday() } returns mapOf<String, Boolean>()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mapOf<String, Map<Int, Int>>(
            DaysOfWeek.monday to mapOf(0 to 0),
            DaysOfWeek.tuesday to mapOf(0 to 0),
            DaysOfWeek.wednesday to mapOf(0 to 0),
            DaysOfWeek.thursday to mapOf(0 to 0),
            DaysOfWeek.friday to mapOf(0 to 0),
            DaysOfWeek.saturday to mapOf(0 to 0),
            DaysOfWeek.sunday to mapOf(0 to 0),
        )

        coEvery { userRepository.addNewTrackie(trackieModel1) } returns true
        coEvery { userRepository.addNewTrackie(trackieModel2) } returns true
        coEvery { userRepository.deleteTrackie(trackieModel2) } returns false

        val viewModel = SharedViewModel(userRepository)

        viewModel.addNewTrackie(
            trackieModel = trackieModel1,
            onFailedToAddNewTrackie = {}
        )
        viewModel.addNewTrackie(
            trackieModel = trackieModel2,
            onFailedToAddNewTrackie = {}
        )

//      Failing to delete a trackie and making sure ui does not change
        val expectedOnFailureMessage = "lklskdjhfaskjfhasl"
        var actualOnFailureMessage = ""
        viewModel.deleteTrackie(
            trackieViewState = trackieModel2,
            onFailedToDeleteTrackie = {

                actualOnFailureMessage = "lklskdjhfaskjfhasl"
            }
        )

        advanceUntilIdle()

        assertEquals(
            expectedOnFailureMessage,
            actualOnFailureMessage
        )

        val expectedUiState = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(
                active = false,
                validUntil = null,
                totalAmountOfTrackies = 2
            ),
            trackiesForToday = listOf<TrackieModel>(trackieModel1),
            statesOfTrackiesForToday = mapOf<String, Boolean>(
                "Water" to false
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
            namesOfAllTrackies = mutableListOf("Water", "Banana"),
            allTrackies = null
        )
        val actualUiState = viewModel.uiState.value
        assertEquals(
            expectedUiState,
            actualUiState
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
            userRepository.fetchStatesOfTrackiesForToday()
            userRepository.fetchWeeklyRegularity()

            userRepository.addNewTrackie(trackieModel = trackieModel1)
            userRepository.addNewTrackie(trackieModel = trackieModel2)

            userRepository.deleteTrackie(trackieModel = trackieModel2)
        }

        coVerify(exactly = 0) { userRepository.resetWeeklyRegularity() }

        coVerifySequence {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
            userRepository.fetchStatesOfTrackiesForToday()
            userRepository.fetchWeeklyRegularity()

            userRepository.addNewTrackie(trackieModel = trackieModel1)
            userRepository.addNewTrackie(trackieModel = trackieModel2)

            userRepository.deleteTrackie(trackieModel = trackieModel2)
        }
    }

    @Test
    fun `deleteTrackie - deleteTrackie returns true, uiState gets updated properly, one trackie is left after deleting`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        coEvery { userRepository.needToResetPastWeekRegularity() } returns false

        coEvery { userRepository.fetchUsersLicense() } returns LicenseModel(
            active = false,
            validUntil = null,
            totalAmountOfTrackies = 0
        )
        coEvery { userRepository.fetchTrackiesForToday() } returns listOf<TrackieModel>()
        coEvery { userRepository.fetchStatesOfTrackiesForToday() } returns mapOf<String, Boolean>()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mapOf<String, Map<Int, Int>>(
            DaysOfWeek.monday to mapOf(0 to 0),
            DaysOfWeek.tuesday to mapOf(0 to 0),
            DaysOfWeek.wednesday to mapOf(0 to 0),
            DaysOfWeek.thursday to mapOf(0 to 0),
            DaysOfWeek.friday to mapOf(0 to 0),
            DaysOfWeek.saturday to mapOf(0 to 0),
            DaysOfWeek.sunday to mapOf(0 to 0),
        )

        coEvery { userRepository.addNewTrackie(trackieModel1) } returns true
        coEvery { userRepository.addNewTrackie(trackieModel2) } returns true
        coEvery { userRepository.deleteTrackie(trackieModel2) } returns true

        val viewModel = SharedViewModel(userRepository)

        viewModel.addNewTrackie(
            trackieModel = trackieModel1,
            onFailedToAddNewTrackie = {}
        )
        viewModel.addNewTrackie(
            trackieModel = trackieModel2,
            onFailedToAddNewTrackie = {}
        )

//      Deleting one of the trackies and checking if the ui gets updated
        val expectedOnFailureMessage = ""
        var actualOnFailureMessage = ""
        viewModel.deleteTrackie(
            trackieViewState = trackieModel2,
            onFailedToDeleteTrackie = {

                actualOnFailureMessage = "lklskdjhfaskjfhasl"
            }
        )

        advanceUntilIdle()

        assertEquals(
            expectedOnFailureMessage,
            actualOnFailureMessage
        )

        val expectedUiState = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(
                active = false,
                validUntil = null,
                totalAmountOfTrackies = 1
            ),
            trackiesForToday = listOf<TrackieModel>(trackieModel1),
            statesOfTrackiesForToday = mapOf<String, Boolean>(
                "Water" to false
            ),
            weeklyRegularity = mapOf<String, Map<Int, Int>>(
                DaysOfWeek.monday to mapOf(1 to 0),
                DaysOfWeek.tuesday to mapOf(1 to 0),
                DaysOfWeek.wednesday to mapOf(1 to 0),
                DaysOfWeek.thursday to mapOf(1 to 0),
                DaysOfWeek.friday to mapOf(1 to 0),
                DaysOfWeek.saturday to mapOf(1 to 0),
                DaysOfWeek.sunday to mapOf(1 to 0),
            ),
            namesOfAllTrackies = mutableListOf("Water"),
            allTrackies = null
        )
        val actualUiState = viewModel.uiState.value
        assertEquals(
            expectedUiState,
            actualUiState
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
            userRepository.fetchStatesOfTrackiesForToday()
            userRepository.fetchWeeklyRegularity()

            userRepository.addNewTrackie(trackieModel = trackieModel1)
            userRepository.addNewTrackie(trackieModel = trackieModel2)

            userRepository.deleteTrackie(trackieModel = trackieModel2)
        }

        coVerify(exactly = 0) { userRepository.resetWeeklyRegularity() }

        coVerifySequence {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
            userRepository.fetchStatesOfTrackiesForToday()
            userRepository.fetchWeeklyRegularity()

            userRepository.addNewTrackie(trackieModel = trackieModel1)
            userRepository.addNewTrackie(trackieModel = trackieModel2)

            userRepository.deleteTrackie(trackieModel = trackieModel2)
        }
    }

    @Test
    fun `deleteTrackie - deleteTrackie returns true, uiState gets updated properly, there are no any trackies left after deletion`() = runTest {

//      Preparation of the test:
        coEvery { userRepository.isFirstTimeInTheApp() } returns false
        coEvery { userRepository.needToResetPastWeekRegularity() } returns false

        coEvery { userRepository.fetchUsersLicense() } returns LicenseModel(
            active = false,
            validUntil = null,
            totalAmountOfTrackies = 0
        )
        coEvery { userRepository.fetchTrackiesForToday() } returns listOf<TrackieModel>()
        coEvery { userRepository.fetchStatesOfTrackiesForToday() } returns mapOf<String, Boolean>()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mapOf<String, Map<Int, Int>>(
            DaysOfWeek.monday to mapOf(0 to 0),
            DaysOfWeek.tuesday to mapOf(0 to 0),
            DaysOfWeek.wednesday to mapOf(0 to 0),
            DaysOfWeek.thursday to mapOf(0 to 0),
            DaysOfWeek.friday to mapOf(0 to 0),
            DaysOfWeek.saturday to mapOf(0 to 0),
            DaysOfWeek.sunday to mapOf(0 to 0),
        )

        coEvery { userRepository.addNewTrackie(trackieModel1) } returns true
        coEvery { userRepository.addNewTrackie(trackieModel2) } returns true

        coEvery { userRepository.deleteTrackie(trackieModel1) } returns true
        coEvery { userRepository.deleteTrackie(trackieModel2) } returns true

        val viewModel = SharedViewModel(userRepository)

        viewModel.addNewTrackie(
            trackieModel = trackieModel1,
            onFailedToAddNewTrackie = {}
        )
        viewModel.addNewTrackie(
            trackieModel = trackieModel2,
            onFailedToAddNewTrackie = {}
        )

//      Deleting al the trackies and checking if the ui gets updated
        val expectedOnFailureMessage1 = ""
        var actualOnFailureMessage1 = ""
        viewModel.deleteTrackie(
            trackieViewState = trackieModel1,
            onFailedToDeleteTrackie = {

                actualOnFailureMessage1 = "lklskdjhfaskjfhasl"
            }
        )

        assertEquals(
            expectedOnFailureMessage1,
            actualOnFailureMessage1
        )

        val expectedOnFailureMessage2 = ""
        var actualOnFailureMessage2 = ""
        viewModel.deleteTrackie(
            trackieViewState = trackieModel2,
            onFailedToDeleteTrackie = {

                actualOnFailureMessage1 = "lklskdjhfaskjfhasl"
            }
        )

        advanceUntilIdle()

        assertEquals(
            expectedOnFailureMessage2,
            actualOnFailureMessage2
        )

        val expectedUiState = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(
                active = false,
                validUntil = null,
                totalAmountOfTrackies = 0
            ),
            trackiesForToday = listOf<TrackieModel>(),
            statesOfTrackiesForToday = mapOf<String, Boolean>(),
            weeklyRegularity = mapOf<String, Map<Int, Int>>(
                DaysOfWeek.monday to mapOf(0 to 0),
                DaysOfWeek.tuesday to mapOf(0 to 0),
                DaysOfWeek.wednesday to mapOf(0 to 0),
                DaysOfWeek.thursday to mapOf(0 to 0),
                DaysOfWeek.friday to mapOf(0 to 0),
                DaysOfWeek.saturday to mapOf(0 to 0),
                DaysOfWeek.sunday to mapOf(0 to 0),
            ),
            namesOfAllTrackies = mutableListOf(),
            allTrackies = null
        )
        val actualUiState = viewModel.uiState.value
        assertEquals(
            expectedUiState,
            actualUiState
        )

//      Verification
        coVerify(exactly = 1) {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
            userRepository.fetchStatesOfTrackiesForToday()
            userRepository.fetchWeeklyRegularity()

            userRepository.addNewTrackie(trackieModel = trackieModel1)
            userRepository.addNewTrackie(trackieModel = trackieModel2)

            userRepository.deleteTrackie(trackieModel = trackieModel1)
            userRepository.deleteTrackie(trackieModel = trackieModel2)
        }

        coVerify(exactly = 0) { userRepository.resetWeeklyRegularity() }

        coVerifySequence {
            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity()

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday()
            userRepository.fetchStatesOfTrackiesForToday()
            userRepository.fetchWeeklyRegularity()

            userRepository.addNewTrackie(trackieModel = trackieModel1)
            userRepository.addNewTrackie(trackieModel = trackieModel2)

            userRepository.deleteTrackie(trackieModel = trackieModel1)
            userRepository.deleteTrackie(trackieModel = trackieModel2)
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