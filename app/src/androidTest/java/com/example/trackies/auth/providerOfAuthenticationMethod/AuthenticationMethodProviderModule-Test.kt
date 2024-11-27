package com.example.trackies.auth.providerOfAuthenticationMethod

import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.auth.data.AuthenticationService
import com.example.trackies.auth.data.FirebaseAuthenticationService
import com.example.trackies.auth.data.RoomAuthenticationService
import com.example.trackies.di.FirebaseAuthenticator
import com.example.trackies.di.RoomAuthenticator
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidTest
@UninstallModules(AuthenticationMethodProviderModule::class)
class AuthenticationMethodProviderModuleTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @FirebaseAuthenticator
    lateinit var firebaseAuthenticationService: AuthenticationService

    @Inject
    @RoomAuthenticator
    lateinit var roomAuthenticationService: AuthenticationService

    @Inject
    lateinit var authenticationMethodProvider: AuthenticationMethodProvider

    @Test
    fun allDependenciesGetProvidedProperly() = runBlocking {

        assertNotNull(firebaseAuthenticationService)

        assertNotNull(roomAuthenticationService)

        assertNotNull(authenticationMethodProvider)
    }


    @Module
    @InstallIn(SingletonComponent::class)
    class AuthenticationMethodProviderModule {

        @Provides
        @FirebaseAuthenticator
        fun provideFirebaseAuthenticationService(): AuthenticationService =
            FirebaseAuthenticationService


        @Provides
        @RoomAuthenticator
        fun provideRoomAuthenticationService(
            lazyRoomDatabase: Lazy<RoomDatabase>
        ): AuthenticationService {

            val roomDatabase = lazyRoomDatabase.get()

            return RoomAuthenticationService(
                roomDatabase = roomDatabase
            )
        }

        @Provides
        @Singleton
        fun provideAuthenticationMethodProvider(
            @FirebaseAuthenticator firebaseAuthenticationService: AuthenticationService,
            @RoomAuthenticator roomAuthenticationService: AuthenticationService,
        ): AuthenticationMethodProvider =
            AuthenticationMethodProvider(
                firebaseAuthService = firebaseAuthenticationService,
                roomAuthService = roomAuthenticationService
            )
    }
}