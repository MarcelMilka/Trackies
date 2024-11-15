package com.example.trackies

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackies.auth.data.AuthenticationService
import com.example.trackies.isSignedIn.addNewTrackie.vm.AddNewTrackieViewModel
import com.example.trackies.isSignedIn.user.data.FirebaseUserRepository
import com.example.trackies.isSignedIn.user.data.UserRepository
import com.example.trackies.isSignedIn.user.di.UserRepositoryModule
import com.example.trackies.isSignedIn.user.vm.SharedViewModel
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(UserRepositoryModule::class)
class MainActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var authenticationService: AuthenticationService

    @Inject
    lateinit var sharedViewModel: SharedViewModel

    @Inject
    lateinit var addNewTrackieViewModel: AddNewTrackieViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun mainActivityLaunchesSuccessfully() {

//        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
//
//            scenario.onActivity { activity ->
//                assertNotNull(activity.authenticationService)
//                assertNotNull(activity.lazySharedViewModel.get())
//                assertNotNull(activity.lazyAddNewTrackieViewModel.get())
//            }
//        }
    }


    @Module
    @InstallIn(SingletonComponent::class)
    class UserRepositoryModule {

        @Provides
        @Reusable
        fun provideUserRepository(): UserRepository =
            FirebaseUserRepository(uniqueIdentifier = "rOZfHWNOP9W6b0u1hO6NP3wndIw2")
    }
}