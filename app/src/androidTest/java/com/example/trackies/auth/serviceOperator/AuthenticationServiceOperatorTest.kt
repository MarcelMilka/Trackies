package com.example.trackies.auth.serviceOperator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackies.auth.buisness.AuthenticationServices
import com.example.trackies.auth.di.AuthenticationModule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AuthenticationServiceOperatorTest {

    @Test
    fun firebaseAuthenticationService_isProperlyReturned () = runBlocking {

        this.launch {
            AuthenticationServiceOperator.setFirebaseAuthenticationService()
        }.join()

        val expectedValue = AuthenticationServices.FirebaseAuthenticationService
        val actualValue = AuthenticationServiceOperator.service.value

        assertEquals(
            expectedValue,
            actualValue
        )
    }

    @Test
    fun roomAuthenticationService_isProperlyReturned () = runTest {

        this.launch {
            AuthenticationServiceOperator.setRoomAuthenticationService()
        }.join()

        val expectedValue = AuthenticationServices.RoomAuthenticationService
        val actualValue = AuthenticationServiceOperator.service.value

        assertEquals(
            expectedValue,
            actualValue
        )
    }

    @Test
    fun firebaseIsSetToDefaultAuthenticationService () = runBlocking {

        val expectedValue = AuthenticationServices.FirebaseAuthenticationService
        val actualValue = AuthenticationServiceOperator.service.value

        assertEquals(
            expectedValue,
            actualValue
        )
    }

    @Test
    fun switchingBetweenAuthServices() = runBlocking {

        AuthenticationServiceOperator.setRoomAuthenticationService()

        delay(1000)


        val expectedValue1 = AuthenticationServices.RoomAuthenticationService
        val actualValue1 = AuthenticationServiceOperator.service.value

        assertEquals(
            expectedValue1,
            actualValue1
        )

        delay(1000)

        AuthenticationServiceOperator.setRoomAuthenticationService()

        delay(1000)

        val expectedValue2 = AuthenticationServices.RoomAuthenticationService
        val actualValue2 = AuthenticationServiceOperator.service.value

        assertEquals(
            expectedValue2,
            actualValue2
        )

        delay(1000)

        AuthenticationServiceOperator.setFirebaseAuthenticationService()

        delay(1000)

        val expectedValue3 = AuthenticationServices.FirebaseAuthenticationService
        val actualValue3 = AuthenticationServiceOperator.service.value

        assertEquals(
            expectedValue3,
            actualValue3
        )
    }
}
