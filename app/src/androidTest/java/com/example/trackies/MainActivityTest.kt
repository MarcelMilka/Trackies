package com.example.trackies

import com.example.trackies.isSignedIn.user.data.FirebaseUserRepository
import com.example.trackies.isSignedIn.user.di.UserRepositoryModule
import com.example.trackies.isSignedIn.user.data.UserRepository
import com.example.trackies.isSignedIn.user.vm.SharedViewModel
import com.example.trackies.auth.data.AuthenticationService
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.components.SingletonComponent
import org.junit.runner.RunWith
import dagger.hilt.InstallIn
import javax.inject.Inject
import org.junit.Before
import dagger.Reusable
import dagger.Provides
import org.junit.Rule
import dagger.Module

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

    @Before
    fun setUp() {
        hiltRule.inject()
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