package testsOfViewModels

import com.example.globalConstants.DaysOfWeek
import com.example.globalConstants.MeasuringUnit
import com.example.trackies.isSignedIn.detailedTrackie.vm.DetailedTrackieViewModel
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DetailedTrackieViewModelUnitTest {

    lateinit var detailedTrackieViewModel: DetailedTrackieViewModel

    @Before
    fun setup() {

        detailedTrackieViewModel = DetailedTrackieViewModel()
    }

    @Test
    fun trackieModelIsNullByDefault() = runBlocking {

        assertNull(detailedTrackieViewModel.trackieModel.value)
    }

    @Test
    fun trackieModelSetAndChangedProperly() = runBlocking {

        assertNull(detailedTrackieViewModel.trackieModel.value)

//      Set weekend
        detailedTrackieViewModel.setTrackieToDisplayDetailsOf(X.weekendTrackieModel)
        assertTrue(detailedTrackieViewModel.trackieModel.value == X.weekendTrackieModel)

//      Set whole week
        detailedTrackieViewModel.setTrackieToDisplayDetailsOf(X.wholeWeekTrackieModel)
        assertTrue(detailedTrackieViewModel.trackieModel.value == X.wholeWeekTrackieModel)

//      Set friday
        detailedTrackieViewModel.setTrackieToDisplayDetailsOf(X.fridayTrackieMode)
        assertTrue(detailedTrackieViewModel.trackieModel.value == X.fridayTrackieMode)
    }
}

// container of TrackieModels
private object X {

    val wholeWeekTrackieModel = TrackieModel(
        name = "A",
        totalDose = 1,
        measuringUnit = MeasuringUnit.ml,
        repeatOn = listOf(DaysOfWeek.monday, DaysOfWeek.tuesday, DaysOfWeek.wednesday, DaysOfWeek.thursday, DaysOfWeek.friday, DaysOfWeek.saturday, DaysOfWeek.sunday),
        ingestionTime = null
    )

    val weekendTrackieModel = TrackieModel(
        name = "B",
        totalDose = 2,
        measuringUnit = MeasuringUnit.g,
        repeatOn = listOf(DaysOfWeek.saturday, DaysOfWeek.sunday),
        ingestionTime = null
    )

    val fridayTrackieMode = TrackieModel(
        name = "C",
        totalDose = 3,
        measuringUnit = MeasuringUnit.pcs,
        repeatOn = listOf(DaysOfWeek.friday),
        ingestionTime = null
    )
}