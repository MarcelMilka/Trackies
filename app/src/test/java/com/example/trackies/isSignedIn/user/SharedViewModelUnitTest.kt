package com.example.trackies.isSignedIn.user

import com.example.globalConstants.DaysOfWeek
import com.example.trackies.isSignedIn.user.data.UserRepository
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
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