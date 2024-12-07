package com.example.trackies.isSignedIn.user

import com.example.globalConstants.DaysOfWeek
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelViewState
import com.example.trackies.isSignedIn.user.data.UserRepository
import com.example.trackies.isSignedIn.user.vm.SharedViewModel
import com.example.trackies.isSignedIn.user.vm.SharedViewModelErrors
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
import kotlinx.coroutines.runBlocking
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

    private val wholeWeekTrackieModel = TrackieModel(
        name = "A",
        totalDose = 3000,
        measuringUnit = "ml",
        repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday,),
        ingestionTime = null
    )

    private val mondayToFridayTrackieModel = TrackieModel(
        name = "B",
        totalDose = 1,
        measuringUnit = "pcs",
        repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday),
        ingestionTime = null
    )

    private val mondayTrackieModel = TrackieModel(
        name = "C",
        totalDose = 1,
        measuringUnit = "g",
        repeatOn = listOf(DaysOfWeek.monday),
        ingestionTime = null
    )

    @Test
    fun `init - repository's isFirstTimeInTheApp() returns null, SharedViewModelViewState is FailedToLoadData`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns null
        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedError = SharedViewModelErrors.IsFirstTimeInTheAppReturnedError
        val actualError = sharedViewModel.error

        assertEquals(
            expectedError,
            actualError
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData
        val actualSharedViewModelViewState = sharedViewModel.uiState.value

        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            userRepository.isFirstTimeInTheApp()
        }
    }

    @Test
    fun `init - repository's needToResetPastWeekRegularity() returns null, SharedViewModelViewState is FailedToLoadData`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns null
        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedError = SharedViewModelErrors.NeedToResetPastWeekRegularityReturnedError
        val actualError = sharedViewModel.error
        assertEquals(
            expectedError,
            actualError
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData
        val actualSharedViewModelViewState = sharedViewModel.uiState.value
        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
        }
    }


    @Test
    fun `init - repository's needToResetPastWeekRegularity() returns true, repository's resetWeeklyRegularity() returns false, SharedViewModelViewState is FailedToLoadData`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns true
        coEvery { userRepository.resetWeeklyRegularity(any()) } returns false
        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedError = SharedViewModelErrors.ResetWeeklyRegularityReturnedError
        val actualError = sharedViewModel.error
        assertEquals(
            expectedError,
            actualError
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData
        val actualSharedViewModelViewState = sharedViewModel.uiState.value
        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())
        }
    }

    @Test
    fun `init - repository's needToResetPastWeekRegularity() returns true, repository's resetWeeklyRegularity() returns true, SharedViewModelViewState is LoadedSuccessfully`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns true
        coEvery { userRepository.resetWeeklyRegularity(any()) } returns true

        coEvery { userRepository.fetchUsersLicense() } returns mockk()
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mockk()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedError = null
        val actualError = sharedViewModel.error
        assertEquals(
            expectedError,
            actualError
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.LoadedSuccessfully::class
        val actualSharedViewModelViewState = sharedViewModel.uiState.value::class
        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }
    }

    @Test
    fun `init - repository's needToResetPastWeekRegularity() returns true, repository's resetWeeklyRegularity() returns true, fetchUsersLicense() returns null, SharedViewModelViewState is FailedToLoadData`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns true
        coEvery { userRepository.resetWeeklyRegularity(any()) } returns true

        coEvery { userRepository.fetchUsersLicense() } returns null
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mockk()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedError = SharedViewModelErrors.AtLeastOneFinalMethodReturnedError
        val actualError = sharedViewModel.error
        assertEquals(
            expectedError,
            actualError
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData
        val actualSharedViewModelViewState = sharedViewModel.uiState.value
        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }
    }

    @Test
    fun `init - repository's needToResetPastWeekRegularity() returns true, repository's resetWeeklyRegularity() returns true, fetchTrackiesForToday() returns null, SharedViewModelViewState is FailedToLoadData`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns true
        coEvery { userRepository.resetWeeklyRegularity(any()) } returns true

        coEvery { userRepository.fetchUsersLicense() } returns mockk()
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns null
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mockk()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedError = SharedViewModelErrors.AtLeastOneFinalMethodReturnedError
        val actualError = sharedViewModel.error
        assertEquals(
            expectedError,
            actualError
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData
        val actualSharedViewModelViewState = sharedViewModel.uiState.value
        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }
    }

    @Test
    fun `init - repository's needToResetPastWeekRegularity() returns true, repository's resetWeeklyRegularity() returns true, fetchStatesOfTrackiesForToday() returns null, SharedViewModelViewState is FailedToLoadData`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns true
        coEvery { userRepository.resetWeeklyRegularity(any()) } returns true

        coEvery { userRepository.fetchUsersLicense() } returns mockk()
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns null
        coEvery { userRepository.fetchWeeklyRegularity() } returns mockk()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedError = SharedViewModelErrors.AtLeastOneFinalMethodReturnedError
        val actualError = sharedViewModel.error
        assertEquals(
            expectedError,
            actualError
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData
        val actualSharedViewModelViewState = sharedViewModel.uiState.value
        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }
    }

    @Test
    fun `init - repository's needToResetPastWeekRegularity() returns true, repository's resetWeeklyRegularity() returns true, fetchWeeklyRegularity() returns null, SharedViewModelViewState is FailedToLoadData`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns true
        coEvery { userRepository.resetWeeklyRegularity(any()) } returns true

        coEvery { userRepository.fetchUsersLicense() } returns mockk()
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchWeeklyRegularity() } returns null

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedError = SharedViewModelErrors.AtLeastOneFinalMethodReturnedError
        val actualError = sharedViewModel.error
        assertEquals(
            expectedError,
            actualError
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData
        val actualSharedViewModelViewState = sharedViewModel.uiState.value
        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }
    }

    @Test
    fun `init - repository's needToResetPastWeekRegularity() returns true, repository's resetWeeklyRegularity() returns true, all final methods return null, SharedViewModelViewState is FailedToLoadData`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns true
        coEvery { userRepository.resetWeeklyRegularity(any()) } returns true

        coEvery { userRepository.fetchUsersLicense() } returns null
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns null
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns null
        coEvery { userRepository.fetchWeeklyRegularity() } returns null

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedError = SharedViewModelErrors.AtLeastOneFinalMethodReturnedError
        val actualError = sharedViewModel.error
        assertEquals(
            expectedError,
            actualError
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData
        val actualSharedViewModelViewState = sharedViewModel.uiState.value
        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }
    }


    @Test
    fun `init - repository's needToResetPastWeekRegularity() returns false, SharedViewModelViewState is LoadedSuccessfully`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns false

        coEvery { userRepository.fetchUsersLicense() } returns mockk()
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mockk()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedError = null
        val actualError = sharedViewModel.error
        assertEquals(
            expectedError,
            actualError
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.LoadedSuccessfully::class
        val actualSharedViewModelViewState = sharedViewModel.uiState.value::class
        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }

        coVerify(exactly = 0) {

            userRepository.resetWeeklyRegularity(any())
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }
    }

    @Test
    fun `init - repository's needToResetPastWeekRegularity() returns false, fetchUsersLicense() returns null, SharedViewModelViewState is FailedToLoadData`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns false

        coEvery { userRepository.fetchUsersLicense() } returns null
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mockk()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedError = SharedViewModelErrors.AtLeastOneFinalMethodReturnedError
        val actualError = sharedViewModel.error
        assertEquals(
            expectedError,
            actualError
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData
        val actualSharedViewModelViewState = sharedViewModel.uiState.value
        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }

        coVerify(exactly = 0) {

            userRepository.resetWeeklyRegularity(any())
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }
    }

    @Test
    fun `init - repository's needToResetPastWeekRegularity() returns false, fetchTrackiesForToday() returns null, SharedViewModelViewState is FailedToLoadData`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns false

        coEvery { userRepository.fetchUsersLicense() } returns mockk()
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns null
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mockk()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedError = SharedViewModelErrors.AtLeastOneFinalMethodReturnedError
        val actualError = sharedViewModel.error
        assertEquals(
            expectedError,
            actualError
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData
        val actualSharedViewModelViewState = sharedViewModel.uiState.value
        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }

        coVerify(exactly = 0) {

            userRepository.resetWeeklyRegularity(any())
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }
    }

    @Test
    fun `init - repository's needToResetPastWeekRegularity() returns false, fetchStatesOfTrackiesForToday() returns null, SharedViewModelViewState is FailedToLoadData`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns false

        coEvery { userRepository.fetchUsersLicense() } returns mockk()
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns null
        coEvery { userRepository.fetchWeeklyRegularity() } returns mockk()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedError = SharedViewModelErrors.AtLeastOneFinalMethodReturnedError
        val actualError = sharedViewModel.error
        assertEquals(
            expectedError,
            actualError
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData
        val actualSharedViewModelViewState = sharedViewModel.uiState.value
        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }

        coVerify(exactly = 0) {

            userRepository.resetWeeklyRegularity(any())
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }
    }

    @Test
    fun `init - repository's needToResetPastWeekRegularity() returns false, fetchWeeklyRegularity() returns null, SharedViewModelViewState is FailedToLoadData`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns false

        coEvery { userRepository.fetchUsersLicense() } returns mockk()
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchWeeklyRegularity() } returns null

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedError = SharedViewModelErrors.AtLeastOneFinalMethodReturnedError
        val actualError = sharedViewModel.error
        assertEquals(
            expectedError,
            actualError
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData
        val actualSharedViewModelViewState = sharedViewModel.uiState.value
        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }

        coVerify(exactly = 0) {

            userRepository.resetWeeklyRegularity(any())
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }
    }

    @Test
    fun `init - repository's needToResetPastWeekRegularity() returns false, all final methods return null, SharedViewModelViewState is FailedToLoadData`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns false

        coEvery { userRepository.fetchUsersLicense() } returns null
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns null
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns null
        coEvery { userRepository.fetchWeeklyRegularity() } returns null

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedError = SharedViewModelErrors.AtLeastOneFinalMethodReturnedError
        val actualError = sharedViewModel.error
        assertEquals(
            expectedError,
            actualError
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData
        val actualSharedViewModelViewState = sharedViewModel.uiState.value
        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
        }

        coVerify(exactly = 0) {

            userRepository.resetWeeklyRegularity(any())
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
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