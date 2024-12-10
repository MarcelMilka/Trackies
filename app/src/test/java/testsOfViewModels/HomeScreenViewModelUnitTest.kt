package testsOfViewModels

import MainCoroutineRule
import com.example.trackies.isSignedIn.homeScreen.buisness.HeightOfLazyColumn
import com.example.trackies.isSignedIn.homeScreen.buisness.HomeScreenChartToDisplay
import com.example.trackies.isSignedIn.homeScreen.viewModel.HomeScreenViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import javax.inject.Inject

@RunWith(JUnit4::class)
class HomeScreenViewModelUnitTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Inject
    lateinit var homeScreenViewModel: HomeScreenViewModel

    @Before
    fun setup() {

        homeScreenViewModel = HomeScreenViewModel()
    }

    @Test
    fun updateHeightOfLazyColumn_totalAmountOfTrackiesForTodayIs_0() = runTest {

        homeScreenViewModel.updateHeightOfLazyColumn(
            totalAmountOfTrackiesForToday = 0
        )

        val expectedValue = HeightOfLazyColumn.noTrackies
        val actualValue =
            homeScreenViewModel.uiState.value.heightOfLazyColumn

        assertEquals(
            expectedValue,
            actualValue
        )
    }

    @Test
    fun updateHeightOfLazyColumn_totalAmountOfTrackiesForTodayIs_1() = runTest {

        homeScreenViewModel.updateHeightOfLazyColumn(
            totalAmountOfTrackiesForToday = 1
        )

        val expectedValue = HeightOfLazyColumn.oneTrackie
        val actualValue =
            homeScreenViewModel.uiState.value.heightOfLazyColumn

        assertEquals(
            expectedValue,
            actualValue
        )
    }

    @Test
    fun updateHeightOfLazyColumn_totalAmountOfTrackiesForTodayIs_2() = runTest {

        homeScreenViewModel.updateHeightOfLazyColumn(
            totalAmountOfTrackiesForToday = 2
        )

        val expectedValue = HeightOfLazyColumn.twoTrackies
        val actualValue =
            homeScreenViewModel.uiState.value.heightOfLazyColumn

        assertEquals(
            expectedValue,
            actualValue
        )
    }

    @Test
    fun updateHeightOfLazyColumn_totalAmountOfTrackiesForTodayIs_3() = runTest {

        homeScreenViewModel.updateHeightOfLazyColumn(
            totalAmountOfTrackiesForToday = 3
        )

        val expectedValue = HeightOfLazyColumn.threeTrackies
        val actualValue =
            homeScreenViewModel.uiState.value.heightOfLazyColumn

        assertEquals(
            expectedValue,
            actualValue
        )
    }

    @Test
    fun updateHeightOfLazyColumn_totalAmountOfTrackiesForTodayIs_moreThanThree() = runTest {

        homeScreenViewModel.updateHeightOfLazyColumn(
            totalAmountOfTrackiesForToday = 4
        )

        val expectedValue = HeightOfLazyColumn.moreThanThreeTrackies
        val actualValue =
            homeScreenViewModel.uiState.value.heightOfLazyColumn

        assertEquals(
            expectedValue,
            actualValue
        )
    }


    @Test
    fun updateTypeOfHomeScreenChart_switchingBetweenChartsWorksProperly() = runTest {

        val expected1 = HomeScreenChartToDisplay.Weekly
        val actual1 = homeScreenViewModel.uiState.value.typeOfHomeScreenChart
        assertEquals(
            expected1,
            actual1
        )

        homeScreenViewModel.updateTypeOfHomeScreenChart(homeScreenChartToDisplay = HomeScreenChartToDisplay.Monthly)

        val expected2 = HomeScreenChartToDisplay.Monthly
        val actual2 = homeScreenViewModel.uiState.value.typeOfHomeScreenChart
        assertEquals(
            expected2,
            actual2
        )

        homeScreenViewModel.updateTypeOfHomeScreenChart(homeScreenChartToDisplay = HomeScreenChartToDisplay.Yearly)

        val expected3 = HomeScreenChartToDisplay.Yearly
        val actual3 = homeScreenViewModel.uiState.value.typeOfHomeScreenChart
        assertEquals(
            expected3,
            actual3
        )

        homeScreenViewModel.updateTypeOfHomeScreenChart(homeScreenChartToDisplay = HomeScreenChartToDisplay.Weekly)

        val expected4 = HomeScreenChartToDisplay.Weekly
        val actual4 = homeScreenViewModel.uiState.value.typeOfHomeScreenChart
        assertEquals(
            expected4,
            actual4
        )

        homeScreenViewModel.updateTypeOfHomeScreenChart(homeScreenChartToDisplay = HomeScreenChartToDisplay.Yearly)

        val expected5 = HomeScreenChartToDisplay.Yearly
        val actual5 = homeScreenViewModel.uiState.value.typeOfHomeScreenChart
        assertEquals(
            expected5,
            actual5
        )
    }
}