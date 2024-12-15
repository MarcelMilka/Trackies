package testOfAuthenticationMethodProvider

import com.example.globalConstants.Destinations
import com.example.trackies.auth.authenticationMethodProvider.AuthenticationMethodProvider
import com.example.trackies.auth.buisness.AuthenticationMethod
import com.example.trackies.auth.data.AuthenticationService
import com.google.common.base.Verify.verify
import io.mockk.coEvery
import io.mockk.verify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthenticationMethodProviderUnitTest {

    private lateinit var authenticationMethodProvider: AuthenticationMethodProvider

    @MockK
    private lateinit var firebaseAuthService: AuthenticationService

    @MockK
    private lateinit var roomAuthService: AuthenticationService

    @Before
    fun beforeTest() {

        firebaseAuthService = mockk(relaxed = true)
        roomAuthService = mockk(relaxed = true)

        authenticationMethodProvider = AuthenticationMethodProvider(
            firebaseAuthService = firebaseAuthService,
            roomAuthService = roomAuthService
        )
    }

    @Test
    fun `getAuthenticationService() returns firebaseAuthenticationService by default`() = runTest {

        val expected = firebaseAuthService
        val actual = authenticationMethodProvider.getAuthenticationService()
        assertEquals(expected, actual)
    }

    @Test
    fun `getAuthenticationService() returns roomAuthenticationService when needed`() = runTest {

        authenticationMethodProvider.setAuthenticationMethod(
            mode = AuthenticationMethod.Room
        )

        val expected = roomAuthService
        val actual = authenticationMethodProvider.getAuthenticationService()
        assertEquals(expected, actual)
    }

    @Test
    fun `getAuthenticationService() returns properly returns appropriate authentication service`() = runTest {

        authenticationMethodProvider.setAuthenticationMethod(
            mode = AuthenticationMethod.Room
        )

        val expected1 = roomAuthService
        val actual1 = authenticationMethodProvider.getAuthenticationService()
        assertEquals(expected1, actual1)

        authenticationMethodProvider.setAuthenticationMethod(
            mode = AuthenticationMethod.Firebase
        )

        val expected2 = firebaseAuthService
        val actual2 = authenticationMethodProvider.getAuthenticationService()
        assertEquals(expected2, actual2)

        authenticationMethodProvider.setAuthenticationMethod(
            mode = AuthenticationMethod.Room
        )

        val expected3 = roomAuthService
        val actual3 = authenticationMethodProvider.getAuthenticationService()
        assertEquals(expected3, actual3)

        val expected4 = roomAuthService
        val actual4 = authenticationMethodProvider.getAuthenticationService()
        assertEquals(expected4, actual4)
    }

    @Test
    fun `getAuthenticationMethod() properly returns appropriate authenticationMethod`() {

        authenticationMethodProvider.setAuthenticationMethod(AuthenticationMethod.Room)

        val expected1 = AuthenticationMethod.Room
        val actual1 = authenticationMethodProvider.getAuthenticationMethod()
        assertEquals(expected1, actual1)

        authenticationMethodProvider.setAuthenticationMethod(AuthenticationMethod.Firebase)

        val expected2 = AuthenticationMethod.Firebase
        val actual2 = authenticationMethodProvider.getAuthenticationMethod()
        assertEquals(expected2, actual2)

        val expected3 = AuthenticationMethod.Firebase
        val actual3 = authenticationMethodProvider.getAuthenticationMethod()
        assertEquals(expected3, actual3)

        authenticationMethodProvider.setAuthenticationMethod(AuthenticationMethod.Room)

        val expected4 = AuthenticationMethod.Room
        val actual4 = authenticationMethodProvider.getAuthenticationMethod()
        assertEquals(expected4, actual4)

        val expected5 = AuthenticationMethod.Room
        val actual5 = authenticationMethodProvider.getAuthenticationMethod()
        assertEquals(expected5, actual5)
    }

    @Test
    fun `getInitialDestination() returns 'isSignedIn' when user is signed in with firebase`() {

        every { firebaseAuthService.initialDestination } returns Destinations.IsSignedIn
        every { roomAuthService.initialDestination } returns Destinations.IsSignedOut

        val expected = Destinations.IsSignedIn
        val actual = authenticationMethodProvider.getInitialDestination()
        assertEquals(expected, actual)
    }

    @Test
    fun `getInitialDestination() returns 'isSignedIn' when user is signed in with firebase and room`() {

        every { firebaseAuthService.initialDestination } returns Destinations.IsSignedIn
        every { roomAuthService.initialDestination } returns Destinations.IsSignedIn

        val expected = Destinations.IsSignedIn
        val actual = authenticationMethodProvider.getInitialDestination()
        assertEquals(expected, actual)
    }

    @Test
    fun `getInitialDestination() returns 'isSignedIn' when user is signed in with room`() {

        every { firebaseAuthService.initialDestination } returns Destinations.IsSignedOut
        every { roomAuthService.initialDestination } returns Destinations.IsSignedIn

        val expected = Destinations.IsSignedIn
        val actual = authenticationMethodProvider.getInitialDestination()
        assertEquals(expected, actual)
    }

    @Test
    fun `getInitialDestination() returns 'isSignedOut' when user is not signed in anywhere`() {

        every { firebaseAuthService.initialDestination } returns Destinations.IsSignedOut
        every { roomAuthService.initialDestination } returns Destinations.IsSignedOut

        val expected = Destinations.IsSignedOut
        val actual = authenticationMethodProvider.getInitialDestination()
        assertEquals(expected, actual)
    }

    @Test
    fun `getFirebaseUniqueID() returns proper string`() {

        every { firebaseAuthService.getSignedInUser() } returns "lskjdfhlsjhfpASIUFHP"

        val expected = "lskjdfhlsjhfpASIUFHP"
        val actual = authenticationMethodProvider.getFirebaseUniqueID()
        assertEquals(expected, actual)
    }

    @Test
    fun `getFirebaseUniqueID() returns null`() {

        every { firebaseAuthService.getSignedInUser() } returns null

        val expected = null
        val actual = authenticationMethodProvider.getFirebaseUniqueID()
        assertEquals(expected, actual)
    }
}