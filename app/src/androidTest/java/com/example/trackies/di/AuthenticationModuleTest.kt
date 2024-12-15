package com.example.trackies.di

import com.example.trackies.auth.data.AuthenticationService
import com.example.trackies.auth.data.FirebaseAuthenticationService
import com.example.trackies.auth.di.AuthenticationServiceModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Singleton

@UninstallModules(AuthenticationServiceModule::class) // uninstalls the PRODUCTION module
@HiltAndroidTest // generates Hilt components for each test
class AuthenticationModuleTest {

    @get:Rule(order = 0) // a test class contains several rules, then it's worth to use the parameter "order" to specify order of reading rules
    var hiltRule = HiltAndroidRule(this) // manages the components' state and performs injection on test

//  Two lines below tell hilt what should get injected
    @Inject
    lateinit var testAuthenticationService: AuthenticationService

//  the function below injects all dependencies
    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun authenticationServiceIsProvidedCorrectly () {

//      reflection is used below, in order to compare classes, not an interface and a class
        assertEquals(
            FirebaseAuthenticationService::class,
            testAuthenticationService::class
        )
    }

//  A kind of mocked module
//  Once a production module gets uninstalled by @Uninstall, the module below gets used during the test
    @Module()
    @InstallIn(SingletonComponent::class)
    class TestAuthenticationModule {

        @Provides
        @Singleton
        fun provideAuthenticationService(): AuthenticationService = FirebaseAuthenticationService
    }
}