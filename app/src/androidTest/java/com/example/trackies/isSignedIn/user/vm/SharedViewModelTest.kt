package com.example.trackies.isSignedIn.user.vm

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackies.isSignedIn.user.buisness.SharedViewModelViewState
import com.example.trackies.isSignedIn.user.data.UserRepository
import com.example.trackies.isSignedIn.user.di.SharedViewModelModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.String
import kotlin.Unit


@Suppress("UNCHECKED_CAST")
@HiltAndroidTest
@UninstallModules(SharedViewModelModule::class)
@RunWith(AndroidJUnit4::class)
class SharedViewModelTest {

    @MockK
    lateinit var userRepository: UserRepository

    lateinit var sharedViewModel: SharedViewModel

    @Before
    fun setup() {

        MockKAnnotations.init(this, relaxed = true)

        sharedViewModel = SharedViewModel(repository = this.userRepository)
    }

//  templateFunction
    fun testedMethod_whatIsTested() = runTest {

//      1: specifying how specific mocked methods should behave:

//      2: specifying expected results
        val expected = ""
        val actual = ""

        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun init_whenIsFirstTimeInTheAppNull_setFailedToLoadData() = runTest {

//      1: specifying how specific mocked methods should behave:
        coEvery { userRepository.isFirstTimeInTheApp {  } } returns null

//      2: specifying expected results
        val expected = SharedViewModelViewState.FailedToLoadData
        val actual = sharedViewModel.uiState.value

        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun init_whenNeedToResetPastWeekActivityNull_setFailedToLoadData() = runTest {

//      1: specifying how specific mocked methods should behave:
        coEvery {
            userRepository.isFirstTimeInTheApp {  }
        } returns true
        coEvery {
            userRepository.needToResetPastWeekActivity(
                onSuccess = {},
                onFailure = {}
            )
        } returns null

//      2: specifying expected results
        val expected = SharedViewModelViewState.FailedToLoadData
        val actual = sharedViewModel.uiState.value

        assertEquals(
            expected,
            actual
        )
    }

    @Test
    fun init_whenResetWeeklyRegularity_setFailedToLoadData() = runTest {

//      1: specifying how specific mocked methods should behave:
        coEvery {
            userRepository.isFirstTimeInTheApp {  }
        } returns true

        val onSuccess = slot<() -> Unit>()
        val onFailure = slot<(String) -> Unit>()

        coEvery {
            userRepository.needToResetPastWeekActivity(
                capture(onSuccess),
                capture(onFailure),
            )
        }


//      2: specifying expected results
        val expected = SharedViewModelViewState.FailedToLoadData
        val actual = sharedViewModel.uiState.value

        assertEquals(
            expected,
            actual
        )
    }


    @Test
    fun playground() = runBlocking {

        val relaxedMockieBuddy = mockk<MockieBuddy>(relaxed = true)

        every {

            relaxedMockieBuddy
                .lambdaBoiii(
                    value = any(), // eq(5) in a different form, also, I can use any()
                    onCall = any() // "I don't care what is passed here; just match any value of this type."
                )
        } answers {

//          val lambda = args[1] as (Int) -> Unit
//          lambda.invoke(125)

//          or:

            secondArg<(Int) -> Unit>().invoke(125)
        }

        val expected = 125
        var actual = 0

        relaxedMockieBuddy.lambdaBoiii(
            value = 5,
            onCall = {

                actual = it
            }
        )

        assertEquals(expected, actual)

        verify(exactly = 1) {

            relaxedMockieBuddy.lambdaBoiii(value = any(), onCall = any())
        }
    }
}

private class MockieBuddy {

    fun lambdaBoiii (value: Int, onCall: (Int) -> Unit) {

        if (value >= 5) {

            onCall(value * value)
        }
    }
}