package testsOfViewModels

import com.example.trackies.isSignedIn.allTrackies.buisness.ListOfTrackiesToDisplay
import com.example.trackies.isSignedIn.allTrackies.vm.AllTrackiesViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AllTrackiesViewModelUnitTest {

    lateinit var allTrackiesViewModel: AllTrackiesViewModel

    @Before
    fun setup() {

        allTrackiesViewModel = AllTrackiesViewModel()
    }

    @Test
    fun `switchListToDisplay() properly switches list of Trackies to display`() = runTest {

        val expected1 = ListOfTrackiesToDisplay.Today
        val actual1 = allTrackiesViewModel.listToDisplay.value
        assertEquals(expected1, actual1)

        allTrackiesViewModel.switchListToDisplay(ListOfTrackiesToDisplay.WholeWeek)

        val expected2 = ListOfTrackiesToDisplay.WholeWeek
        val actual2 = allTrackiesViewModel.listToDisplay.value
        assertEquals(expected2, actual2)

        allTrackiesViewModel.switchListToDisplay(ListOfTrackiesToDisplay.Today)

        val expected3 = ListOfTrackiesToDisplay.Today
        val actual3 = allTrackiesViewModel.listToDisplay.value
        assertEquals(expected3, actual3)
    }
}