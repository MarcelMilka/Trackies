package com.example.trackies.isSignedIn.user.di

import android.content.Context
import androidx.room.Room
import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.auth.buisness.AuthenticationMethod
import com.example.trackies.di.Named
import com.example.trackies.isSignedIn.user.data.FirebaseUserRepository
import com.example.trackies.isSignedIn.user.data.RoomUserRepository
import com.example.trackies.isSignedIn.user.data.UserRepository
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

//@HiltAndroidTest // generates Hilt components for each test.
//@UninstallModules(UserRepositoryModule::class)
//class UserRepositoryModuleTest {
//
////  performs injection of dependencies for test cases.
//    @get:Rule(order = 0)
//    var hiltRule = HiltAndroidRule(this)
//
//    @Inject
//    lateinit var actualUserRepository: UserRepository
//
//    @Before
//    fun setup() {
//        hiltRule.inject()
//    }
//
//    @Test
//    fun when_authServOperator_returns_FirebaseAuthenticationService_then_provideFirebaseUserRepository() = runBlocking {
//
//        this.launch {
//            AuthenticationServiceOperator.setFirebaseAuthenticationService()
//        }.join()
//
//        val expectedUserRepository = FirebaseUserRepository::class
//
//        assertEquals(
//            expectedUserRepository,
//            actualUserRepository::class
//        )
//    }
//
//    @Test
//    fun when_authServOperator_returns_RoomAuthenticationService_then_provideRoomUserRepository() = runBlocking {
//
//        this.launch {
//            AuthenticationServiceOperator.setRoomAuthenticationService()
//        }.join()
//
//        val expectedUserRepository = RoomUserRepository::class
//
//        assertEquals(
//            expectedUserRepository,
//            actualUserRepository::class
//        )
//    }
//
//    @Module
//    @InstallIn(SingletonComponent::class)
//    class FakeUserRepositoryModule {
//
//        @Provides
//        @Named("uniqueIdentifier")
//        fun provideUniqueIdentifier(): String? = "123"
//
//        @Provides
//        @Reusable
//        fun provideUserRepository(
//            @Named("uniqueIdentifier") lazyUniqueIdentifier: Lazy<String>,
//            @ApplicationContext appContext: Lazy<Context>
//        ): UserRepository {
//
//            when (AuthenticationServiceOperator.service.value) {
//
//                AuthenticationMethod.Firebase -> {
//
//                    val uniqueIdentifier = lazyUniqueIdentifier.get()
//                    return FirebaseUserRepository(uniqueIdentifier = uniqueIdentifier)
//                }
//
//                AuthenticationMethod.Room -> {
//
//                    val roomDatabase = Room.databaseBuilder(
//                        appContext.get(),
//                        RoomDatabase::class.java, com.example.globalConstants.Room.databaseName,
//                    ).build()
//
//                    return RoomUserRepository(
//                        roomDatabase = roomDatabase
//                    )
//                }
//            }
//        }
//    }
//}