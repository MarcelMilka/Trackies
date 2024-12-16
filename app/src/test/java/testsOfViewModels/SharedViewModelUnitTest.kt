package testsOfViewModels

import MainCoroutineRule
import com.example.globalConstants.DaysOfWeek
import com.example.globalConstants.MeasuringUnit
import com.example.trackies.isSignedIn.user.buisness.LicenseModel
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelErrors
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

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
        measuringUnit = MeasuringUnit.ml,
        repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday),
        ingestionTime = null
    )

    private val mondayToFridayTrackieModel = TrackieModel(
        name = "B",
        totalDose = 1,
        measuringUnit = MeasuringUnit.pcs,
        repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday),
        ingestionTime = null
    )

    private val mondayTrackieModel = TrackieModel(
        name = "C",
        totalDose = 1,
        measuringUnit = MeasuringUnit.g,
        repeatOn = listOf(DaysOfWeek.monday),
        ingestionTime = null
    )

    private val saturdaySundayTrackieModel = TrackieModel(
        name = "D",
        totalDose = 400,
        measuringUnit = MeasuringUnit.ml,
        repeatOn = listOf(DaysOfWeek.saturday, DaysOfWeek.sunday),
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
        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData(
            errorMessage = SharedViewModelErrors.isFirstTimeInTheAppReturnedNull
        )
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
        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData(
            errorMessage = SharedViewModelErrors.needToResetPastWeekRegularityReturnedNull
        )
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
        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData(
            errorMessage = SharedViewModelErrors.resetWeeklyRegularityReturnedNull
        )
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
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns mockk()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
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
            userRepository.fetchNamesOfAllTrackies()
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
            userRepository.fetchNamesOfAllTrackies()
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
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns mockk()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData(
            errorMessage = SharedViewModelErrors.atLeastOneFinalMethodReturnedNull
        )
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
            userRepository.fetchNamesOfAllTrackies()
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
            userRepository.fetchNamesOfAllTrackies()
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
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns mockk()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData(
            errorMessage = SharedViewModelErrors.atLeastOneFinalMethodReturnedNull
        )
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
            userRepository.fetchNamesOfAllTrackies()
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
            userRepository.fetchNamesOfAllTrackies()
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
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns mockk()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData(
            errorMessage = SharedViewModelErrors.atLeastOneFinalMethodReturnedNull
        )
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
            userRepository.fetchNamesOfAllTrackies()
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
            userRepository.fetchNamesOfAllTrackies()
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
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns mockk()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData(
            errorMessage = SharedViewModelErrors.atLeastOneFinalMethodReturnedNull
        )
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
            userRepository.fetchNamesOfAllTrackies()
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
            userRepository.fetchNamesOfAllTrackies()
        }
    }

    @Test
    fun `init - repository's needToResetPastWeekRegularity() returns true, repository's resetWeeklyRegularity() returns true, fetchNamesOfAllTrackies() returns null, SharedViewModelViewState is FailedToLoadData`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns true
        coEvery { userRepository.resetWeeklyRegularity(any()) } returns true

        coEvery { userRepository.fetchUsersLicense() } returns mockk()
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mockk()
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns null

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData(
            errorMessage = SharedViewModelErrors.atLeastOneFinalMethodReturnedNull
        )
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
            userRepository.fetchNamesOfAllTrackies()
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
            userRepository.fetchNamesOfAllTrackies()
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
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns null

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData(
            errorMessage = SharedViewModelErrors.atLeastOneFinalMethodReturnedNull
        )
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
            userRepository.fetchNamesOfAllTrackies()
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())
            userRepository.resetWeeklyRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
            userRepository.fetchNamesOfAllTrackies()
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
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns mockk()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
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
            userRepository.fetchNamesOfAllTrackies()
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
            userRepository.fetchNamesOfAllTrackies()
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
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns mockk()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData(
            errorMessage = SharedViewModelErrors.atLeastOneFinalMethodReturnedNull
        )
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
            userRepository.fetchNamesOfAllTrackies()
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
            userRepository.fetchNamesOfAllTrackies()
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
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns mockk()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData(
            errorMessage = SharedViewModelErrors.atLeastOneFinalMethodReturnedNull
        )
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
            userRepository.fetchNamesOfAllTrackies()
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
            userRepository.fetchNamesOfAllTrackies()
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
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns mockk()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData(
            errorMessage = SharedViewModelErrors.atLeastOneFinalMethodReturnedNull
        )
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
            userRepository.fetchNamesOfAllTrackies()
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
            userRepository.fetchNamesOfAllTrackies()
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
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns mockk()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData(
            errorMessage = SharedViewModelErrors.atLeastOneFinalMethodReturnedNull
        )
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
            userRepository.fetchNamesOfAllTrackies()
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
            userRepository.fetchNamesOfAllTrackies()
        }
    }

    @Test
    fun `init - repository's needToResetPastWeekRegularity() returns false, fetchFetchNamesOfAllTrackies() returns null, SharedViewModelViewState is FailedToLoadData`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns false

        coEvery { userRepository.fetchUsersLicense() } returns mockk()
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mockk()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mockk()
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns null

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData(
            errorMessage = SharedViewModelErrors.atLeastOneFinalMethodReturnedNull
        )
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
            userRepository.fetchNamesOfAllTrackies()
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
            userRepository.fetchNamesOfAllTrackies()
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
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns null

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Waiting for all tasks to complete:
        advanceUntilIdle()

//      Assertions:
        val expectedSharedViewModelViewState = SharedViewModelViewState.FailedToLoadData(
            errorMessage = SharedViewModelErrors.atLeastOneFinalMethodReturnedNull
        )
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
            userRepository.fetchNamesOfAllTrackies()
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
            userRepository.fetchNamesOfAllTrackies()
        }
    }


    @Test
    fun `addNewTrackie() - repository's addNewTrackie returns false, onFailedToAddNewTrackie is called`() = runTest {

//      Preparation:
        coEvery { userRepository.addNewTrackie(wholeWeekTrackieModel) } returns false

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Assertions:
        val expectedOnFailedToAddNewTrackieMessage = "onFailedToAddNewTrackie"
        var actualOnFailedToAddNewTrackie = ""
        sharedViewModel.addNewTrackie(
            trackieModel = wholeWeekTrackieModel,
            currentDayOfWeek = DaysOfWeek.wednesday,
            onFailedToAddNewTrackie = {

                actualOnFailedToAddNewTrackie = "onFailedToAddNewTrackie"
            }
        )

        assertEquals(
            expectedOnFailedToAddNewTrackieMessage,
            actualOnFailedToAddNewTrackie
        )

//      Verifications:
        coVerify(exactly = 1) {

            sharedViewModel.addNewTrackie(
                trackieModel = wholeWeekTrackieModel,
                currentDayOfWeek = DaysOfWeek.wednesday,
                onFailedToAddNewTrackie = {

                    actualOnFailedToAddNewTrackie = "onFailedToAddNewTrackie"
                }
            )
        }
    }

    @Test
    fun `addNewTrackie() v1 - repository's addNewTrackie returns true, Trackie is added properly`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns false

        coEvery { userRepository.fetchUsersLicense() } returns LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 0)
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns listOf<TrackieModel>()
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mapOf<String, Boolean>()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mapOf<String, Map<Int, Int>>(
            DaysOfWeek.monday to mapOf(0 to 0),
            DaysOfWeek.tuesday to mapOf(0 to 0),
            DaysOfWeek.wednesday to mapOf(0 to 0),
            DaysOfWeek.thursday to mapOf(0 to 0),
            DaysOfWeek.friday to mapOf(0 to 0),
            DaysOfWeek.saturday to mapOf(0 to 0),
            DaysOfWeek.sunday to mapOf(0 to 0)
        )
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns listOf<String>()

        coEvery { userRepository.addNewTrackie(wholeWeekTrackieModel) } returns true

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Assertions:
        val expectedOnFailedToAddNewTrackieMessage = ""
        var actualOnFailedToAddNewTrackie = ""
        sharedViewModel.addNewTrackie(
            trackieModel = wholeWeekTrackieModel,
            currentDayOfWeek = DaysOfWeek.wednesday,
            onFailedToAddNewTrackie = {

                actualOnFailedToAddNewTrackie = "onFailedToAddNewTrackie"
            }
        )

        advanceUntilIdle()

        assertEquals(
            expectedOnFailedToAddNewTrackieMessage,
            actualOnFailedToAddNewTrackie
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 1),
            trackiesForToday = listOf(wholeWeekTrackieModel),
            statesOfTrackiesForToday = mapOf<String, Boolean>("A" to false),
            weeklyRegularity = mapOf<String, Map<Int, Int>>(
                DaysOfWeek.monday to mapOf(1 to 0),
                DaysOfWeek.tuesday to mapOf(1 to 0),
                DaysOfWeek.wednesday to mapOf(1 to 0),
                DaysOfWeek.thursday to mapOf(1 to 0),
                DaysOfWeek.friday to mapOf(1 to 0),
                DaysOfWeek.saturday to mapOf(1 to 0),
                DaysOfWeek.sunday to mapOf(1 to 0),
            ),
            namesOfAllTrackies = listOf("A"),
            allTrackies = null
        )
        val actualSharedViewModelViewState = sharedViewModel.uiState.value

        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            sharedViewModel.addNewTrackie(
                trackieModel = wholeWeekTrackieModel,
                currentDayOfWeek = DaysOfWeek.wednesday,
                onFailedToAddNewTrackie = {

                    actualOnFailedToAddNewTrackie = "onFailedToAddNewTrackie"
                }
            )
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
            userRepository.fetchNamesOfAllTrackies()

            userRepository.addNewTrackie(wholeWeekTrackieModel)
        }
    }

    @Test
    fun `addNewTrackie() v2 - repository's addNewTrackie returns true, Trackie is added properly`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns false

        coEvery { userRepository.fetchUsersLicense() } returns LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 1)
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns listOf<TrackieModel>(wholeWeekTrackieModel)
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mapOf<String, Boolean>("A" to false)
        coEvery { userRepository.fetchWeeklyRegularity() } returns mapOf<String, Map<Int, Int>>(
            DaysOfWeek.monday to mapOf(1 to 0),
            DaysOfWeek.tuesday to mapOf(1 to 0),
            DaysOfWeek.wednesday to mapOf(1 to 0),
            DaysOfWeek.thursday to mapOf(1 to 0),
            DaysOfWeek.friday to mapOf(1 to 0),
            DaysOfWeek.saturday to mapOf(1 to 0),
            DaysOfWeek.sunday to mapOf(1 to 0)
        )
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns listOf("A")

        coEvery { userRepository.addNewTrackie(mondayTrackieModel) } returns true

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Assertions:
        val expectedOnFailedToAddNewTrackieMessage = ""
        var actualOnFailedToAddNewTrackie = ""
        sharedViewModel.addNewTrackie(
            trackieModel = mondayTrackieModel,
            currentDayOfWeek = DaysOfWeek.wednesday,
            onFailedToAddNewTrackie = {

                actualOnFailedToAddNewTrackie = "onFailedToAddNewTrackie"
            }
        )

        advanceUntilIdle()

        assertEquals(
            expectedOnFailedToAddNewTrackieMessage,
            actualOnFailedToAddNewTrackie
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 2),
            trackiesForToday = listOf(wholeWeekTrackieModel),
            statesOfTrackiesForToday = mapOf<String, Boolean>("A" to false),
            weeklyRegularity = mapOf<String, Map<Int, Int>>(
                DaysOfWeek.monday to mapOf(2 to 0),
                DaysOfWeek.tuesday to mapOf(1 to 0),
                DaysOfWeek.wednesday to mapOf(1 to 0),
                DaysOfWeek.thursday to mapOf(1 to 0),
                DaysOfWeek.friday to mapOf(1 to 0),
                DaysOfWeek.saturday to mapOf(1 to 0),
                DaysOfWeek.sunday to mapOf(1 to 0),
            ),
            namesOfAllTrackies = listOf("A", "C"),
            allTrackies = null
        )
        val actualSharedViewModelViewState = sharedViewModel.uiState.value

        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            sharedViewModel.addNewTrackie(
                trackieModel = mondayTrackieModel,
                currentDayOfWeek = DaysOfWeek.wednesday,
                onFailedToAddNewTrackie = {

                    actualOnFailedToAddNewTrackie = "onFailedToAddNewTrackie"
                }
            )
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
            userRepository.fetchNamesOfAllTrackies()

            userRepository.addNewTrackie(mondayTrackieModel)
        }
    }

    @Test
    fun `addNewTrackie() - list of all trackies is not null, repository's addNewTrackie returns true, list of all trackies is updated properly`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns false

        coEvery { userRepository.fetchUsersLicense() } returns LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 2)
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns listOf<TrackieModel>(wholeWeekTrackieModel, mondayToFridayTrackieModel)
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mapOf<String, Boolean>("A" to false, "B" to false)
        coEvery { userRepository.fetchWeeklyRegularity() } returns mapOf<String, Map<Int, Int>>(
            DaysOfWeek.monday to mapOf(2 to 0),
            DaysOfWeek.tuesday to mapOf(2 to 0),
            DaysOfWeek.wednesday to mapOf(2 to 0),
            DaysOfWeek.thursday to mapOf(2 to 0),
            DaysOfWeek.friday to mapOf(2 to 0),
            DaysOfWeek.saturday to mapOf(1 to 0),
            DaysOfWeek.sunday to mapOf(1 to 0)
        )
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns listOf("A", "B")

        coEvery { userRepository.fetchAllTrackies() } returns listOf(wholeWeekTrackieModel, mondayToFridayTrackieModel)
        coEvery { userRepository.addNewTrackie(mondayTrackieModel) } returns true

        val sharedViewModel = SharedViewModel(repository = userRepository)

        sharedViewModel.fetchAllTrackies {  }

//      Assertions:
        val expectedSharedViewModelViewState1 = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 2),
            trackiesForToday = listOf(wholeWeekTrackieModel, mondayToFridayTrackieModel),
            statesOfTrackiesForToday = mapOf<String, Boolean>("A" to false, "B" to false),
            weeklyRegularity = mapOf<String, Map<Int, Int>>(
                DaysOfWeek.monday to mapOf(2 to 0),
                DaysOfWeek.tuesday to mapOf(2 to 0),
                DaysOfWeek.wednesday to mapOf(2 to 0),
                DaysOfWeek.thursday to mapOf(2 to 0),
                DaysOfWeek.friday to mapOf(2 to 0),
                DaysOfWeek.saturday to mapOf(1 to 0),
                DaysOfWeek.sunday to mapOf(1 to 0),
            ),
            namesOfAllTrackies = mutableListOf("A", "B"),
            allTrackies = listOf(wholeWeekTrackieModel, mondayToFridayTrackieModel)
        )
        val actualSharedViewModelViewState1 = sharedViewModel.uiState.value

        assertEquals(
            expectedSharedViewModelViewState1,
            actualSharedViewModelViewState1
        )

        val expectedOnFailedToAddNewTrackieMessage = ""
        var actualOnFailedToAddNewTrackie = ""
        sharedViewModel.addNewTrackie(
            trackieModel = mondayTrackieModel,
            currentDayOfWeek = DaysOfWeek.wednesday,
            onFailedToAddNewTrackie = {

                actualOnFailedToAddNewTrackie = "onFailedToAddNewTrackie"
            }
        )

        assertEquals(
            expectedOnFailedToAddNewTrackieMessage,
            actualOnFailedToAddNewTrackie
        )

        val expectedSharedViewModelViewState2 = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 3),
            trackiesForToday = listOf(wholeWeekTrackieModel, mondayToFridayTrackieModel),
            statesOfTrackiesForToday = mapOf<String, Boolean>("A" to false, "B" to false),
            weeklyRegularity = mapOf<String, Map<Int, Int>>(
                DaysOfWeek.monday to mapOf(3 to 0),
                DaysOfWeek.tuesday to mapOf(2 to 0),
                DaysOfWeek.wednesday to mapOf(2 to 0),
                DaysOfWeek.thursday to mapOf(2 to 0),
                DaysOfWeek.friday to mapOf(2 to 0),
                DaysOfWeek.saturday to mapOf(1 to 0),
                DaysOfWeek.sunday to mapOf(1 to 0),
            ),
            namesOfAllTrackies = mutableListOf("A", "B", "C"),
            allTrackies = listOf(wholeWeekTrackieModel, mondayToFridayTrackieModel, mondayTrackieModel)
        )
        val actualSharedViewModelViewState2 = sharedViewModel.uiState.value

        assertEquals(
            expectedSharedViewModelViewState2,
            actualSharedViewModelViewState2
        )

//      Verifications:
        coVerify(exactly = 1) {

            sharedViewModel.addNewTrackie(
                trackieModel = mondayTrackieModel,
                currentDayOfWeek = DaysOfWeek.wednesday,
                onFailedToAddNewTrackie = {

                    actualOnFailedToAddNewTrackie = "onFailedToAddNewTrackie"
                }
            )
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
            userRepository.fetchNamesOfAllTrackies()

            userRepository.fetchAllTrackies()
            userRepository.addNewTrackie(mondayTrackieModel)
        }
    }


    @Test
    fun `deleteTrackie() - repository's deleteTrackie returns false, onFailedToAddNewTrackie is called`() = runTest {

//      Preparation:
        coEvery { userRepository.deleteTrackie(wholeWeekTrackieModel) } returns false

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Assertions:
        val expectedOnFailedToAddNewTrackieMessage = "onFailedToDeleteTrackie"
        var actualOnFailedToAddNewTrackie = ""
        sharedViewModel.deleteTrackie(
            trackieModel = wholeWeekTrackieModel,
            currentDayOfWeek = DaysOfWeek.wednesday,
            onFailedToDeleteTrackie = {

                actualOnFailedToAddNewTrackie = "onFailedToDeleteTrackie"
            }
        )

        assertEquals(
            expectedOnFailedToAddNewTrackieMessage,
            actualOnFailedToAddNewTrackie
        )

//      Verifications:
        coVerify(exactly = 1) {

            sharedViewModel.deleteTrackie(
                trackieModel = wholeWeekTrackieModel,
                currentDayOfWeek = DaysOfWeek.wednesday,
                onFailedToDeleteTrackie = {

                    actualOnFailedToAddNewTrackie = "onFailedToDeleteTrackie"
                }
            )
        }
    }

    @Test
    fun `deleteTrackie() - repository's deleteTrackie returns true, TrackieIsDeletedProperly v1`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns false

        coEvery { userRepository.fetchUsersLicense() } returns LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 3)
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns listOf<TrackieModel>(wholeWeekTrackieModel, mondayToFridayTrackieModel)
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mapOf<String, Boolean>("A" to false, "B" to true)
        coEvery { userRepository.fetchWeeklyRegularity() } returns mapOf<String, Map<Int, Int>>(
            DaysOfWeek.monday to mapOf(3 to 1),
            DaysOfWeek.tuesday to mapOf(2 to 1),
            DaysOfWeek.wednesday to mapOf(2 to 1),
            DaysOfWeek.thursday to mapOf(2 to 1),
            DaysOfWeek.friday to mapOf(2 to 1),
            DaysOfWeek.saturday to mapOf(1 to 0),
            DaysOfWeek.sunday to mapOf(1 to 0)
        )
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns listOf("A", "B", "C")

        coEvery { userRepository.deleteTrackie(mondayTrackieModel) } returns true

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Assertions:
        val expectedOnFailedToAddNewTrackieMessage = ""
        var actualOnFailedToAddNewTrackie = ""
        sharedViewModel.deleteTrackie(
            trackieModel = mondayTrackieModel,
            currentDayOfWeek = DaysOfWeek.wednesday,
            onFailedToDeleteTrackie = {

                actualOnFailedToAddNewTrackie = "onFailedToDeleteTrackie"
            }
        )

        advanceUntilIdle()

        assertEquals(
            expectedOnFailedToAddNewTrackieMessage,
            actualOnFailedToAddNewTrackie
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 2),
            trackiesForToday = listOf(wholeWeekTrackieModel, mondayToFridayTrackieModel),
            statesOfTrackiesForToday = mapOf<String, Boolean>("A" to false, "B" to true),
            weeklyRegularity = mapOf<String, Map<Int, Int>>(
                DaysOfWeek.monday to mapOf(2 to 0),
                DaysOfWeek.tuesday to mapOf(2 to 1),
                DaysOfWeek.wednesday to mapOf(2 to 1),
                DaysOfWeek.thursday to mapOf(2 to 1),
                DaysOfWeek.friday to mapOf(2 to 1),
                DaysOfWeek.saturday to mapOf(1 to 0),
                DaysOfWeek.sunday to mapOf(1 to 0),
            ),
            namesOfAllTrackies = listOf("A", "B"),
            allTrackies = null
        )
        val actualSharedViewModelViewState = sharedViewModel.uiState.value

        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            sharedViewModel.deleteTrackie(
                trackieModel = mondayTrackieModel,
                currentDayOfWeek = DaysOfWeek.wednesday,
                onFailedToDeleteTrackie = {

                    actualOnFailedToAddNewTrackie = "onFailedToDeleteTrackie"
                }
            )
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
            userRepository.fetchNamesOfAllTrackies()

            userRepository.deleteTrackie(mondayTrackieModel)
        }
    }

    @Test
    fun `deleteTrackie() - repository's deleteTrackie returns true, TrackieIsDeletedProperly v2`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns false

        coEvery { userRepository.fetchUsersLicense() } returns LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 2)
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns listOf<TrackieModel>(wholeWeekTrackieModel, mondayToFridayTrackieModel)
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mapOf<String, Boolean>("A" to false, "B" to true)
        coEvery { userRepository.fetchWeeklyRegularity() } returns mapOf<String, Map<Int, Int>>(
            DaysOfWeek.monday to mapOf(2 to 0),
            DaysOfWeek.tuesday to mapOf(2 to 0),
            DaysOfWeek.wednesday to mapOf(2 to 0),
            DaysOfWeek.thursday to mapOf(2 to 0),
            DaysOfWeek.friday to mapOf(2 to 0),
            DaysOfWeek.saturday to mapOf(1 to 0),
            DaysOfWeek.sunday to mapOf(1 to 0)
        )
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns listOf("A", "B")

        coEvery { userRepository.deleteTrackie(mondayToFridayTrackieModel) } returns true

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Assertions:
        val expectedOnFailedToAddNewTrackieMessage = ""
        var actualOnFailedToAddNewTrackie = ""
        sharedViewModel.deleteTrackie(
            trackieModel = mondayToFridayTrackieModel,
            currentDayOfWeek = DaysOfWeek.wednesday,
            onFailedToDeleteTrackie = {

                actualOnFailedToAddNewTrackie = "onFailedToDeleteTrackie"
            }
        )

        advanceUntilIdle()

        assertEquals(
            expectedOnFailedToAddNewTrackieMessage,
            actualOnFailedToAddNewTrackie
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 1),
            trackiesForToday = listOf(wholeWeekTrackieModel),
            statesOfTrackiesForToday = mapOf<String, Boolean>("A" to false),
            weeklyRegularity = mapOf<String, Map<Int, Int>>(
                DaysOfWeek.monday to mapOf(1 to 0),
                DaysOfWeek.tuesday to mapOf(1 to 0),
                DaysOfWeek.wednesday to mapOf(1 to 0),
                DaysOfWeek.thursday to mapOf(1 to 0),
                DaysOfWeek.friday to mapOf(1 to 0),
                DaysOfWeek.saturday to mapOf(1 to 0),
                DaysOfWeek.sunday to mapOf(1 to 0),
            ),
            namesOfAllTrackies = listOf("A"),
            allTrackies = null
        )
        val actualSharedViewModelViewState = sharedViewModel.uiState.value

        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            sharedViewModel.deleteTrackie(
                trackieModel = mondayToFridayTrackieModel,
                currentDayOfWeek = DaysOfWeek.wednesday,
                onFailedToDeleteTrackie = {

                    actualOnFailedToAddNewTrackie = "onFailedToDeleteTrackie"
                }
            )
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
            userRepository.fetchNamesOfAllTrackies()

            userRepository.deleteTrackie(mondayToFridayTrackieModel)
        }
    }

    @Test
    fun `deleteTrackie() - repository's deleteTrackie returns true, TrackieIsDeletedProperly v3`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns false

        coEvery { userRepository.fetchUsersLicense() } returns LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 1)
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns listOf<TrackieModel>(wholeWeekTrackieModel)
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mapOf<String, Boolean>("A" to false)
        coEvery { userRepository.fetchWeeklyRegularity() } returns mapOf<String, Map<Int, Int>>(
            DaysOfWeek.monday to mapOf(1 to 0),
            DaysOfWeek.tuesday to mapOf(1 to 0),
            DaysOfWeek.wednesday to mapOf(1 to 0),
            DaysOfWeek.thursday to mapOf(1 to 0),
            DaysOfWeek.friday to mapOf(1 to 0),
            DaysOfWeek.saturday to mapOf(1 to 0),
            DaysOfWeek.sunday to mapOf(1 to 0)
        )
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns listOf("A")

        coEvery { userRepository.deleteTrackie(wholeWeekTrackieModel) } returns true

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Assertions:
        val expectedOnFailedToAddNewTrackieMessage = ""
        var actualOnFailedToAddNewTrackie = ""
        sharedViewModel.deleteTrackie(
            trackieModel = wholeWeekTrackieModel,
            currentDayOfWeek = DaysOfWeek.wednesday,
            onFailedToDeleteTrackie = {

                actualOnFailedToAddNewTrackie = "onFailedToDeleteTrackie"
            }
        )

        advanceUntilIdle()

        assertEquals(
            expectedOnFailedToAddNewTrackieMessage,
            actualOnFailedToAddNewTrackie
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 0),
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
            namesOfAllTrackies = listOf(),
            allTrackies = null
        )
        val actualSharedViewModelViewState = sharedViewModel.uiState.value

        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            sharedViewModel.deleteTrackie(
                trackieModel = wholeWeekTrackieModel,
                currentDayOfWeek = DaysOfWeek.wednesday,
                onFailedToDeleteTrackie = {

                    actualOnFailedToAddNewTrackie = "onFailedToDeleteTrackie"
                }
            )
        }

        coVerifySequence {

            userRepository.isFirstTimeInTheApp()
            userRepository.needToResetPastWeekRegularity(any())

            userRepository.fetchUsersLicense()
            userRepository.fetchTrackiesForToday(any())
            userRepository.fetchStatesOfTrackiesForToday(any())
            userRepository.fetchWeeklyRegularity()
            userRepository.fetchNamesOfAllTrackies()

            userRepository.deleteTrackie(wholeWeekTrackieModel)
        }
    }


    @Test
    fun `fetchListOfAllTrackies() - repository's fetchAllTrackies returns null, onFailedToFetchAllTrackies is called`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns false

        coEvery { userRepository.fetchUsersLicense() } returns LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 0)
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns listOf<TrackieModel>()
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mapOf<String, Boolean>()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mapOf<String, Map<Int, Int>>(
            DaysOfWeek.monday to mapOf(0 to 0),
            DaysOfWeek.tuesday to mapOf(0 to 0),
            DaysOfWeek.wednesday to mapOf(0 to 0),
            DaysOfWeek.thursday to mapOf(0 to 0),
            DaysOfWeek.friday to mapOf(0 to 0),
            DaysOfWeek.saturday to mapOf(0 to 0),
            DaysOfWeek.sunday to mapOf(0 to 0)
        )
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns listOf<String>()


        coEvery { userRepository.fetchAllTrackies() } returns null

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Assertions:
        val expectedOnFailedToFetchAllTrackiesMessage = "onFailedToFetchAllTrackies"
        var actualOnFailedToFetchAllTrackiesMessage = ""
        sharedViewModel.fetchAllTrackies {

            actualOnFailedToFetchAllTrackiesMessage = "onFailedToFetchAllTrackies"
        }

        assertEquals(
            expectedOnFailedToFetchAllTrackiesMessage,
            actualOnFailedToFetchAllTrackiesMessage
        )
    }

    @Test
    fun `fetchListOfAllTrackies() - repository's fetchAllTrackies returns empty list`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns false

        coEvery { userRepository.fetchUsersLicense() } returns LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 0)
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns listOf<TrackieModel>()
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mapOf<String, Boolean>()
        coEvery { userRepository.fetchWeeklyRegularity() } returns mapOf<String, Map<Int, Int>>(
            DaysOfWeek.monday to mapOf(0 to 0),
            DaysOfWeek.tuesday to mapOf(0 to 0),
            DaysOfWeek.wednesday to mapOf(0 to 0),
            DaysOfWeek.thursday to mapOf(0 to 0),
            DaysOfWeek.friday to mapOf(0 to 0),
            DaysOfWeek.saturday to mapOf(0 to 0),
            DaysOfWeek.sunday to mapOf(0 to 0)
        )
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns listOf<String>()

        coEvery { userRepository.fetchAllTrackies() } returns listOf<TrackieModel>()

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Assertions:
        val expectedOnFailedToFetchAllTrackiesMessage = ""
        var actualOnFailedToFetchAllTrackiesMessage = ""
        sharedViewModel.fetchAllTrackies {

            actualOnFailedToFetchAllTrackiesMessage = "onFailedToFetchAllTrackies"
        }

        assertEquals(
            expectedOnFailedToFetchAllTrackiesMessage,
            actualOnFailedToFetchAllTrackiesMessage
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 0),
            trackiesForToday = listOf<TrackieModel>(),
            statesOfTrackiesForToday = mapOf<String, Boolean>(),
            weeklyRegularity = mapOf<String, Map<Int, Int>>(
                DaysOfWeek.monday to mapOf(0 to 0),
                DaysOfWeek.tuesday to mapOf(0 to 0),
                DaysOfWeek.wednesday to mapOf(0 to 0),
                DaysOfWeek.thursday to mapOf(0 to 0),
                DaysOfWeek.friday to mapOf(0 to 0),
                DaysOfWeek.saturday to mapOf(0 to 0),
                DaysOfWeek.sunday to mapOf(0 to 0)
            ),
            namesOfAllTrackies = mutableListOf<String>(),
            allTrackies = listOf<TrackieModel>()
        )
        val actualSharedViewModelViewState = sharedViewModel.uiState.value

        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )
    }

    @Test
    fun `fetchListOfAllTrackies() - repository's fetchAllTrackies returns list of trackies, new trackie gets added, list of all trackies is updated`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns false

        coEvery { userRepository.fetchUsersLicense() } returns LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 3)
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns listOf<TrackieModel>(wholeWeekTrackieModel, mondayToFridayTrackieModel)
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mapOf<String, Boolean>("A" to true, "B" to false)
        coEvery { userRepository.fetchWeeklyRegularity() } returns mapOf<String, Map<Int, Int>>(
            DaysOfWeek.monday to mapOf(3 to 2),
            DaysOfWeek.tuesday to mapOf(2 to 2),
            DaysOfWeek.wednesday to mapOf(2 to 1),
            DaysOfWeek.thursday to mapOf(2 to 0),
            DaysOfWeek.friday to mapOf(2 to 0),
            DaysOfWeek.saturday to mapOf(1 to 0),
            DaysOfWeek.sunday to mapOf(1 to 0)
        )
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns listOf("A", "B", "C")

        coEvery { userRepository.fetchAllTrackies() } returns listOf<TrackieModel>(wholeWeekTrackieModel, mondayToFridayTrackieModel, mondayTrackieModel)
        coEvery { userRepository.addNewTrackie(saturdaySundayTrackieModel) } returns true

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Assertions:
        val expectedOnFailedToFetchAllTrackiesMessage = ""
        var actualOnFailedToFetchAllTrackiesMessage = ""
        sharedViewModel.fetchAllTrackies {

            actualOnFailedToFetchAllTrackiesMessage = "onFailedToFetchAllTrackies"
        }
        assertEquals(
            expectedOnFailedToFetchAllTrackiesMessage,
            actualOnFailedToFetchAllTrackiesMessage
        )

        val expectedSharedViewModelViewState1 = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 3),
            trackiesForToday = listOf<TrackieModel>(wholeWeekTrackieModel, mondayToFridayTrackieModel),
            statesOfTrackiesForToday = mapOf<String, Boolean>("A" to true, "B" to false),
            weeklyRegularity = mapOf<String, Map<Int, Int>>(
                DaysOfWeek.monday to mapOf(3 to 2),
                DaysOfWeek.tuesday to mapOf(2 to 2),
                DaysOfWeek.wednesday to mapOf(2 to 1),
                DaysOfWeek.thursday to mapOf(2 to 0),
                DaysOfWeek.friday to mapOf(2 to 0),
                DaysOfWeek.saturday to mapOf(1 to 0),
                DaysOfWeek.sunday to mapOf(1 to 0)
            ),
            namesOfAllTrackies = mutableListOf<String>("A", "B", "C"),
            allTrackies = listOf<TrackieModel>(wholeWeekTrackieModel, mondayToFridayTrackieModel, mondayTrackieModel)
        )
        val actualSharedViewModelViewState1 = sharedViewModel.uiState.value
        assertEquals(
            expectedSharedViewModelViewState1,
            actualSharedViewModelViewState1
        )

        sharedViewModel.addNewTrackie(
            trackieModel = saturdaySundayTrackieModel,
            currentDayOfWeek = DaysOfWeek.wednesday,
            onFailedToAddNewTrackie = {}
        )

        val expectedSharedViewModelViewState2 = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 4),
            trackiesForToday = listOf<TrackieModel>(wholeWeekTrackieModel, mondayToFridayTrackieModel),
            statesOfTrackiesForToday = mapOf<String, Boolean>("A" to true, "B" to false),
            weeklyRegularity = mapOf<String, Map<Int, Int>>(
                DaysOfWeek.monday to mapOf(3 to 2),
                DaysOfWeek.tuesday to mapOf(2 to 2),
                DaysOfWeek.wednesday to mapOf(2 to 1),
                DaysOfWeek.thursday to mapOf(2 to 0),
                DaysOfWeek.friday to mapOf(2 to 0),
                DaysOfWeek.saturday to mapOf(2 to 0),
                DaysOfWeek.sunday to mapOf(2 to 0)
            ),
            namesOfAllTrackies = mutableListOf<String>("A", "B", "C", "D"),
            allTrackies = listOf<TrackieModel>(wholeWeekTrackieModel, mondayToFridayTrackieModel, mondayTrackieModel, saturdaySundayTrackieModel)
        )
        val actualSharedViewModelViewState2 = sharedViewModel.uiState.value
        assertEquals(
            expectedSharedViewModelViewState2,
            actualSharedViewModelViewState2
        )
    }


    @Test
    fun `markTrackieAsIngested() - repository's markTrackieAsIngested() returns false, onFailedToMarkTrackieAsIngested is called`() = runTest {

//      Preparation:
        coEvery { userRepository.markTrackieAsIngested(any(), any()) } returns false
        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Assertions:
        val expectedOnFailedToMarkTrackieAsIngestedMessage = "onFailedToMarkTrackieAsIngested"
        var actualOnFailedToMarkTrackieAsIngestedMessage = ""
        sharedViewModel.markTrackieAsIngested(
            trackieModel = wholeWeekTrackieModel,
            currentDayOfWeek = DaysOfWeek.wednesday,
            onFailedToMarkTrackieAsIngested = {

                actualOnFailedToMarkTrackieAsIngestedMessage = "onFailedToMarkTrackieAsIngested"
            }
        )
        assertEquals(
            expectedOnFailedToMarkTrackieAsIngestedMessage,
            actualOnFailedToMarkTrackieAsIngestedMessage
        )

//      Verifications:
        coVerify(exactly = 1) {

            userRepository.markTrackieAsIngested(any(), any())
        }
    }

    @Test
    fun `markTrackieAsIngested() - repository's markTrackieAsIngested() returns true, SharedViewModelViewState is updated properly`() = runTest {

//      Preparation:
        coEvery { userRepository.isFirstTimeInTheApp() } returns true
        coEvery { userRepository.needToResetPastWeekRegularity(any()) } returns false

        coEvery { userRepository.fetchUsersLicense() } returns LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 2)
        coEvery { userRepository.fetchTrackiesForToday(any()) } returns listOf<TrackieModel>(wholeWeekTrackieModel, mondayToFridayTrackieModel)
        coEvery { userRepository.fetchStatesOfTrackiesForToday(any()) } returns mapOf<String, Boolean>("A" to true, "B" to false)
        coEvery { userRepository.fetchWeeklyRegularity() } returns mapOf<String, Map<Int, Int>>(
            DaysOfWeek.monday to mapOf(2 to 2),
            DaysOfWeek.tuesday to mapOf(2 to 2),
            DaysOfWeek.wednesday to mapOf(2 to 1),
            DaysOfWeek.thursday to mapOf(2 to 0),
            DaysOfWeek.friday to mapOf(2 to 0),
            DaysOfWeek.saturday to mapOf(1 to 0),
            DaysOfWeek.sunday to mapOf(1 to 0)
        )
        coEvery { userRepository.fetchNamesOfAllTrackies() } returns listOf("A", "B")

        coEvery { userRepository.markTrackieAsIngested(trackieModel = mondayToFridayTrackieModel, currentDayOfWeek = DaysOfWeek.wednesday) } returns true

        val sharedViewModel = SharedViewModel(repository = userRepository)

//      Assertions:
        val expectedOnFailedToMarkTrackieAsIngestedMessage = ""
        var actualOnFailedToMarkTrackieAsIngestedMessage = ""
        sharedViewModel.markTrackieAsIngested(
            trackieModel = mondayToFridayTrackieModel,
            currentDayOfWeek = DaysOfWeek.wednesday,
            onFailedToMarkTrackieAsIngested = {

                actualOnFailedToMarkTrackieAsIngestedMessage = "onFailedToMarkTrackieAsIngested"
            }
        )
        assertEquals(
            expectedOnFailedToMarkTrackieAsIngestedMessage,
            actualOnFailedToMarkTrackieAsIngestedMessage
        )

        val expectedSharedViewModelViewState = SharedViewModelViewState.LoadedSuccessfully(
            license = LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 2),
            trackiesForToday = listOf<TrackieModel>(wholeWeekTrackieModel, mondayToFridayTrackieModel),
            statesOfTrackiesForToday = mapOf<String, Boolean>("A" to true, "B" to true),
            weeklyRegularity = mapOf<String, Map<Int, Int>>(
                DaysOfWeek.monday to mapOf(2 to 2),
                DaysOfWeek.tuesday to mapOf(2 to 2),
                DaysOfWeek.wednesday to mapOf(2 to 2),
                DaysOfWeek.thursday to mapOf(2 to 0),
                DaysOfWeek.friday to mapOf(2 to 0),
                DaysOfWeek.saturday to mapOf(1 to 0),
                DaysOfWeek.sunday to mapOf(1 to 0)
            ),
            namesOfAllTrackies = listOf("A", "B"),
            allTrackies = null
        )
        val actualSharedViewModelViewState = sharedViewModel.uiState.value
        assertEquals(
            expectedSharedViewModelViewState,
            actualSharedViewModelViewState
        )

//      Verifications:
        coVerify(exactly = 1) {

            userRepository.markTrackieAsIngested(trackieModel = mondayToFridayTrackieModel, currentDayOfWeek = DaysOfWeek.wednesday)
        }
    }

    @Test
    fun x() {

        val namesOfAllTrackies = listOf("A", "B")

        fun updateNamesOfAllTrackies(): List<String> {

            var namesOfAllTrackies = namesOfAllTrackies.toMutableList()

            namesOfAllTrackies.add(element = "C")

            return namesOfAllTrackies
        }

        assertEquals(
            updateNamesOfAllTrackies(),
            listOf("A", "B", "C")
        )
    }
}