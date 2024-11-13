package com.example.trackies.auth.serviceOperator

import com.example.trackies.auth.buisness.AuthenticationMethod
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test

//class AuthenticationServiceOperatorTest {
//
//    @Test
//    fun firebaseAuthenticationService_isProperlyReturned () = runBlocking {
//
//        this.launch {
//            AuthenticationServiceOperator.setFirebaseAuthenticationService()
//        }.join()
//
//        val expectedValue = AuthenticationMethod.Firebase
//        val actualValue = AuthenticationServiceOperator.service.value
//
//        assertEquals(
//            expectedValue,
//            actualValue
//        )
//    }
//
//    @Test
//    fun roomAuthenticationService_isProperlyReturned () = runTest {
//
//        this.launch {
//            AuthenticationServiceOperator.setRoomAuthenticationService()
//        }.join()
//
//        val expectedValue = AuthenticationMethod.Room
//        val actualValue = AuthenticationServiceOperator.service.value
//
//        assertEquals(
//            expectedValue,
//            actualValue
//        )
//    }
//
//    @Test
//    fun firebaseIsSetToDefaultAuthenticationService () = runBlocking {
//
//        val expectedValue = AuthenticationMethod.Firebase
//        val actualValue = AuthenticationServiceOperator.service.value
//
//        assertEquals(
//            expectedValue,
//            actualValue
//        )
//    }
//
//    @Test
//    fun switchingBetweenAuthServices() = runBlocking {
//
//        AuthenticationServiceOperator.setRoomAuthenticationService()
//
//        delay(1000)
//
//
//        val expectedValue1 = AuthenticationMethod.Room
//        val actualValue1 = AuthenticationServiceOperator.service.value
//
//        assertEquals(
//            expectedValue1,
//            actualValue1
//        )
//
//        delay(1000)
//
//        AuthenticationServiceOperator.setRoomAuthenticationService()
//
//        delay(1000)
//
//        val expectedValue2 = AuthenticationMethod.Room
//        val actualValue2 = AuthenticationServiceOperator.service.value
//
//        assertEquals(
//            expectedValue2,
//            actualValue2
//        )
//
//        delay(1000)
//
//        AuthenticationServiceOperator.setFirebaseAuthenticationService()
//
//        delay(1000)
//
//        val expectedValue3 = AuthenticationMethod.Firebase
//        val actualValue3 = AuthenticationServiceOperator.service.value
//
//        assertEquals(
//            expectedValue3,
//            actualValue3
//        )
//    }
//}
