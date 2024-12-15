package com.example.trackies.testsOfUi.welcomeScreen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.trackies.isSignedOut.presentation.ui.welcomeScreen
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestOfWelcomeScreen {

    @get:Rule(order = 0)
    val composeTestRule = createComposeRule()

    private var onNavigateSignUpWasClicked = false
    private var onNavigateSignInWasClicked = false
    private var onContinueAsGuestWasClicked = false

    @Before
    fun beforeTest() {

        onNavigateSignUpWasClicked = false
        onNavigateSignInWasClicked = false
        onContinueAsGuestWasClicked = false

        composeTestRule.setContent {

            welcomeScreen(

                onNavigateSignUp = {

                    onNavigateSignUpWasClicked = true
                },

                onNavigateSignIn = {

                    onNavigateSignInWasClicked = true
                },

                onContinueAsGuest = {

                    onContinueAsGuestWasClicked = true
                }
            )
        }
    }

    @Test
    fun contentIsDisplayedProperly() {

        composeTestRule.onNodeWithText("Hey there!").assertIsDisplayed()
        buttonSignUp.assertIsDisplayed()
        buttonSignIn.assertIsDisplayed()
        buttonContinueAsGuest.assertIsDisplayed()
    }

    @Test
    fun buttonSignUp_calls_onNavigateSignUp() {

        buttonSignUp.performClick()
        assertTrue(onNavigateSignUpWasClicked)
    }

    @Test
    fun buttonSignIn_calls_onNavigateSignIn() {

        buttonSignIn.performClick()
        assertTrue(onNavigateSignInWasClicked)
    }

    @Test
    fun buttonContinueAsGuest_calls_onContinueAsGuest() {

        buttonContinueAsGuest.performClick()
        assertTrue(onContinueAsGuestWasClicked)
    }

    private val buttonSignUp = composeTestRule.onNodeWithText("Sign up")
    private val buttonSignIn = composeTestRule.onNodeWithText("Sign in")
    private val buttonContinueAsGuest = composeTestRule.onNodeWithText("continue as guest")
}