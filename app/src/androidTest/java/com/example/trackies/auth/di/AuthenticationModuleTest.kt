package com.example.trackies.auth.di

import android.util.Log
import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.auth.buisness.AuthenticationMethod
import com.example.trackies.auth.data.AuthenticationService
import com.example.trackies.auth.data.FirebaseAuthenticationService
import com.example.trackies.auth.data.RoomAuthenticationService
import com.example.trackies.auth.providerOfAuthenticationMethod.AuthenticationMethodProvider
import com.example.trackies.auth.providerOfAuthenticationMethod.AuthenticationMethodProviderModule
import com.example.trackies.di.FirebaseAuthenticator
import com.example.trackies.di.Named
import com.example.trackies.di.RoomAuthenticator
import com.example.trackies.isSignedIn.user.data.FirebaseUserRepository
import com.example.trackies.isSignedIn.user.data.RoomUserRepository
import com.example.trackies.isSignedIn.user.data.UserRepository
import com.example.trackies.isSignedIn.user.di.UserRepositoryModule
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertSame
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidTest
@UninstallModules(
    AuthenticationMethodProviderModule::class,
    AuthenticationModule::class,
    UserRepositoryModule::class
)
class AuthenticationModuleTest {

//  Setup:

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var authenticationMethodProvider: AuthenticationMethodProvider

    @Inject
    lateinit var authenticationService: AuthenticationService

    @Inject
    lateinit var userRepository: Lazy<UserRepository>

    @Before
    fun beforeTest() {

        hiltRule.inject()
    }
//  --------------------------------------------------------------------------------------------------------------------
//  Test part:
    @Test
    fun authenticationMethodProvider_getsProvidedCorrectly() {

        Log.d("AuthenticationModuleTest_logs", "$authenticationMethodProvider")
        assertNotNull(authenticationMethodProvider)
    }

    @Test
    fun x() {

        assertNotNull(authenticationMethodProvider)
        Log.d("AuthenticationModuleTest_logs", "x: authenticationMethodProvider = $authenticationMethodProvider")

        val defaultAuthenticationServiceIsFirebase =
            authenticationMethodProvider.getAuthenticationService()

        Log.d("AuthenticationModuleTest_logs", "x: defaultAuthenticationServiceIsFirebase = $defaultAuthenticationServiceIsFirebase")

        assertNotNull(defaultAuthenticationServiceIsFirebase)

        authenticationMethodProvider.setAuthenticationMethod(mode = AuthenticationMethod.Room)

        val roomAuthenticationService =
            authenticationMethodProvider.getAuthenticationService()

        Log.d("AuthenticationModuleTest_logs", "x: roomAuthenticationService = $roomAuthenticationService")

        assertNotNull(roomAuthenticationService)

        authenticationMethodProvider.setAuthenticationMethod(mode = AuthenticationMethod.Firebase)

        val firebaseAuthenticationService =
            authenticationMethodProvider.getAuthenticationService()

        Log.d("AuthenticationModuleTest_logs", "x: firebaseAuthenticationService = $firebaseAuthenticationService")

        assertNotNull(firebaseAuthenticationService)
    }

    @Test
    fun firebaseCase() {

//      *Authentication method is by default set to Firebase*

//      2: Authentication service is set to FirebaseAuthenticationService
        assertNotNull(authenticationService)
        Log.d("Hei!", "firebaseCase, authentication service: $authenticationService")

//      3: User repository  is set to FirebaseUserRepository
        val userRepository = userRepository.get()

        assertNotNull(userRepository)
        Log.d("Hei!", "firebaseCase, user repository: $userRepository")
    }

    @Test
    fun roomCase() = runBlocking{

//      1: Set authentication method to Room
        authenticationMethodProvider.setAuthenticationMethod(mode = AuthenticationMethod.Room)

//      2: Authentication service is set to RoomAuthenticationService
        assertNotNull(authenticationService)
        Log.d("Hei!", "roomCase, authentication service: $authenticationService")

//      3: User repository  is set to RoomUserRepository
        val userRepository = userRepository.get()

        Log.d("Hei!", "roomCase, user repository: $userRepository")
        assertNotNull(userRepository)
    }
//  roomCase, authentication service: com.example.trackies.auth.data.FirebaseAuthenticationService@ff70dde
//  roomCase, user repository: com.example.trackies.isSignedIn.user.data.RoomUserRepository@f4f9247

//  --------------------------------------------------------------------------------------------------------------------
//  Tested modules:

    @Module
    @InstallIn(SingletonComponent::class)
    class Test_AuthenticationMethodProviderModule {

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

//  Not ok
    @Module()
    @InstallIn(SingletonComponent::class)
    class Test_AuthenticationModule {

        @Provides
        fun provideAuthenticationService(
            authenticationMethodProvider: AuthenticationMethodProvider
        ): AuthenticationService {

            val authenticationService =
                authenticationMethodProvider.getAuthenticationService()

            Log.d("Gibbs!", "$authenticationService")

            return authenticationService
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class Test_UserRepositoryModule {

        @Provides
        @Named("uniqueIdentifier")
        fun provideUniqueIdentifier(): String? =
            FirebaseAuthenticationService.getSignedInUser()

        @Provides
        @Reusable
        fun provideUserRepository(
            authenticationMethodProvider: AuthenticationMethodProvider,
            @Named("uniqueIdentifier") lazyUniqueIdentifier: Lazy<String>,
            lazyRoomDatabase: Lazy<RoomDatabase>
        ): UserRepository {

            val authenticationMethod =
                authenticationMethodProvider.getAuthenticationMethod()

            Log.d("Chuj dupa łopata", "$authenticationMethod")

            when(authenticationMethod) {

                AuthenticationMethod.Firebase -> {

                    Log.d("Chuj dupa łopata", "provide firebase auth method")

                    val uniqueIdentifier = lazyUniqueIdentifier.get()
                    return FirebaseUserRepository(uniqueIdentifier = "uniqueIdentifier")
                }

                AuthenticationMethod.Room -> {

                    Log.d("Chuj dupa łopata", "provide room auth method")

                    val roomDatabase = lazyRoomDatabase.get()
                    return RoomUserRepository(roomDatabase = roomDatabase)
                }
            }
        }
    }
}